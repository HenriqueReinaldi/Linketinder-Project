package org.linketinder.view.shared

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Match
import org.linketinder.model.objetos.Recrutador
import org.linketinder.model.objetos.Vaga
import org.linketinder.view.ViewIO
import spock.lang.Shared
import spock.lang.Specification

class MatchViewSpec extends Specification {
    ViewIO view = Mock()

    @Shared
    Match match = new Match(
        new Candidato(id: 1, nome: "abc"),
        new Vaga(id: 1, nome: "def"),
        new Recrutador(id: 1, nome: "ghi"),
        "2002-02-02"
    )

    void "Representacao funciona corretamente"() {
        given:
        MatchView match_view = new MatchView(view)

        when:
        String resposta = match_view.representacao(match)

        then:
        verifyAll {
            resposta.contains("abc")
            resposta.contains("def")
            resposta.contains("ghi")
            resposta.contains("2002-02-02")
        }
    }

    void "Exibir chama representacao e view.send_message"() {
        given:
        MatchView match_view = Spy(MatchView, constructorArgs: [view])

        when:
        match_view.exibir(match)

        then:
        1 * match_view.representacao(match)
        1 * view.send_message(_)
    }
}
