package org.linketinder.view.terminal

import org.linketinder.model.objetos.Candidato
import org.linketinder.view.traits.Cadastravel
import org.linketinder.view.traits.Representavel

class CandidatoViewTerm implements Representavel<Candidato>, Cadastravel<Candidato> {

    @Override
    String representacao(Candidato objeto) {
        String competencias = objeto.competencias
            .collect{it.tecnologia }
            .join(", ")
            ?: ""

        """Candidato ${objeto.nome} ${objeto.sobrenome}:
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

    void exibir(Candidato objeto) {
        println representacao(objeto);
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
            "sobrenome": "Sobrenome:",
            "nascimento": "Data de nascimento:",
            "email" : "Email:",
            "estado" : "Estado:",
            "CEP" : "CEP:",
            "descricao" : "Descrição:",
            "CPF" : "CPF:",
            "idade": "idade:",
            "competencias": "Competencias:",
            "senha": "Senha:"
        ]

        campos.each {e ->
            campos[e.key] = pergunta(e.value)
        }

        return campos;
    }
}