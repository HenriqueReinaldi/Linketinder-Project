package org.linketinder.classes

import spock.lang.Shared
import spock.lang.Specification

class CandidatoTeste extends Specification{

    @Shared
    List<Integer> numeros = (-20..20)

    @Shared
    List<Candidato> Candidatos = numeros.collect {
        it ->  new Candidato
                (
                        "nome${it}",
                        "email${it}@mail",
                        "DF${it}",
                        "0${it}",
                        "um bom candidato${it}",
                        "${it}",
                        it.toInteger(),
                        ["nenhuma${it}"],
                );
    }

    def "Metodo getNome retorna nome" (){
        expect:
            cand.getNome() == "nome$numero"

        where:
            numero << numeros
            cand << Candidatos
    }

    def "Metodo getEmail retorna email" (){
        expect:
            cand.getEmail() == "email$numero@mail"

        where:
            numero << numeros
            cand << Candidatos
    }

    // creio não ser ideal, mas foi feito com propósito de experimentar com a ferramenta...
    def "Outros getters retornam o valor correto" () {
        expect:
        verifyAll {
            cand.getEstado() == "DF${numero}"
            cand.getCPF() == "${numero}"
            cand.getCompetencias() == ["nenhuma${numero}"]
            cand.getCEP() == "0${numero}"
            cand.getDescricao() == "um bom candidato${numero}"
            cand.getIdade() == numero.toInteger()
        }


        where:
            numero << numeros
            cand << Candidatos
    }
}
