package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Vaga
import org.linketinder.service.CandidatoService
import org.linketinder.service.CurtidaService
import org.linketinder.service.EmpresaService
import org.linketinder.service.VagaService

@TupleConstructor
class CurtidaController {
    CurtidaService curtida_service
    CandidatoService candidato_service
    VagaService vaga_service

    List<Curtida> get_lista_curtida() {
        return curtida_service.get_lista()
    }

    void candidato_curtir(ModelData modelo) {
        Curtida c = assemble_curtida(modelo.data)
        curtida_service.curtir_como_candidato(c)
    }

    void empresa_curtir(ModelData modelo) {
        Curtida c = assemble_curtida(modelo.data)
        curtida_service.curtir_como_empresa(c)
    }

    Curtida assemble_curtida(Map<String, String> curtida_info) {
        try {
            Candidato candidato = candidato_service.get_by_id(curtida_info["candidato_id"])
            Vaga vaga = vaga_service.get_by_id(curtida_info["vaga_id"])

            return new Curtida(
                    candidato: candidato,
                    vaga: vaga,
                    id: curtida_info.id ? curtida_info.id.toInteger() : -1
            )
        }
        catch (Exception ignored) {
            return null
        }
    }
}
