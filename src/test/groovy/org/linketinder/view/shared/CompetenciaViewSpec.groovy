package org.linketinder.view.shared

import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Endereco
import org.linketinder.view.View
import org.linketinder.view.ViewIO
import spock.lang.Shared
import spock.lang.Specification

class CompetenciaViewSpec extends Specification {
    ViewIO view = Mock()

    @Shared
    Competencia competencia = new Competencia(
            id: 1,
            tecnologia: "nome",
    )

    void "capturar_dados usa view.get_input para coletar infos"() {
        given:
        CompetenciaView competencia_view = new CompetenciaView(view)

        when:
        Map<String, String> info = competencia_view.capturar_dados()

        then:
        1 * view.get_input(_ as String) >> "input"

        and:
        info.every { it.value == "input" }
    }

    void "Extensão: capturar_dados com ID usa view.get_input para coletar infos"() {
        given:
        CompetenciaView competencia_view = new CompetenciaView(view)

        when:
        Map<String, String> info = competencia_view.capturar_dados(true)

        then:
        2 * view.get_input(_ as String) >> "input"

        and:
        info.every { it.value == "input" }
    }

    void "Representacao funciona corretamente"() {
        given:
        CompetenciaView competencia_view = new CompetenciaView(view)

        when:
        String resposta = competencia_view.representacao(competencia)

        then:
        verifyAll {
            resposta.contains("nome")
            resposta.contains("1")
        }
    }

    void "Exibir chama representacao e view.send_message"() {
        given:
        CompetenciaView competencia_view = Spy(CompetenciaView, constructorArgs: [view])

        when:
        competencia_view.exibir(competencia)

        then:
        1 * competencia_view.representacao(competencia)
        1 * view.send_message(_)

    }
}
