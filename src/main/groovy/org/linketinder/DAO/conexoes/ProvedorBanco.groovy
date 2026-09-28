package org.linketinder.DAO.conexoes

import java.sql.Connection
import java.sql.SQLException

abstract class ProvedorBanco {
    protected String nome_banco
    protected String usuario
    protected String senha
    protected Boolean usar_ssl
    public Connection conn

    abstract void conectar() throws SQLException
    abstract void desconectar() throws SQLException
}
