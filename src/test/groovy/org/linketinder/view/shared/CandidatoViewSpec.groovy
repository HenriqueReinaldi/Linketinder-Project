package org.linketinder.view.shared

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Endereco
import org.linketinder.view.View
import spock.lang.Shared
import spock.lang.Specification


class CandidatoViewSpec extends Specification{
    View view = Mock()

    @Shared
    Candidato candidato = new Candidato(
            id: 1,
            nome: "nome",
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

    def "capturar_dados usa view.get_input para coletar infos"() {
        given:
            CandidatoView candidato_view = new CandidatoView(view)

        when:
            Map<String, String> info = candidato_view.capturar_dados()

        then:
            10 * view.get_input(_ as String) >> "input"

        and:
            info.every { it.value == "input"}
    }

    def "Extensão: capturar_dados com ID usa view.get_input para coletar infos"() {
        given:
            CandidatoView candidato_view = new CandidatoView(view)

        when:
            Map<String, String> info = candidato_view.capturar_dados(true)

        then:
            11 * view.get_input(_ as String) >> "input"

        and:
            info.every { it.value == "input"}
    }

    def "Representacao funciona corretamente"(){
        given:
            CandidatoView candidato_view = new CandidatoView(view)

        when:
            String resposta = candidato_view.representacao(candidato)

        then:
            verifyAll {
                resposta.contains("nome")
                resposta.contains("sobrenome")
                resposta.contains("descricao")
                resposta.contains("email")
                resposta.contains("estado")
                resposta.contains("CEP")
                resposta.contains("CPF")
                resposta.contains("data_nascimento")
                resposta.contains("1")
            }
    }

    def "Exibir chama representacao e view.send_message"(){
        given:
            CandidatoView candidato_view = Spy(CandidatoView, constructorArgs: [view])

        when:
            candidato_view.exibir(candidato)

        then:
            1 * candidato_view.representacao(candidato)
            1 * view.send_message(_)

    }
}
