package org.linketinder.DAO.conexoes

import java.sql.Connection
import java.sql.SQLException

import static java.sql.DriverManager.getConnection

class ProvedorPostgres extends ProvedorBanco {

    @Override
    void conectar() throws SQLException {
        if (conn != null) return

        Properties props = new Properties()
        props.setProperty("user", usuario)
        props.setProperty("password", senha)
        props.setProperty("ssl", Boolean.toString(usar_ssl))
        String URL_SERV = "jdbc:postgresql://localhost:5432/${nome_banco}"

        conn = getConnection(URL_SERV, props)
    }

    @Override
    void desconectar() throws SQLException {
        if (conn == null) return
        conn.close()
        conn = null
    }

    ProvedorPostgres(){
        this.nome_banco = "linketinder"
        this.usuario = "postgres"
        this.senha = "postgres"
        this.usar_ssl = false
        this.conn = null
    }
}
