package org.linketinder.database

import java.sql.Connection
import java.sql.PreparedStatement

class Delete {
    static Connection conn

    //retorna true se deletar com sucesso
    static boolean execute_delete_busca(String busca, Closure busca_args){
        int delecoes = 0
        try{
            PreparedStatement pst = conn.prepareStatement( busca )
            busca_args(pst)
            delecoes = pst.executeUpdate()
            pst.close()
        }
        catch (Exception e) { e.printStackTrace() }

        if (delecoes > 0) return true
        return false
    }

    static boolean delete_candidato_by_id(int id){
        String busca = """
            delete from candidato where id = ?
        """
        return execute_delete_busca(busca, {PreparedStatement pst -> pst.setInt(1, id)})
    }

    static boolean delete_empresa_by_id(int id){
        String busca = """
            delete from empresa where id = ?
        """
        return execute_delete_busca(busca, {PreparedStatement pst -> pst.setInt(1, id)})
    }

    static boolean delete_vaga_by_id(int id){
        String busca = """
            delete from vaga where id = ?
        """
        return execute_delete_busca(busca, {PreparedStatement pst -> pst.setInt(1, id)})
    }

    static boolean delete_competencia_by_id(int id){
        String busca = """
            delete from competencia where id = ?
        """
        return execute_delete_busca(busca, {PreparedStatement pst -> pst.setInt(1, id)})
    }


    static boolean deletar_entidade_competencias_by_entidadeid(String entidade, int id){
        String busca = """
            delete from ${entidade}_competencias where ${entidade}_id = ?
        """
        return execute_delete_busca(busca, {PreparedStatement pst -> pst.setInt(1, id)})
    }
}
