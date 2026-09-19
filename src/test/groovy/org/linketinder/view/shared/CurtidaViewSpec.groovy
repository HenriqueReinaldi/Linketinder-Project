package org.linketinder.view.shared

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga
import org.linketinder.view.View
import spock.lang.Shared
import spock.lang.Specification

class CurtidaViewSpec extends Specification{
    View view = Mock()

    @Shared
    Candidato candidato = new Candidato(
        id: 1,
        nome: "candidato_nome",
        sobrenome: "sobrenome",
        descricao: "descricao",
        idade: 1,
        email: "email",
        endereco: new Endereco(
                estado: "estado",
                CEP: "CEP"
        ),
        CPF: "CPF",
        data_nascimento: "data_nascimento",
        competencias: []
    )
    @Shared
    Empresa empresa = new Empresa(
        id: 1,
        nome: "nome",
        descricao: "descricao",
        email: "email",
        endereco: new Endereco(
                pais: "pais",
                estado: "estado",
                CEP: "CEP"
        ),
        CNPJ: "CNPJ"
    )
    @Shared
    Vaga vaga = new Vaga(
        id: 1,
        nome: "vaga_nome",
        descricao: "descricao",
        endereco: new Endereco(
                estado: "estado",
                CEP: "CEP"
        ),
        empresa: empresa,
        competencias_desejadas: []
    )
    @Shared
    Curtida Curtida = new Curtida(
        id: 1,
        candidato: candidato,
        vaga: vaga,
        empresa_curtiu: false
    )

    def "capturar_dados usa view.get_input para coletar infos"() {
        given:
        CurtidaView curtida_view = new CurtidaView(view)

        when:
        Map<String, String> info = curtida_view.capturar_dados()

        then:
        2 * view.get_input(_ as String) >> "input"

        and:
        info.every { it.value == "input"}
    }

    def "Extensão: capturar_dados com ID usa view.get_input para coletar infos"() {
        given:
        CurtidaView curtida_view = new CurtidaView(view)

        when:
        Map<String, String> info = curtida_view.capturar_dados(true)

        then:
        3 * view.get_input(_ as String) >> "input"

        and:
        info.every { it.value == "input"}
    }

    def "Representacao funciona corretamente"(){
        given:
        CurtidaView curtida_view = new CurtidaView(view)

        when:
        String resposta = curtida_view.representacao(Curtida)

        then:
        verifyAll {
            resposta.contains("candidato_nome")
            resposta.contains("vaga_nome")
            resposta.contains("false")
            resposta.contains("1")
        }
    }

    def "Exibir chama representacao e view.send_message"(){
        given:
        CurtidaView curtida_view = Spy(CurtidaView, constructorArgs: [view])

        when:
        curtida_view.exibir(Curtida)

        then:
        1 * curtida_view.representacao(Curtida)
        1 * view.send_message(_)
    }
}
