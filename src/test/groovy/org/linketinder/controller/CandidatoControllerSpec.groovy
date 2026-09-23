package org.linketinder.controller

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Curtida
import org.linketinder.service.CandidatoService
import spock.lang.Specification

class CandidatoControllerSpec extends Specification {
    CandidatoService candidato_service = Mock()
    CandidatoController controller = new CandidatoController(candidato_service)


    void "get lista candidato chama o service correto"() {
        when:
        controller.get_lista_candidato()
        then:
        1 * candidato_service.get_lista()
    }

    void "cadastrar candidato chama o service correto e assemblemodel"() {
        given:
        CandidatoController controller = Spy(constructorArgs: [candidato_service])
        ModelData md = new ModelData()
        Candidato candidato = new Candidato(id: 1)

        when:
        controller.cadastrar_candidato(md)

        then:
        1 * controller.assemble_candidato(md.data) >> candidato
        1 * candidato_service.cadastrar(candidato)
    }

    void "deletar candidato chama o service correto"() {
        when:
        controller.deletar_candidato(13)
        then:
        1 * candidato_service.deletar(13)
    }

    void "update candidato chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Candidato candidato = new Candidato(id: 1)
        CandidatoController controller = Spy(constructorArgs: [candidato_service])

        when:
        controller.update_candidato(md)

        then:
        1 * controller.assemble_candidato(md.data) >> candidato
        1 * candidato_service.update(candidato)
    }

    void "assemble candidato monta candidato corretamente"() {
        given:
        CandidatoController controller = Spy(constructorArgs: [candidato_service])
        Map<String, String> candidato_info = [
                "competencias": "competencia",
                "CEP"         : "CEP",
                "estado"      : "estado",
                "CPF"         : "CPF",
                "nome"        : "nome",
                "sobrenome"   : "sobrenome",
                "nascimento"  : "nascimento",
                "email"       : "email",
                "descricao"   : "descricao",
                "senha"       : "senha",
                "id"          : "1"
        ]

        when:
        Candidato cand = controller.assemble_candidato(candidato_info)

        then:
        cand.nome == "nome"
        cand.sobrenome == "sobrenome"
        cand.CPF == "CPF"
        cand.data_nascimento == "nascimento"
        cand.email == "email"
        cand.descricao == "descricao"
        cand.senha == "senha"
        cand.id == 1

        cand.endereco.CEP == "CEP"
        cand.endereco.estado == "estado"

        cand.competencias.size() == 1
        cand.competencias[0].tecnologia == "competencia"
    }
}
