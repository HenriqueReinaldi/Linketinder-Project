package org.linketinder.DAO.conexoes

import java.sql.Connection
import java.sql.SQLException

abstract class ProvedorBanco {
    String nome_banco
    String usuario
    String senha
    Boolean usar_ssl

    abstract Connection conectar() throws SQLException

    abstract void desconectar(Connection conn) throws SQLException
}
