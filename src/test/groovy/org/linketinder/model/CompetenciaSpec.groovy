package org.linketinder.model

import org.linketinder.model.objetos.Competencia
import spock.lang.Shared
import spock.lang.Specification

class CompetenciaSpec extends Specification {
    @Shared
    List<Integer> numeros = (-20..20)

    @Shared
    List<Competencia> competencias = numeros.collect {
        new Competencia(
                id: it.toInteger(),
                tecnologia: "tecnologia!tecnologia!${it}"
        )
    }

    void "Metodo getId retorna id"() {
        expect:
        competencia.getId() == numero.toInteger()

        where:
        numero << numeros
        competencia << competencias
    }

    void "Metodo getTecnologia retorna tecnologia"() {
        expect:
        competencia.getTecnologia() == "tecnologia!tecnologia!$numero"

        where:
        numero << numeros
        competencia << competencias
    }
}
