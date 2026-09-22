package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Competencia
import org.linketinder.service.CompetenciaService

@TupleConstructor
class CompetenciaController {
    AssembleModel assemble_model
    CompetenciaService competencia_service

    List<Competencia> get_lista_competencia(){
        return competencia_service.get_lista()
    }
    void deletar_competencia(int id){
        competencia_service.deletar(id)
    }
    void update_competencia(ModelData modelo){
        Competencia c = assemble_model.assemble_competencia(modelo.data)
        competencia_service.update(c)
    }
}
