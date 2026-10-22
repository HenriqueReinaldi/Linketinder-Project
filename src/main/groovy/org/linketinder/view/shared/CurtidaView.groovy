package org.linketinder.view.shared

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Curtida
import org.linketinder.view.ViewIO
import org.linketinder.view.traits.Cadastravel
import org.linketinder.view.traits.Representavel
import org.linketinder.view.traits.SecretamenteRepresentavel

@TupleConstructor
class CurtidaView implements Representavel<Curtida>, Cadastravel<Curtida>, SecretamenteRepresentavel<Curtida> {
    ViewIO view

    @Override
    String representacao(Curtida objeto) {
        String match_string = objeto.empresa_curtiu ? "MATCH!!!" : "Sem match ainda..."
        String candidato_nome = objeto.candidato?.nome ?: (objeto.candidato?.id ? "ID:${objeto.candidato.id}" : "N/A")
        String vaga_nome = objeto.vaga?.nome ?: (objeto.vaga?.id ? "ID:${objeto.vaga.id}" : "N/A")

        """ 
           |${match_string}
           |Candidato       : ${candidato_nome}
           |Vaga            : ${vaga_nome}
           |empresa_curtiu  : ${objeto.empresa_curtiu}
        """.stripMargin()

    }

    @Override
    String representacao_secreta(Curtida objeto) {
        String match_string = objeto.empresa_curtiu ? "MATCH!!!" : "Sem match ainda..."
        String candidato_desc = objeto.candidato?.descricao ?: (objeto.candidato?.id ? "Descrição do candidato ${objeto.candidato.id}" : "N/A")
        String vaga_nome = objeto.vaga?.nome ?: (objeto.vaga?.id ? "ID:${objeto.vaga.id}" : "N/A")

        """
           |${match_string}
           |Candidato       : ${candidato_desc}
           |Vaga            : ${vaga_nome}
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
                "candidato_id": "ID do Candidato:",
                "vaga_id"     : "ID da da Vaga:"
        ]

        if (com_id) campos["id"] = "ID:"

        campos.each { e ->
            campos[e.key] = pergunta(e.value)
        }

        return campos;
    }
}
