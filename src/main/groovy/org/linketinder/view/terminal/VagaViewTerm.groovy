package org.linketinder.view.terminal

import org.linketinder.model.objetos.Vaga
import org.linketinder.view.traits.Cadastravel
import org.linketinder.view.traits.Representavel

class VagaViewTerm implements Representavel<Vaga>, Cadastravel<Vaga>{

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
            "descricao" : "Descrição:",
            "CEP" : "CEP:",
            "pais": "pais:",
            "estado" : "Estado:",
            "competencias_desejadas": "Competencias:",
            "empresa_CNPJ" : "CNPJ da empresa:"
        ]

        campos.each {e ->
            campos[e.key] = pergunta(e.value)
        }

        return campos;
    }
}
