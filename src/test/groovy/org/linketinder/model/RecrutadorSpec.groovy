package org.linketinder.model

import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Recrutador
import spock.lang.Shared
import spock.lang.Specification

class RecrutadorSpec extends Specification {
    @Shared
    List<Integer> numeros = (-20..20)

    @Shared
    List<Recrutador> recrutadores = numeros.collect {
        new Recrutador(
                id: it.toInteger(),
                nome: "nome${it}",
                email: "email${it}@mail",
                descricao: "um bom recrutador${it}",
                senha: "${it}",
                CPF: "0${it}",
                data_nascimento: "1990-01-${it}",
                nicho: "nicho${it}",
                empresa: new Empresa(id: it.toInteger(), CNPJ: "empresa${it}", nome: "empresa nome${it}", email: "emp${it}@mail", descricao: "descr${it}", senha: "senha${it}", endereco: null),
                endereco: null
        )
    }

    void "Metodo getNome retorna nome"() {
        expect:
        recrutador.getNome() == "nome$numero"

        where:
        numero << numeros
        recrutador << recrutadores
    }

    void "Outros getters retornam seus valores devidamente"() {
        expect:
        verifyAll(recrutador) {
            getId() == numero.toInteger()
            getNome() == "nome$numero"
            getEmail() == "email$numero@mail"
            getDescricao() == "um bom recrutador$numero"
            getSenha() == "$numero"
            getCPF() == "0$numero"
            getData_nascimento() == "1990-01-$numero"
            getNicho() == "nicho$numero"
            getEmpresa() != null
            getEndereco() == null
        }

        where:
        numero << numeros
        recrutador << recrutadores
    }
}
