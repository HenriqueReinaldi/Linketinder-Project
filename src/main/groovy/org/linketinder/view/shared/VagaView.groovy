package org.linketinder.view.shared

import groovy.transform.TupleConstructor
import org.codehaus.groovy.ast.expr.TupleExpression
import org.linketinder.model.objetos.Vaga
import org.linketinder.view.View
import org.linketinder.view.traits.Cadastravel
import org.linketinder.view.traits.Representavel

@TupleConstructor
class VagaView implements Representavel<Vaga>, Cadastravel<Vaga>{
    View view

    @Override
    String representacao(Vaga objeto) {
        String competencias = objeto.competencias_desejadas
            .collect{ it.tecnologia }
            .join(", ")
            ?: ""

        """Vaga ${objeto.nome}:
           |Descrição   : ${objeto.descricao}
           |Pais        : ${objeto.endereco.pais}
           |CEP         : ${objeto.endereco.CEP}
           |Estado      : ${objeto.endereco.estado}
           |Empresa     : ${objeto.empresa.nome}
           |Competencias: ${competencias}
           |id          : ${objeto.id}
        """.stripMargin()
    }

    @Override
    void exibir(Vaga objeto) {
        view.send_message(representacao objeto)
    }

    @Override
    Map<String, String> capturar_dados(boolean com_id) {
        Closure pergunta = { String pergunta ->
            view.get_input(pergunta)
        }

        Map<String, String> campos = [
            "nome" : "Nome:",
            "descricao" : "Descrição:",
            "CEP" : "CEP:",
            "pais": "pais:",
            "estado" : "Estado:",
            "competencias_desejadas": "Competencias:",
            "empresa_CNPJ" : "CNPJ da empresa:"
        ]

        if (com_id) campos["id"] = "ID:"

        campos.each {e ->
            campos[e.key] = pergunta(e.value)
        }

        return campos;
    }
}
