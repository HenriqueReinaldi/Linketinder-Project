package org.linketinder.model

import org.linketinder.model.objetos.Empresa
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class EmpresaSpec extends Specification {

    @Shared
    List<Integer> numeros = (-20..20)

    @Shared
    List<Empresa> empresas = numeros.collect {
        new Empresa(
                id: it.toInteger(),
                CNPJ: "0${it}",
                nome: "nome${it}",
                email: "email${it}@mail",
                descricao: "uma boa empresa${it}",
                senha: "${it}",
                endereco: null
        )
    }

    void "Metodo getNome retorna nome"() {
        expect:
        empresa.getNome() == "nome$numero"

        where:
        numero << numeros
        empresa << empresas
    }

    void "Metodo getEmail retorna email"() {
        expect:
        empresa.getEmail() == "email$numero@mail"

        where:
        numero << numeros
        empresa << empresas
    }

    void "Outros getters retornam seus valores devidamente"() {
        expect:
        verifyAll(empresa) {
            getId() == numero.toInteger()
            getCNPJ() == "0$numero"
            getNome() == "nome$numero"
            getEmail() == "email$numero@mail"
            getDescricao() == "uma boa empresa$numero"
            getSenha() == "$numero"
            getEndereco() == null
        }

        where:
        numero << numeros
        empresa << empresas
    }

}
