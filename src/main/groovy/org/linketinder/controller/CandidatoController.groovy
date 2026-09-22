package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Curtida
import org.linketinder.service.CandidatoService

@TupleConstructor
class CandidatoController {
    AssembleModel assemble_model
    CandidatoService candidato_service

    List<Candidato> get_lista_candidato() {
        return candidato_service.get_lista()
    }

    void cadastrar_candidato(ModelData modelo) {
        Candidato c = assemble_model.assemble_candidato(modelo.data)
        candidato_service.cadastrar(c)
    }

    void deletar_candidato(int id) {
        candidato_service.deletar(id)
    }

    void update_candidato(ModelData modelo) {
        Candidato c = assemble_model.assemble_candidato(modelo.data)
        candidato_service.update(c)
    }

    void candidato_curtir(ModelData modelo) {
        Curtida c = assemble_model.assemble_curtida(modelo.data)
        candidato_service.curtir(c)
    }
}
