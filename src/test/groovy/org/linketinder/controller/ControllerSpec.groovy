package org.linketinder.controller

import org.linketinder.model.objetos.Candidato
import org.linketinder.service.Service
import org.linketinder.view.View
import spock.lang.Shared
import spock.lang.Specification

class ControllerSpec extends Specification{
    @Shared
    List<Integer> numeros = (-20..20)

    @Shared
    List<Candidato> candidatos = numeros.collect {
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

    View view = Mock(View)
    Service service = Mock(Service)
    AssembleModel assembleModel = Mock(AssembleModel)



    def "run() com input 'listar candidatos'"() {
        given:
            view.get_input("@>") >> "listar candidatos"
            view.candidato_view >> candidatoView
            service.get_lista_generico("candidato") >> candidatos

        when:
            boolean continuation = controller.run()

        then:
            candidatos.size() * candidatoView.exibir(_ as Candidato)
            continuation
    }
}