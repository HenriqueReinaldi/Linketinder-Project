package org.linketinder.DAO.conexoes

import java.sql.Connection

class Conexao {
    static final Conexao instancia = new Conexao()
    ProvedorBanco provedor

    static Conexao get_instancia() {
        return instancia
    }

    private Conexao() {}


    void set_tipo_banco(String tipo_banco) {
        provedor = ProvedorFactory.pegar_provedor(tipo_banco)
    }

    void conectar() {
        provedor.conectar()
    }

    void desconectar() {
        provedor.desconectar()
    }

    Connection get_conexao() {
        return provedor.conn
    }

}
