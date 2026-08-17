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
        expect:
            cad.cadastrar_candidato(cand)

        where:
            cad = new Cadastro()
            cand << candidatos

    }

    def "Cadastrar não candidato como candidato não é permitido" (){
        expect:
            ! cad.cadastrar_candidato(cand)

        where:
            cad = new Cadastro()
            cand << empresas
    }

    def "Cadastrar empresa funciona" () {
        expect:
            cad.cadastrar_empresa(emp)

        where:
            cad = new Cadastro()
            emp << empresas

    }

    def "Cadastrar não empresa como empresa não é permitido" (){
        expect:
        ! cad.cadastrar_empresa(cand)

        where:
            cad = new Cadastro()
            cand << candidatos
    }

}
