package org.linketinder.DAO

import org.linketinder.DAO.old.Create
import org.linketinder.DAO.old.Delete
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class VagaDAO {
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

    static int cadastrar_vaga_if_not_exists(Vaga v) throws SQLException{
        /*
            TODO:
            CRIACAO DAS TABELAS VAGA_COMPETENCIA EM SERVICE
            CRIACAO DO ENDERE4CO EM SERVICE
         */
        if (v == null) return -1


        String busca = """
            insert into vaga (nome, descricao, endereco_id, empresa_id) 
            values (?, ?, ?, ?) returning id
        """
        int id_novo = return_id_from_busca(busca) {PreparedStatement pst ->
            pst.setString(1, v.nome)
            pst.setString(2, v.descricao)
            pst.setInt(3, v.endereco.id)
            pst.setInt(4, v.empresa.id)
        }
        return id_novo
    }


    static List<Vaga> get_lista_vaga() throws SQLException{
        /*
        todo:
        mandar service completar endereco empresa e competencias desejadas
         */

        List<Vaga> vagas = get_lista_tabela("""
            select 
                v.id AS vaga_id,
                v.nome AS vaga_nome,
                v.descricao AS vaga_descricao,
                v.endereco_id AS vaga_endereco_id,
                e.id AS empresa_id
            from vaga as v join empresa as e on e.id = v.empresa_id """, {}
        ) { ResultSet res ->
            int id = res.getInt("vaga_id")
            return new Vaga(
                    id: id,
                    nome: res.getString("vaga_nome"),
                    descricao: res.getString("vaga_descricao"),
                    endereco: new Endereco(id: res.getInt("vaga_endereco_id")),
                    empresa: new Empresa(id: res.getInt("empresa_id")),
                    competencias_desejadas: null
            )
        }
        return vagas;
    }
    static Vaga get_vaga_by_id(int id) throws SQLException{
        /*
        todo:
        mandar service completar endereco empresa e competencias desejadas
         */

        String busca = "select * from vaga where id = ?"

        List<Vaga> vagas = get_lista_tabela(busca, {
            PreparedStatement pst -> pst.setInt(1, id)
        }) { ResultSet res ->
            int vid = res.getInt("id")
            return new Vaga(
                    id: vid,
                    nome: res.getString("nome"),
                    descricao: res.getString("descricao"),
                    endereco: new Endereco(id: res.getInt("vaga_endereco_id")),
                    empresa: new Empresa(id: res.getInt("empresa_id")),
                    competencias_desejadas: null
            )
        }

        if (!vagas) return null
        return vagas[0]
    }


    static boolean update_vaga(Vaga v) throws SQLException{
        /*
            TODO:
            CRIACAO DAS TABELAS VAGA_COMPETENCIA EM SERVICE
            CRIACAO DO ENDERE4CO EM SERVICE
         */

        if (v == null) return false
        String busca = """
            update vaga set
                nome = ?, descricao = ?, endereco_id = ?, empresa_id = ?
            where id = ?
        """

        boolean troca_aconteceu = execute_update_busca(busca) { PreparedStatement pst ->
            pst.setString(1, v.nome)
            pst.setString(2, v.descricao)
            pst.setInt(3, v.endereco.id)
            pst.setInt(4, v.empresa.id)
            pst.setInt(5, v.id)
        }

        return troca_aconteceu
    }


    static boolean delete_vaga_by_id(int id) throws SQLException{
        String busca = """
            delete from vaga where id = ?
        """
        return execute_busca_delete(busca, {PreparedStatement pst -> pst.setInt(1, id)})
    }
}
