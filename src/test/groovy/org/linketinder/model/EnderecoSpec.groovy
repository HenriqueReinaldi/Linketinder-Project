package org.linketinder.model

import org.linketinder.model.objetos.Endereco
import spock.lang.Shared
import spock.lang.Specification

class EnderecoSpec extends Specification{

    @Shared
    List<Integer> numeros = (-20..20)

    @Shared
    List<Endereco> enderecos = numeros.collect {
        new Endereco(
            id: it.toInteger(),
            CEP: "67${it}",
            pais: "pais${it}",
            estado: "estado${it}"
        )
    }

    def "Metodo getId retorna id"() {
        expect:
            endereco.getId() == numero.toInteger()

        where:
            numero << numeros
            endereco << enderecos
    }

    def "Metodo getCEP retorna CEP"() {
        expect:
            endereco.getCEP() == "67$numero"

        where:
            numero << numeros
            endereco << enderecos
    }

    def "Metodo getPas retorna pais"() {
        expect:
            endereco.getPais() == "pais$numero"

        where:
            numero << numeros
            endereco << enderecos
    }

    def "Metodo getEstado retorna estado"() {
        expect:
            endereco.getEstado() == "estado$numero"

        where:
            numero << numeros
            endereco << enderecos
    }
}
