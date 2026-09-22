package org.linketinder.controller

import groovy.transform.TupleConstructor
import groovy.transform.Undefined.EXCEPTION
import org.linketinder.model.objetos.Competencia
import org.linketinder.service.CompetenciaService

@TupleConstructor
class CompetenciaController {
    CompetenciaService competencia_service

    List<Competencia> get_lista_competencia() {
        return competencia_service.get_lista()
    }

    void deletar_competencia(int id) {
        competencia_service.deletar(id)
    }

    void update_competencia(ModelData modelo) {
        Competencia c = assemble_competencia(modelo.data)
        competencia_service.update(c)
    }

    Competencia assemble_competencia(Map<String, String> competencia_info) {
        try {
            return new Competencia(
                    tecnologia: competencia_info.tecnologia,
                    id: competencia_info.id ? competencia_info.id.toInteger() : -1
            )
        }
        catch (Exception ignored) {
            return null
        }
    }
}
