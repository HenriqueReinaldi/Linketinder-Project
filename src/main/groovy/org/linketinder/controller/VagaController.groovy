package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Vaga
import org.linketinder.service.VagaService

@TupleConstructor
class VagaController {
    AssembleModel assemble_model

    VagaService vaga_service

    List<Vaga> get_lista_vaga() {
        return vaga_service.get_lista()
    }

    void cadastrar_vaga(ModelData modelo) {
        Vaga v = assemble_model.assemble_vaga(modelo.data)
        vaga_service.cadastrar(v)
    }

    void deletar_vaga(int id) {
        vaga_service.deletar(id)
    }

    void update_vaga(ModelData modelo) {
        Vaga v = assemble_model.assemble_vaga(modelo.data)
        vaga_service.update(v)
    }
}
