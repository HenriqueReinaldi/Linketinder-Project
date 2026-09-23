package org.linketinder.model

import org.linketinder.model.objetos.Vaga
import spock.lang.Shared
import spock.lang.Specification

class VagaSpec extends Specification{
    @Shared
    List<Integer> numeros = (-20..20)

    @Shared
    List<Vaga> vagas = numeros.collect {
        new Vaga(
            id: it.toInteger(),
            nome: "nome${it}",
            descricao: "descricao${it}",
            endereco: null,
            empresa: null,
            competencias_desejadas: null
        )
    }

    void "Metodo getNome retorna nome"() {
        expect:
            vaga.getNome() == "nome$numero"

        where:
            numero << numeros
            vaga << vagas
    }

    void "Metodo getDescricao retorna descricao"() {
        expect:
            vaga.getDescricao() == "descricao$numero"

        where:
            numero << numeros
            vaga << vagas
    }

    void "Outros getters retornam seus valores devidamente"() {
        expect:
            verifyAll(vaga) {
                getEndereco() == null
                getEmpresa() == null
                getCompetencias_desejadas() == null
                getId() == numero.toInteger()
            }

        where:
            numero << numeros
            vaga << vagas
    }
}
