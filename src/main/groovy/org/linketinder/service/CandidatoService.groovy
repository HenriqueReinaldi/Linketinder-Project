package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.Banco
import org.linketinder.DAO.CandidatoDAO
import org.linketinder.DAO.CompetenciaDAO
import org.linketinder.DAO.CurtidaDAO
import org.linketinder.DAO.EnderecoDAO
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Curtida

@TupleConstructor
class CandidatoService {
    Banco bd

    CandidatoDAO dao
    CompetenciaDAO competencia_dao
    EnderecoDAO endereco_dao
    CurtidaDAO curtida_dao

    List<Candidato> get_lista(){
        try{
            List<Candidato> candidatos = dao.get_lista_candidato()

            candidatos.each {Candidato c ->
                c.endereco = endereco_dao.get_endereco_by_id(c.endereco.id)
                c.competencias = competencia_dao.get_lista_competencias_of_entidade("candidato", c.id)
            }

            return candidatos
        }
        catch (Exception ignored){
            return []
        }
    }

    void cadastrar(Candidato c){
        try{
            int endereco_id = endereco_dao.cadastrar_endereco_se_nao_existe(c.endereco)
            if (endereco_id < 0) return
            c.endereco.id = endereco_id

            int candidato_id = dao.cadastrar_candidato_se_nao_existe(c)
            if (candidato_id < 0) return

            List<Integer> competencias_id  = []
            for (Competencia comp : c.competencias){
                competencias_id << competencia_dao.create_if_not_exists_competencia(comp.tecnologia)
            }
            for (int competencia_id : competencias_id){
                competencia_dao.cadastrar_competencias_entidade("candidato", candidato_id, competencia_id)
            }
        }
        catch (Exception e) {
            e.printStackTrace()
        }
    }

    void deletar(int id){
        try{
            dao.delete_candidato_by_id(id)
        }
        catch (Exception ignored) {}
    }

    void update(Candidato c){
        try{
            int endereco_id = endereco_dao.cadastrar_endereco_se_nao_existe(c.endereco)
            if (endereco_id < 0) return
            c.endereco.id = endereco_id

            dao.update_candidato(c)

            competencia_dao.delete_entidade_competencias_by_entidadeid("candidato", c.id)

            List<Integer> competencias_id  = []
            for (Competencia comp : c.competencias){
                competencias_id << competencia_dao.create_if_not_exists_competencia(comp.tecnologia)
            }
            for (int competencia_id : competencias_id){
                competencia_dao.cadastrar_competencias_entidade("candidato", c.id, competencia_id)
            }
        }
        catch (Exception ignored) {}
    }

    void curtir(Curtida c){
        try{
            curtida_dao.cadastrar_curtida(c)
        }
        catch (Exception e) {
            e.printStackTrace()
        }
    }

    Candidato get_by_id(String id){
        try {
            return dao.get_candidato_by_id(Integer.parseInt(id))
        } catch (Exception ignored) {
            return null
        }
    }

    CandidatoService(Banco bd){
        this.bd = bd
        this.dao = new CandidatoDAO(bd)
        this.competencia_dao = new CompetenciaDAO(bd)
        this.endereco_dao = new EnderecoDAO(bd)
        this.curtida_dao = new CurtidaDAO(bd)
    }
}
