package org.linketinder.view.shared

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Curtida
import org.linketinder.view.View
import org.linketinder.view.ViewIO
import org.linketinder.view.traits.Cadastravel
import org.linketinder.view.traits.Representavel

@TupleConstructor
class CurtidaView implements Representavel<Curtida>, Cadastravel<Curtida> {
    ViewIO view

    @Override
    String representacao(Curtida objeto) {
        String match_string = objeto.empresa_curtiu ? "MATCH!!!" : "Sem match ainda..."

        """ 
           |${match_string}
           |Candidato       : ${objeto.candidato.nome} ..ID:${objeto.candidato.id}
           |Vaga            : ${objeto.vaga.nome} ..ID:${objeto.vaga.id}
           |empresa_curtiu  : ${objeto.empresa_curtiu}
        """.stripMargin()

    }

    @Override
    void exibir(Curtida objeto) {
        view.send_message(representacao objeto)
    }

    @Override
    Map<String, String> capturar_dados(boolean com_id) {
        Closure pergunta = { String pergunta ->
            view.get_input(pergunta)
        }

        Map<String, String> campos = [
                "candidato_id" : "ID do Candidato:",
                "vaga_id" : "ID da da Vaga:"
        ]

        if (com_id) campos["id"] = "ID:"

        campos.each {e ->
            campos[e.key] = pergunta(e.value)
        }

        return campos;
    }
}
