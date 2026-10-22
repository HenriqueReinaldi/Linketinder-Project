package org.linketinder.view.shared

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Recrutador
import org.linketinder.view.ViewIO
import org.linketinder.view.traits.Cadastravel
import org.linketinder.view.traits.Representavel

@TupleConstructor
class RecrutadorView implements Representavel<Recrutador>, Cadastravel<Recrutador> {
    ViewIO view

    @Override
    String representacao(Recrutador objeto) {
        """Recrutador ${objeto.nome}:
           |Descrição   : ${objeto.descricao}
           |Email       : ${objeto.email}
           |CPF         : ${objeto.CPF}
           |Nascimento  : ${objeto.data_nascimento}
           |Nicho       : ${objeto.nicho}
           |Empresa     : ${objeto.empresa?.nome ?: 'N/A'}
           |id          : ${objeto.id}
        """.stripMargin()
    }

    @Override
    void exibir(Recrutador objeto) {
        view.send_message(representacao objeto)
    }

    @Override
    Map<String, String> capturar_dados(boolean com_id) {
        Closure pergunta = { String pergunta ->
            view.get_input(pergunta)
        }

        Map<String, String> campos = [
                "nome"        : "Nome:",
                "descricao"   : "Descrição:",
                "email"       : "Email:",
                "CPF"         : "CPF:",
                "nascimento"  : "Data de Nascimento:",
                "senha"       : "Senha:",
                "empresa_CNPJ": "CNPJ da empresa:",
                "nicho"       : "Nicho:",
        ]

        if (com_id) campos["id"] = "ID:"

        campos.each { e ->
            campos[e.key] = pergunta(e.value)
        }

        return campos
    }
}
