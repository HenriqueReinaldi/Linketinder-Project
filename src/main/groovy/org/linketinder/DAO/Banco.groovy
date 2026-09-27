package org.linketinder.DAO

import org.linketinder.DAO.conexoes.ProvedorBanco
import org.linketinder.DAO.conexoes.ProvedorFactory

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


class Banco {
    static ProvedorBanco provedor
    static Connection conn

    static boolean executar_detectar_updates(String busca, Closure busca_args) throws SQLException {
        PreparedStatement pst = null

        try {
            pst = conn.prepareStatement(busca)
            busca_args(pst)

            if (pst.executeUpdate() > 0) return true
            return false
        }
        finally {
            pst?.close()
        }
    }

    static <GENERICO> GENERICO executar(String busca, Closure busca_args, Closure<GENERICO> retornador) throws SQLException {
        PreparedStatement pst = null
        ResultSet res = null

        try {
            pst = conn.prepareStatement(busca)
            busca_args(pst)

            res = pst.executeQuery()
            return retornador(res)
        }
        finally {
            res?.close()
            pst?.close()
        }
    }

    Banco(String tipo_banco) throws SQLException {
        provedor = ProvedorFactory.pegar_provedor(tipo_banco)
        conn = provedor.conectar()
    }
}

