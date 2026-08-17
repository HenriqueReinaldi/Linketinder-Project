package org.linketinder.classes

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class EmpresaTeste extends Specification{

    @Shared
    List<Integer> numeros = (-20..20)

    @Shared
    List<Empresa> empresas = numeros.collect {numero -> new Empresa
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

    @Unroll
    def "metodo getNOme retorna o nome corretamente #numero" (){
        expect:
            empresa.getNome() == "nome${numero}"

        where:
            numero << numeros
            empresa << empresas
    }

    def "metodo getEmail retorna o email corretamente" () {
        expect:
            empresa.getEmail() == "email${numero}@mail"

        where:
            numero << numeros
            empresa << empresas
    }

    def "outros getters retornam seus valores devidamente" () {


        expect:
            verifyAll {
                empresa.getEstado() == "DF${numero}"
                empresa.getCEP() == "0${numero}"
                empresa.getDescricao() == "uma boa empresa${numero}"
                empresa.getCNPJ() == "0${numero}"
                empresa.getPais() == "BRASIL${numero}"
                empresa.getCompetencias_desejadas() ==["nenhuma${numero}"]
            }

        where:
            numero << numeros
            empresa << empresas
    }
    
}
