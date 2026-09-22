package org.linketinder.view.shared

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Competencia
import org.linketinder.view.View
import org.linketinder.view.ViewIO
import org.linketinder.view.traits.Cadastravel
import org.linketinder.view.traits.Representavel

@TupleConstructor
class CompetenciaView implements Representavel<Competencia>, Cadastravel {
    ViewIO view

    @Override
    String representacao(Competencia objeto) {
        "Competencia: ${objeto.tecnologia} - id: ${objeto.id}"
    }

    @Override
    void exibir(Competencia objeto) {
        view.send_message(representacao objeto)
    }

    @Override
    Map<String, String> capturar_dados(boolean com_id) {
        Closure pergunta = { String pergunta ->
            view.get_input(pergunta)
        }

        Map<String, String> campos = [
                "tecnologia": "Tecnologia:",
        ]

        if (com_id) campos["id"] = "ID:"

        campos.each { e ->
            campos[e.key] = pergunta(e.value)
        }

        return campos
    }
}
