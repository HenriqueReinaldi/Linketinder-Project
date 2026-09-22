package org.linketinder.controller

import org.linketinder.model.objetos.Vaga
import org.linketinder.service.CurtidaService
import org.linketinder.service.VagaService
import spock.lang.Specification

class VagaControllerSpec extends Specification {
    AssembleModel assemble_model = Mock()
    VagaService vaga_service = Mock()
    VagaController controller = new VagaController(assemble_model, vaga_service)


    void "get lista vaga  chama o service correto"() {
        when:
        controller.get_lista_vaga()
        then:
        1 * vaga_service.get_lista()
    }

    void "cadastrar vaga chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Vaga vaga = new Vaga(id: 1)

        when:
        controller.cadastrar_vaga(md)

        then:
        1 * assemble_model.assemble_vaga(md.data) >> vaga
        1 * vaga_service.cadastrar(vaga)
    }

    void "deletar vaga chama o service correto"() {
        when:
        controller.deletar_vaga(15)
        then:
        1 * vaga_service.deletar(15)
    }

    void "update vaga chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Vaga vaga = new Vaga(id: 1)

        when:
        controller.update_vaga(md)

        then:
        1 * assemble_model.assemble_vaga(md.data) >> vaga
        1 * vaga_service.update(vaga)
    }
}
