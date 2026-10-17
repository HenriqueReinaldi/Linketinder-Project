package org.linketinder.model

import org.linketinder.model.objetos.Curtida
import spock.lang.Shared
import spock.lang.Specification

class CurtidaSpec extends Specification {
    @Shared
    List<Integer> numeros = (-20..20)

    @Shared
    List<Curtida> curtidas = numeros.collect {
        new Curtida(
                candidato: null,
                vaga: null,
                empresa_curtiu: (it % 2 == 0)
        )
    }

    void "Getters retornam seus valores devidamente"() {
        expect:
        verifyAll(curtida) {
            getCandidato() == null
            getVaga() == null
            getEmpresa_curtiu() == (numero % 2 == 0)
        }

        where:
        numero << numeros
        curtida << curtidas
    }
}
