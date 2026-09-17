package org.linketinder.view.terminal

import org.linketinder.model.objetos.Competencia
import org.linketinder.view.traits.Cadastravel
import org.linketinder.view.traits.Representavel

class CompetenciaViewTerm implements Representavel<Competencia>, Cadastravel{
    @Override
    String representacao(Competencia objeto) {
        "Competencia: ${objeto.tecnologia} - id: ${objeto.id}"
    }

    @Override
    void exibir(Competencia objeto) {
        println representacao(objeto)
    }

    @Override
    Map<String, String> capturar_dados(boolean com_id) {
        Scanner scan = new Scanner(System.in);
        Closure pergunta = { String pergunta ->
            print pergunta
            scan.nextLine()
        }

        Map<String, String> campos = [
            "tecnologia" : "Tecnologia:",
        ]

        if (com_id) campos["id"] = "ID:"

        campos.each {e ->
            campos[e.key] = pergunta(e.value)
        }

        return campos;
    }
}
