package org.linketinder.controller

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Vaga
import org.linketinder.service.CandidatoService
import org.linketinder.service.CurtidaService
import org.linketinder.service.EmpresaService
import org.linketinder.service.VagaService
import spock.lang.Specification

class CurtidaControllerSpec extends Specification {
    CurtidaService curtida_service = Mock()
    CandidatoService candidato_service = Mock()
    VagaService vaga_service = Mock()
    CurtidaController controller = new CurtidaController(curtida_service)

    void "get lista curtida chama o service correto"() {
        when:
        controller.get_lista_curtida()
        then:
        1 * curtida_service.get_lista()
    }

    void "candidato curtir chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Curtida curtida = new Curtida(id: 1)
        CurtidaController controller = Spy(constructorArgs: [curtida_service, candidato_service, vaga_service])

        when:
        controller.candidato_curtir(md)

        then:
        1 * controller.assemble_curtida(md.data) >> curtida
        1 * curtida_service.curtir_como_candidato(curtida)
    }

    void "empresa curtir chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Curtida curtida = new Curtida(id: 1)
        CurtidaController controller = Spy(constructorArgs: [curtida_service, candidato_service, vaga_service])

        when:
        controller.empresa_curtir(md)

        then:
        1 * controller.assemble_curtida(md.data) >> curtida
        1 * curtida_service.curtir_como_empresa(curtida)
    }

    void "assemble_curtida monta curtida corretamente"() {
        given:
        CurtidaController controller = Spy(constructorArgs: [curtida_service, candidato_service, vaga_service])
        Map<String, String> curtida_info = [
                "candidato_id": "12",
                "vaga_id"     : "13",
                "id"          : "1"
        ]
        Candidato candidato = new Candidato(id: 12)
        Vaga vaga = new Vaga(id: 13)

        when:
        Curtida curtida = controller.assemble_curtida(curtida_info)

        then:
        1 * candidato_service.get_by_id("12") >> candidato
        1 * vaga_service.get_by_id("13") >> vaga

        curtida.id == 1
        curtida.candidato == candidato
        curtida.vaga == vaga
    }
}
