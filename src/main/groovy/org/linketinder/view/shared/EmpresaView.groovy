package org.linketinder.view.shared

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Empresa
import org.linketinder.view.View
import org.linketinder.view.ViewIO
import org.linketinder.view.traits.Cadastravel
import org.linketinder.view.traits.Representavel

@TupleConstructor
class EmpresaView implements Representavel<Empresa>, Cadastravel<Empresa> {
    ViewIO view

    @Override
    String representacao(Empresa objeto) {
        """Empresa ${objeto.nome}:
           |Descrição   : ${objeto.descricao}
           |Pais        : ${objeto.endereco.pais}
           |Email       : ${objeto.email}
           |Estado      : ${objeto.endereco.estado}
           |CEP         : ${objeto.endereco.CEP}
           |CNPJ        : ${objeto.CNPJ}
           |id          : ${objeto.id}
        """.stripMargin()
    }

    void exibir(Empresa objeto) {
        view.send_message(representacao objeto)
    }

    @Override
    Map<String, String> capturar_dados(boolean com_id) {
        Closure pergunta = { String pergunta ->
            view.get_input(pergunta)
        }

        Map<String, String> campos = [
                "nome"     : "Nome:",
                "email"    : "Email:",
                "estado"   : "Estado:",
                "CEP"      : "CEP:",
                "descricao": "Descrição:",
                "CNPJ"     : "CNPJ:",
                "pais"     : "pais:",
                "senha"    : "Senha:"
        ]

        if (com_id) campos["id"] = "ID:"

        campos.each { e ->
            campos[e.key] = pergunta(e.value)
        }

        return campos;
    }
}
