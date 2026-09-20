package org.linketinder.DAO

import org.linketinder.DAO.old.Read
import org.linketinder.model.objetos.Endereco

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class EnderecoDAO {
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


    static int create_if_not_exists_pais(String nome) throws SQLException{
        String busca = """
            insert into pais (nome) values (?)
            on conflict (nome) do update set nome = pais.nome 
            returning id
            """
        return return_id_from_busca(busca) { PreparedStatement pst ->
            pst.setString(1, nome)
        }
    }
    static int create_if_not_exists_estado(String nome) throws SQLException{
        String busca = """
            insert into estado (nome) values (?)
            on conflict (nome) do update set nome = estado.nome 
            returning id
            """
        return return_id_from_busca(busca) { PreparedStatement pst ->
            pst.setString(1, nome)
        }
    }
    static int cadastrar_endereco_if_not_exists(Endereco e) throws SQLException{
        if (e == null) return -1

        if (e.pais == null) e.pais = "default"
        if (e.estado == null) e.estado = "default"

        int endereco_id = get_endereco_id(e)
        if (endereco_id != -1) return endereco_id

        int pais_id = create_if_not_exists_pais(e.pais)
        int estado_id = create_if_not_exists_estado(e.estado)

        String busca = "insert into endereco (CEP, pais_id, estado_id) values (?, ?, ?) returning id"
        return return_id_from_busca(busca) { PreparedStatement pst ->
            pst.setString(1, e.CEP)
            pst.setInt(2, pais_id)
            pst.setInt(3, estado_id)
        }
    }

    static int get_endereco_id(Endereco e) throws SQLException{
        String busca = """
            select 
                e.id as id,
                p.nome as pais_nome,
                es.nome as estado_nome,
                CEP
            from endereco as e
            join pais p on e.pais_id = p.id 
            join estado es on e.estado_id = es.id
            where CEP = ?
            and p.nome = ?
            and es.nome = ?
        """

        List<Integer> endereco = get_lista_tabela(busca, {
            PreparedStatement pst ->
                pst.setString(1, e.CEP)
                pst.setString(2, e.pais)
                pst.setString(3, e.estado)
        }) { ResultSet res -> return res.getInt("id")}

        if (!endereco) return -1
        return endereco[0]
    }
    static Endereco get_endereco_by_id(int id) throws SQLException{
        String busca = """
            select 
                e.id as endereco_id,
                p.nome as pais_nome,
                es.nome as estado_nome,
                CEP
            from endereco as e
            join pais p on e.pais_id = p.id 
            join estado es on e.estado_id = es.id
            where e.id = ?
        """

        List<Endereco> enderecos = get_lista_tabela(busca, {
            PreparedStatement pst -> pst.setInt(1, id)
        }) { ResultSet res ->
            return new Endereco(
                    id: res.getInt("endereco_id"),
                    pais: res.getString("pais_nome"),
                    estado: res.getString("estado_nome"),
                    CEP: res.getString("CEP"),
            )
        }

        if (!enderecos) return null
        return enderecos[0]
    }
}
