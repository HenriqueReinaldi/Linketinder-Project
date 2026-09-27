package org.linketinder.DAO.conexoes

import java.sql.Connection
import java.sql.SQLException

import static java.sql.DriverManager.getConnection

class ProvedorPostgres extends ProvedorBanco {
    String nome_banco = "linketinder"
    String usuario = "postgres"
    String senha = "postgres"
    Boolean usar_ssl = false

    @Override
    Connection conectar() throws SQLException {
        Properties props = new Properties()
        props.setProperty("user", usuario)
        props.setProperty("password", senha)
        props.setProperty("ssl", Boolean.toString(usar_ssl))
        String URL_SERV = "jdbc:postgresql://localhost:5432/${nome_banco}"

        return getConnection(URL_SERV, props)
    }

    @Override
    void desconectar(Connection conn) throws SQLException {
        if (conn == null) return
        conn.close()
    }
}
