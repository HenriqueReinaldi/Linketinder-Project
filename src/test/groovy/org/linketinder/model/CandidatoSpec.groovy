package org.linketinder.model

import org.linketinder.model.objetos.Candidato
import spock.lang.Shared
import spock.lang.Specification

class CandidatoSpec extends Specification{

    @Shared
    List<Integer> numeros = (-20..20)

    @Shared
    List<Candidato> Candidatos = numeros.collect {
        new Candidato(
            id: it.toInteger(),
            CPF: "0${it}",
            idade: 25,
            competencias: null,
            nome: "nome${it}",
            sobrenome: "sobrenome${it}",
            data_nascimento: "2000-01-${it}",
            email: "email${it}@mail",
            descricao: "um bom candidato${it}",
            senha: "${it}",
            endereco: null
        )
    }

    def "Metodo getNome retorna nome"(){
        expect:
            cand.getNome() == "nome$numero"

        where:
            numero << numeros
            cand << Candidatos
    }

    def "Metodo getEmail retorna email"(){
        expect:
            cand.getEmail() == "email$numero@mail"

        where:
            numero << numeros
            cand << Candidatos
    }

    def "Outros getters retornam seus valores devidamente"() {
        expect:
            verifyAll(cand) {
                getId() == numero.toInteger()
                getCPF() == "0$numero"
                getIdade() == 25
                getCompetencias() == null
                getNome() == "nome$numero"
                getSobrenome() == "sobrenome$numero"
                getData_nascimento() == "2000-01-$numero"
                getEmail() == "email$numero@mail"
                getDescricao() == "um bom candidato$numero"
                getSenha() == "$numero"
                getEndereco() == null
            }

        where:
            numero << numeros
            cand << Candidatos
    }
}
