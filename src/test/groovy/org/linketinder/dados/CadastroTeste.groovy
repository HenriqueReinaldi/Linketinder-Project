package org.linketinder.dados

import org.linketinder.classes.Candidato
import org.linketinder.classes.Empresa
import spock.lang.Shared
import spock.lang.Specification

class CadastroTeste extends Specification{
    @Shared
    List<Integer> numeros = (-20..20)

    @Shared
    List<Candidato> candidatos = numeros.collect {
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

    @Shared
    List<Empresa> empresas = numeros.collect { numero -> new Empresa
            (
                    "nome${numero}",
                    "email${numero}@mail",
                    "DF${numero}",
                    "0${numero}",
                    "uma boa empresa${numero}",
                    "0${numero}",
                    "BRASIL${numero}",
                    ["nenhuma${numero}"]
            )
    };

    def "Cadastrar candidato funciona" () {
        given:
            def dados = new Dados()
            def cadastro = new Cadastro(dados)
            def tamanho_previo = dados.get_qtd_candidatos()

        when: "um novo candidato é cadastrado"
            def status =cadastro.cadastrar_candidato(c)

        then: "funcao retorna verdadeiro (candidato cadastrado)"
            status

        and: "percebe-se que a quantidade de candidatos aumentou"
            dados.get_qtd_candidatos() == tamanho_previo+1

        and: "e que agora o novo candidato existe na lista"
            dados.getCandidatos().contains(c)

        where:
            c << candidatos
    }

    def "Cadastrar empresa funciona" () {
        given:
            def dados = new Dados()
            def cadastro = new Cadastro(dados)
            def tamanho_previo = dados.get_qtd_empresas()

        when: "uma nova empresa é cadastrada"
            def status = cadastro.cadastrar_empresa(e)

        then:  "funcao retorna verdadeiro (empresa cadastrado)"
            status

        and: "percebe-se que a quantidade de empresas aumentou"
            dados.get_qtd_empresas() == tamanho_previo+1

        and: "e que agora a nova empresa existe na lista"
            dados.getEmpresas().contains(e)

        where:
            e << empresas
    }
}
