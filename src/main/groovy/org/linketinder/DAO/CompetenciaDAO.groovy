package org.linketinder.DAO

import org.linketinder.model.objetos.Competencia

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class CompetenciaDAO {
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


    static int create_if_not_exists_competencia(String tecnologia) throws SQLException{
        String busca = """
            insert into competencia (tecnologia) values (?)
            on conflict (tecnologia) do update set tecnologia = competencia.tecnologia 
            returning id
            """
        return return_id_from_busca(busca) { PreparedStatement pst ->
            pst.setString(1, tecnologia)
        }
    }
    static void cadastrar_competencias_entidade(String entidade, int id_entidade, int id_competencia) throws SQLException{
        String busca = """
            insert into ${entidade}_competencias (${entidade}_id, competencia_id) values (?, ?)
            on conflict (${entidade}_id, competencia_id) do nothing
        """

        execute_busca(busca) {PreparedStatement pst ->
            pst.setInt(1, id_entidade)
            pst.setInt(2, id_competencia)
        }
    }


    static List<Competencia> get_lista_competencia() throws SQLException{
        List<Competencia> competencias = get_lista_tabela("select * from competencia", {}) { ResultSet res ->
            return new Competencia(
                    id: res.getInt("id"),
                    tecnologia: res.getString("tecnologia"),
            )
        }
        return competencias
    }
    static List<Competencia> get_lista_competencias_of_entidade(String entidade, int entidade_id)throws SQLException{
        List<Competencia> competencias = get_lista_tabela("select * from ${entidade}_competencias where ${entidade}_id = ?",
                {PreparedStatement pst -> pst.setInt(1, entidade_id) })
                { ResultSet res ->
                    int id = res.getInt("competencia_id")
                    return new Competencia(
                            id: id,
                            tecnologia: get_coluna_from_entrada_id(id, "competencia", "tecnologia")
                    )
                }
        return competencias
    }

    static boolean update_competencia(Competencia c) throws SQLException{
        if (c == null) return false
        String busca = "update competencia set tecnologia = ? where id = ?"

        return execute_update_busca(busca) { PreparedStatement pst ->
            pst.setString(1, c.tecnologia)
            pst.setInt(2, c.id)
        }
    }


    static boolean delete_competencia_by_id(int id) throws SQLException{
        String busca = """
            delete from competencia where id = ?
        """
        return execute_busca_delete(busca, {PreparedStatement pst -> pst.setInt(1, id)})
    }

    static boolean delete_entidade_competencias_by_entidadeid(String entidade, int id) throws SQLException{
        String busca = """
            delete from ${entidade}_competencias where ${entidade}_id = ?
        """
        return execute_busca_delete(busca, {PreparedStatement pst -> pst.setInt(1, id)})
    }
}
