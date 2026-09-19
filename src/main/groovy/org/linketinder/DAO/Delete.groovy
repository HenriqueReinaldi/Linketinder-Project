package org.linketinder.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

class Delete {
    static Connection conn

    //retorna true se deletar com sucesso
    static boolean execute_busca_delete(String busca, Closure busca_args) throws SQLException{
        PreparedStatement pst = conn.prepareStatement( busca )
        busca_args(pst)
        int delecoes = pst.executeUpdate()
        pst.close()

        if (delecoes > 0) return true
        return false
    }

    static boolean delete_candidato_by_id(int id) throws SQLException{
        String busca = """
            delete from candidato where id = ?
        """
        return execute_busca_delete(busca, {PreparedStatement pst -> pst.setInt(1, id)})
    }

    static boolean delete_empresa_by_id(int id) throws SQLException{
        String busca = """
            delete from empresa where id = ?
        """
        return execute_busca_delete(busca, {PreparedStatement pst -> pst.setInt(1, id)})
    }

    static boolean delete_vaga_by_id(int id) throws SQLException{
        String busca = """
            delete from vaga where id = ?
        """
        return execute_busca_delete(busca, {PreparedStatement pst -> pst.setInt(1, id)})
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
