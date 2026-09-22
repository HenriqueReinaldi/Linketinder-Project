package org.linketinder.view.shared

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Candidato
import org.linketinder.view.View
import org.linketinder.view.ViewIO
import org.linketinder.view.traits.Cadastravel
import org.linketinder.view.traits.Representavel

@TupleConstructor
class CandidatoView implements Representavel<Candidato>, Cadastravel<Candidato> {
    ViewIO view

    @Override
    String representacao(Candidato objeto) {
        String competencias = objeto.competencias
            .collect{it.tecnologia }
            .join(", ")
            ?: ""

        """Candidato ${objeto.nome} ${objeto.sobrenome}:
           |Descrição   : ${objeto.descricao}
           |Idade       : ${objeto.idade}
           |Email       : ${objeto.email}
           |Estado      : ${objeto.endereco.estado}
           |CEP         : ${objeto.endereco.CEP}
           |CPF         : ${objeto.CPF}
           |Competencias: ${competencias}
           |Nascimento  : ${objeto.data_nascimento}
           |id          : ${objeto.id}
        """.stripMargin()
    }

    @Override
    void exibir(Candidato objeto) {
        view.send_message(representacao objeto)
    }

    @Override
    Map<String, String> capturar_dados(boolean com_id) {
        Closure pergunta = { String pergunta ->
            view.get_input(pergunta)
        }

        Map<String, String> campos = [
            "nome" : "Nome:",
            "sobrenome": "Sobrenome:",
            "nascimento": "Data de nascimento:",
            "email" : "Email:",
            "estado" : "Estado:",
            "CEP" : "CEP:",
            "descricao" : "Descrição:",
            "CPF" : "CPF:",
            "competencias": "Competencias:",
            "senha": "Senha:"
        ]

        if (com_id) campos["id"] = "ID:"

        campos.each {e ->
            campos[e.key] = pergunta(e.value)
        }

        return campos;
    }
}