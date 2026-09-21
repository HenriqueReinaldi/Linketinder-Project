package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.Banco
import org.linketinder.DAO.CompetenciaDAO
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia

@TupleConstructor
class CompetenciaService {
    Banco bd
    CompetenciaDAO dao

    List<Competencia> get_lista(){
        try{
            return dao.get_lista_competencia()
        }
        catch (Exception ignored){
            return null
        }
    }

    void deletar(int id){
        try{
            bd.delete.delete_competencia_by_id(id)
        }
        catch (Exception ignored) {}
    }


    void update(Competencia c){
        try{
            bd.update.update_competencia(c)
        }
        catch (Exception ignored) {}
    }

    CompetenciaService(Banco bd){
        this.bd = bd
        this.dao = new CompetenciaDAO(bd)
    }
}
