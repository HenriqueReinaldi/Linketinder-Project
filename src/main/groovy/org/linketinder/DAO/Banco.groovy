package org.linketinder.DAO

import org.linketinder.DAO.old.Create
import org.linketinder.DAO.old.Delete
import org.linketinder.DAO.old.Read
import org.linketinder.DAO.old.Update

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

import static java.sql.DriverManager.getConnection as getConnection

class Banco {
    static String nome_banco
    static Connection conn

    static int return_id_from_busca(String busca, Closure busca_args) throws SQLException {
        //retorna o campo "id" resultante da busca SQL
        String id = ""
        PreparedStatement pst = conn.prepareStatement(busca, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        busca_args(pst)
        ResultSet res = pst.executeQuery()

        res.beforeFirst()
        if (res.next()) id = res.getString("id")

        res.close()
        pst.close()

        try {
            return Integer.parseInt(id)
        } catch (Exception ignored) {
            throw new SQLException("retornado id não numérico")
        }
    }

    static void execute_busca(String busca, Closure busca_args) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(busca)
        busca_args(pst)
        pst.execute()
        pst.close()
    }

    static String get_coluna_from_entrada_id(int id, String tabela, String coluna) throws SQLException {
        String resultado = ""
        String busca = "select * from ${tabela} where id = ?"

        PreparedStatement pst = conn.prepareStatement(busca, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        pst.setInt(1, id)
        ResultSet res = pst.executeQuery()

        res.beforeFirst()
        if (res.next()) resultado = res.getString(coluna)

        res.close()
        pst.close()
        return resultado
    }

    static <Generico> List<Generico> get_lista_tabela(String busca, Closure busca_args, Closure<Generico> construtor) throws SQLException {
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

    static boolean execute_busca_detect_updates(String busca, Closure busca_args) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(busca)
        busca_args(pst)
        int alteracoes = pst.executeUpdate()
        pst.close()

        if (alteracoes > 0) return true
        return false
    }

    static void conectar() throws SQLException {
        Properties props = new Properties()
        props.setProperty("user", "postgres")
        props.setProperty("password", "postgres")
        props.setProperty("ssl", "false")
        String URL_SERV = "jdbc:postgresql://localhost:5432/${nome_banco}"
        //extremamente seguro.

        conn = getConnection(URL_SERV, props)
    }

    static void desconectar() throws SQLException {
        if (conn == null) return

        conn.close();
    }

    Banco(String nome_banco) {
        this.nome_banco = nome_banco
        conectar()
    }
}

