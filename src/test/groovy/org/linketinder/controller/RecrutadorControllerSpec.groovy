package org.linketinder.controller

import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Recrutador
import org.linketinder.service.CurtidaService
import org.linketinder.service.EmpresaService
import org.linketinder.service.RecrutadorService
import org.linketinder.service.VagaService
import spock.lang.Specification

class RecrutadorControllerSpec extends Specification {
    RecrutadorService recrutador_service = Mock()
    EmpresaService empresa_service = Mock()
    VagaService vaga_service = Mock()

    void "get lista recrutador chama o service correto"() {
        given:
        RecrutadorController controller = new RecrutadorController(recrutador_service, empresa_service, vaga_service)

        when:
        controller.get_lista_recrutador()

        then:
        1 * recrutador_service.get_lista()
    }

    void "listar vagas do recrutador chama o service correto"() {
        given:
        RecrutadorController controller = new RecrutadorController(recrutador_service, empresa_service, vaga_service)

        when:
        controller.get_lista_vaga_recrutador(7)

        then:
        1 * vaga_service.get_lista_vaga_recrutador(7)
    }

    void "listar curtidas do recrutador chama o service correto"() {
        given:
        CurtidaService curtida_service = Mock()
        RecrutadorController controller = new RecrutadorController(recrutador_service, empresa_service, vaga_service, curtida_service)

        when:
        controller.get_lista_curtida_recrutador(9)

        then:
        1 * curtida_service.get_lista_curtida_recrutador(9)
    }

    void "cadastrar recrutador chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Recrutador recrutador = new Recrutador()
        RecrutadorController controller = Spy(constructorArgs: [recrutador_service, empresa_service, vaga_service])

        when:
        controller.cadastrar_recrutador(md)

        then:
        1 * controller.assemble_recrutador(md.data) >> recrutador
        1 * recrutador_service.cadastrar(recrutador)
    }

    void "responder curtida chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        RecrutadorController controller = Spy(constructorArgs: [recrutador_service, empresa_service, vaga_service])

        when:
        controller.get_lista_vaga_recrutador(7)

        then:
        1 * vaga_service.get_lista_vaga_recrutador(7)
    }

    void "assemble_recrutador monta recrutador corretamente"() {
        given:
        RecrutadorController controller = Spy(constructorArgs: [recrutador_service, empresa_service, vaga_service])
        Map<String, String> recrutador_info = [
                nome        : "nome",
                descricao   : "descricao",
                email       : "email@email.com",
                CPF         : "12345678909",
                nascimento  : "1990-01-01",
                senha       : "senha123",
                empresa_CNPJ: "empresa_CNPJ",
                nicho       : "nicho",
                id          : "3"
        ]
        Empresa empresa = new Empresa(CNPJ: "empresa_CNPJ")

        when:
        Recrutador recrutador = controller.assemble_recrutador(recrutador_info)

        then:
        1 * empresa_service.get_by_CNPJ("empresa_CNPJ") >> empresa

        recrutador.nome == "nome"
        recrutador.descricao == "descricao"
        recrutador.email == "email@email.com"
        recrutador.CPF == "12345678909"
        recrutador.data_nascimento == "1990-01-01"
        recrutador.senha == "senha123"
        recrutador.empresa == empresa
        recrutador.nicho == "nicho"
        recrutador.id == 3
    }
}
