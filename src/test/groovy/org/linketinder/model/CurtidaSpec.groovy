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
                id: it.toInteger(),
                candidato: null,
                vaga: null,
                empresa_curtiu: (it % 2 == 0)
        )
    }

    void "Metodo getId retorna id"() {
        expect:
        curtida.getId() == numero.toInteger()

        where:
        numero << numeros
        curtida << curtidas
    }

    void "Outros getters retornam seus valores devidamente"() {
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
