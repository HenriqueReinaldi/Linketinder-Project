package org.linketinder.terminal

import org.linketinder.dados.Cadastro
import org.linketinder.dados.Dados
import spock.lang.Shared
import spock.lang.Specification

class TerminalTeste extends Specification{
    @Shared
    List<Integer> numeros = (-20..20)

    @Shared
    List<String> inputs_candidatos = numeros.collect(it -> new String(
        "nome$it\n" +
        "email@mail.com$it\n" +
        "estado\n" +
        "CEP\n" +
        "DESCRICAO\n" +
        "CPF\n" +
        "20\n" +
        "competencia1 competencia$it\n"
    ));

    @Shared
    List<String> inputs_empresas = numeros.collect(it -> new String(
        "nome$it\n" +
        "email@mail.com$it\n" +
        "estado\n" +
        "CEP\n" +
        "DESCRICAO\n" +
        "CNPJ\n" +
        "BRASIL\n" +
        "competencia1 competencia$it\n"
    ))


    def "testar input: cadastro de candidato"(){
        given: "Um novo terminal"
            Dados d = new Dados()
            Cadastro c = new Cadastro(d)
            Terminal terminal = new Terminal(d, c)

        and: "Um input"
            System.setIn(new ByteArrayInputStream(input.bytes))

        when: "cadastramos o candidato"
            def status = terminal.cadastrar_candidato()

        then: "funcao retorna o candidato cadastrado"
            status != null

        and: "observamos o candidato nos dados"
            d.candidatos.contains(status)

        where:
            input << inputs_candidatos
            numero << numeros
    }

    def "testar input: cadastro de empresa"(){
        given: "Um novo terminal"
            Dados d = new Dados()
            Cadastro c = new Cadastro(d)
            Terminal terminal = new Terminal(d, c)

        and: "Um input"
            System.setIn(new ByteArrayInputStream(input.bytes))

        when: "cadastramos a empresa"
            def status = terminal.cadastrar_empresa()

        then: "funcao retorna a empresa cadastrada"
            status != null

        and: "observamos a empresa nos dados"
            d.empresas.contains(status)

        where:
            input << inputs_empresas
            numero << numeros
    }

}
