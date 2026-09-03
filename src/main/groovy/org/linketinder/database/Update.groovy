package org.linketinder.database

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Vaga

import java.sql.Connection
import java.sql.PreparedStatement

class Update {
    static Connection conn

    static boolean execute_update_busca(String busca, Closure busca_args){
        int alteracoes = 0
        try{
            PreparedStatement pst = conn.prepareStatement( busca )
            busca_args(pst)
            alteracoes = pst.executeUpdate()
            pst.close()
        }
        catch (Exception e) { e.printStackTrace() }

        if (alteracoes > 0) return true
        return false
    }

    //recebe uma classe que representa a modificacao. modifica por ID
    static boolean update_candidato(Candidato c){
        if (c == null) return false

        int endereco_id = Create.cadastrar_endereco_if_not_exists(c.endereco)

        List<Integer> competencias_id  = []
        for (Competencia comp : c.competencias){
            competencias_id << Create.create_if_not_exists_competencia(comp.tecnologia)
        }

        String busca = """
            update candidato set
                nome = ?, sobrenome = ?, e_mail = ?, CPF = ?, descricao = ?,
                data_nascimento = ?, senha = ?, endereco_id = ?
            where id = ?
        """

        boolean status = execute_update_busca(busca) { PreparedStatement pst ->
            pst.setString(1, c.nome)
            pst.setString(2, c.sobrenome)
            pst.setString(3, c.email)
            pst.setString(4, c.CPF)
            pst.setString(5, c.descricao)
            pst.setDate(6, java.sql.Date.valueOf(c.data_nascimento))
            pst.setString(7, c.senha)
            pst.setInt(8, endereco_id)
            pst.setInt(9, c.id)
        }
        if (!status) return status

        Delete.deletar_entidade_competencias_by_entidadeid("candidato", c.id)
        for (int competencia_id : competencias_id){
            Create.cadastrar_competencias_entidade("candidato", c.id, competencia_id)
        }

        return status
    }

    static boolean update_vaga(Vaga v){
        if (v == null) return false

        int endereco_id = Create.cadastrar_endereco_if_not_exists(v.endereco)

        List<Integer> competencias_id  = []
        for (Competencia comp : v.competencias_desejadas){
            competencias_id << Create.create_if_not_exists_competencia(comp.tecnologia)
        }

        String busca = """
            update vaga set
                nome = ?, descricao = ?, endereco_id = ?, empresa_id = ?
            where id = ?
        """

        boolean status = execute_update_busca(busca) { PreparedStatement pst ->
            pst.setString(1, v.nome)
            pst.setString(2, v.descricao)
            pst.setInt(3, endereco_id)
            pst.setInt(4, v.empresa.id)
            pst.setInt(5, v.id)
        }
        if (!status) return status

        Delete.deletar_entidade_competencias_by_entidadeid("vaga", v.id)
        for (int competencia_id : competencias_id){
            Create.cadastrar_competencias_entidade("vaga", v.id, competencia_id)
        }

        return status
    }

    static boolean update_empresa(Empresa m){
        if (m == null) return false

        int endereco_id = Create.cadastrar_endereco_if_not_exists(m.endereco)
        String busca = """
            update empresa set
                nome = ?, e_mail = ?, CNPJ = ?, descricao = ?, senha = ?, endereco_id = ?
            where id = ?
        """

        boolean status = execute_update_busca(busca) { PreparedStatement pst ->
            pst.setString(1, m.nome)
            pst.setString(2, m.email)
            pst.setString(3, m.CNPJ)
            pst.setString(4, m.descricao)
            pst.setString(5, m.senha)
            pst.setInt(6, endereco_id)
            pst.setInt(7, m.id)//67
        }

        return status
    }

    static boolean update_competencia(Competencia c){
        if (c == null) return false

        String busca = "update competencia set tecnologia = ? where id = ?"

        boolean status = execute_update_busca(busca) { PreparedStatement pst ->
            pst.setString(1, c.tecnologia)
            pst.setInt(2, c.id)
        }

        return status
    }
}
