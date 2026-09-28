package org.linketinder.DAO

import org.linketinder.DAO.conexoes.Conexao
import org.linketinder.DAO.conexoes.ProvedorBanco
import org.linketinder.DAO.conexoes.ProvedorFactory

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


class Banco {
    static Conexao conexao

    static void desconectar() throws SQLException {
        conexao.desconectar()
    }

    static boolean executar_detectar_updates(String busca, Closure busca_args) throws SQLException {
        PreparedStatement pst = null

        try {
            pst = conexao.get_conexao().prepareStatement(busca)
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
            pst = conexao.get_conexao().prepareStatement(busca)
            busca_args(pst)

            boolean tem_resultset = pst.execute()
            if (tem_resultset){
                res = pst.getResultSet()
                return retornador(res)
            }
            else{
                return null
            }

        }
        finally {
            res?.close()
            pst?.close()
        }
    }

    Banco(String tipo_banco) {
        conexao = Conexao.get_instancia()
        conexao.set_tipo_banco(tipo_banco)
        conexao.conectar()
    }
}

