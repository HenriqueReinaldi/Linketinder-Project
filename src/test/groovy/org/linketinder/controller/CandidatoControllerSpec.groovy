package org.linketinder.controller

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Curtida
import org.linketinder.service.CandidatoService
import spock.lang.Specification

class CandidatoControllerSpec extends Specification{
    AssembleModel assemble_model = Mock()
    CandidatoService candidato_service = Mock()
    CandidatoController controller = new CandidatoController(assemble_model, candidato_service)


    void "get lista candidato chama o service correto"() {
        when:
        controller.get_lista_candidato()
        then:
        1 * candidato_service.get_lista()
    }

    void "cadastrar candidato chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Candidato candidato = new Candidato(id: 1)

        when:
        controller.cadastrar_candidato(md)

        then:
        1 * assemble_model.assemble_candidato(md.data) >> candidato
        1 * candidato_service.cadastrar(candidato)
    }

    void "deletar candidato chama o service correto"(){
        when:
        controller.deletar_candidato(13)
        then:
        1 * candidato_service.deletar(13)
    }

    void "update candidato chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Candidato candidato = new Candidato(id: 1)

        when:
        controller.update_candidato(md)

        then:
        1 * assemble_model.assemble_candidato(md.data) >> candidato
        1 * candidato_service.update(candidato)
    }

    void "candidato curtir chama o service correto e assemblemodel"(){
        given:
        ModelData md = new ModelData()
        Curtida curtida = new Curtida(id: 1)

        when:
        controller.candidato_curtir(md)

        then:
        1 * assemble_model.assemble_curtida(md.data) >> curtida
        1 * candidato_service.curtir(curtida)
    }
}
