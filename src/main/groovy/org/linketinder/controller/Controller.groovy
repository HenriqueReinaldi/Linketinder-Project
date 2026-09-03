package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.service.Service
import org.linketinder.view.terminal.TermView

@TupleConstructor
class Controller {
    TermView tv
    Service service

    boolean cadastrar_candidato(){
        Map<String, String> ci = tv.candidato_view.capturar_dados()
        return service.cadastrar_candidato(ci)
    }
    boolean cadastrar_empresa(){
        Map<String, String> ei = tv.empresa_view.capturar_dados()
        return service.cadastrar_empresa(ei)
    }
    boolean cadastrar_vaga(){
        Map<String, String> vi = tv.vaga_view.capturar_dados()
        return service.cadastrar_vaga(vi)
    }
    
    int receber_input(String input){
        switch (input){
            case "?":
                tv.send_message "Comandos read:"
                tv.send_message "listar <candidatos / empresas / vagas / candidatos>\n"

                tv.send_message "Comandos create:"
                tv.send_message "cadastrar <candidato / empresa>\n"

                tv.send_message "Outros:"
                tv.send_message "sair                 | fecha o programa"
                break

            case "listar candidatos":
                service.bd.read.get_lista_candidatos().each {
                    String rep = tv.candidato_view.representacao(it)
                    tv.send_message rep
                }
                break

            case "listar empresas":
                service.bd.read.get_lista_empresas().each {
                    String rep = tv.empresa_view.representacao(it)
                    tv.send_message rep
                }
                break

            case "listar vagas":
                service.bd.read.get_lista_vagas().each {
                    String rep = tv.vaga_view.representacao(it)
                    tv.send_message rep
                }
                break

            case "listar competencias":
                service.bd.read.get_lista_competencias().each {
                    String rep = tv.competencia_view.representacao(it)
                    tv.send_message rep
                }
                break

            case "cadastrar candidato":
                cadastrar_candidato()
                break

            case "cadastrar empresa":
                cadastrar_empresa()
                break

            case "cadastrar vaga":
                cadastrar_vaga()
                break

            case "sair":
                return 1
        }
        0
    }

    void init() {
        while (true){
            String res = tv.get_input "@>"
            if (receber_input(res)) break
        }
    }
}
