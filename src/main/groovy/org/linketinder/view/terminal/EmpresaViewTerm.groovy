package org.linketinder.view.terminal

import org.linketinder.model.objetos.Empresa
import org.linketinder.view.traits.Cadastravel
import org.linketinder.view.traits.Representavel

class EmpresaViewTerm implements Representavel<Empresa>, Cadastravel<Empresa> {

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

    void exibir(Empresa objeto){
        println representacao(objeto)
    }

    @Override
    Map<String, String> capturar_dados() {
        Scanner scan = new Scanner(System.in);

        Closure pergunta = { String pergunta ->
            print pergunta
            scan.nextLine()
        }

        Map<String, String> campos = [
            "nome" : "Nome:",
            "email" : "Email:",
            "estado" : "Estado:",
            "CEP" : "CEP:",
            "descricao" : "Descrição:",
            "CNPJ" : "CNPJ:",
            "pais": "pais:",
            "senha": "Senha:"
        ]

        campos.each {e ->
            campos[e.key] = pergunta(e.value)
        }

        return campos;
    }
}
