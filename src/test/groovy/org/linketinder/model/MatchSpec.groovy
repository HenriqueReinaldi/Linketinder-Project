package org.linketinder.model

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Match
import org.linketinder.model.objetos.Recrutador
import org.linketinder.model.objetos.Vaga
import spock.lang.Shared
import spock.lang.Specification

class MatchSpec extends Specification {
    @Shared
    List<Integer> numeros = (-20..20)

    @Shared
    List<Match> matches = numeros.collect {
        new Match(
                new Candidato(id: it),
                new Vaga(id: it),
                new Recrutador(id: it),
                "${it}"
        )
    }

    void "Getters retornam seus valores devidamente"() {
        expect:
        verifyAll(match) {
            getCandidato().id == numero
            getVaga().id == numero
            getRecrutador().id == numero
            getData_match() == numero.toString()

        }

        where:
        numero << numeros
        match << matches
    }
}
