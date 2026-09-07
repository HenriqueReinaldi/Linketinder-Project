package org.linketinder.view.terminal

import org.linketinder.model.objetos.Curtida
import org.linketinder.view.traits.Cadastravel
import org.linketinder.view.traits.Representavel

class CurtidaViewTerm implements Representavel<Curtida>, Cadastravel<Curtida> {
    @Override
    Map<String, String> capturar_dados() {
        Scanner scan = new Scanner(System.in);

        Closure pergunta = { String pergunta ->
            print pergunta
            scan.nextLine()
        }

        Map<String, String> campos = [
            "candidato_id" : "ID do Candidato:",
            "vaga_id" : "ID da da Vaga:"
        ]

        campos.each {e ->
            campos[e.key] = pergunta(e.value)
        }

        return campos;
    }

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
        println representacao(objeto)
    }
}
