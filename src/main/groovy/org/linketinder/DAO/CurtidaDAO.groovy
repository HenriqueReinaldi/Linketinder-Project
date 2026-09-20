package org.linketinder.DAO

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Vaga

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class CurtidaDAO {
    static Connection conn

    static int return_id_from_busca(String busca, Closure busca_args) throws SQLException{
        //retorna o campo "id" resultante da busca SQL
        String id = ""
        PreparedStatement pst = conn.prepareStatement( busca, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY )
        busca_args(pst)
        ResultSet res = pst.executeQuery()

        res.beforeFirst()
        if (res.next()) id = res.getString("id")

        res.close()
        pst.close()

        try {
            return Integer.parseInt(id)
        } catch (Exception ignored){
            throw new SQLException("retornado id não numérico")
        }
    }
    static void execute_busca(String busca, Closure busca_args) throws SQLException{
        PreparedStatement pst = conn.prepareStatement( busca )
        busca_args(pst)
        pst.execute()
        pst.close()
    }
    static boolean execute_busca_delete(String busca, Closure busca_args) throws SQLException{
        PreparedStatement pst = conn.prepareStatement( busca )
        busca_args(pst)
        int delecoes = pst.executeUpdate()
        pst.close()

        if (delecoes > 0) return true
        return false
    }
    static String get_coluna_from_entrada_id(int id, String tabela, String coluna) throws SQLException{
        String resultado = ""
        String busca = "select * from ${tabela} where id = ?"

        PreparedStatement pst = conn.prepareStatement( busca, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY )
        pst.setInt(1, id)
        ResultSet res = pst.executeQuery()

        res.beforeFirst()
        if (res.next()) resultado = res.getString(coluna)

        res.close()
        pst.close()
        return resultado
    }
    static <Generico> List<Generico> get_lista_tabela(String busca, Closure busca_args, Closure<Generico> construtor) throws SQLException{
        List<Generico> genericos = []

        PreparedStatement pst = conn.prepareStatement(
                busca,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        )
        busca_args(pst)
        ResultSet res = pst.executeQuery()
        res.beforeFirst()
        while (res.next()) {
            Generico g = construtor(res)
            genericos << g
        }
        res.close()
        pst.close()

        return genericos
    }
    static boolean execute_update_busca(String busca, Closure busca_args) throws SQLException{
        PreparedStatement pst = conn.prepareStatement( busca )
        busca_args(pst)
        int alteracoes = pst.executeUpdate()
        pst.close()

        if (alteracoes > 0) return true
        return false
    }

    static void cadastrar_curtida(Curtida c) throws SQLException{
        if (c == null) return

        String busca = """
            insert into curtida (candidato_id, vaga_id) 
            values (?, ?) on conflict (candidato_id, vaga_id) do nothing
        """

        execute_busca(busca) {PreparedStatement pst ->
            pst.setInt(1, c.candidato.id)
            pst.setInt(2, c.vaga.id)
        }

    }

    static List<Curtida> get_lista_curtida() throws SQLException{
        /*
            TODO:
            completar candidato e vaga em service.
         */

        List<Curtida> curtidas = get_lista_tabela("select * from curtida", {}) { ResultSet res ->
            return new Curtida(
                    candidato: new Candidato(id: res.getInt("candidato_id")),
                    vaga: new Vaga(id: res.getInt("vaga_id")),
                    empresa_curtiu: res.getBoolean("empresa_curtiu")
            )
        }
        return curtidas
    }

    static boolean empresa_curtir(Curtida c) throws SQLException{
        String busca = "update curtida set empresa_curtiu = true where candidato_id = ? and vaga_id = ?"

        return execute_update_busca(busca) { PreparedStatement pst ->
            pst.setInt(1, c.candidato.id)
            pst.setInt(2, c.vaga.id)
        }
    }
}
