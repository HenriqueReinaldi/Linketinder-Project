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
        Map<String, String> ci = tv.candidatoView.capturar_dados()
        return service.cadastrar_candidato(ci)
    }
    boolean cadastrar_empresa(){
        Map<String, String> ei = tv.empresaView.capturar_dados()
        return service.cadastrar_empresa(ei)
    }
    
    int receber_input(String input){
        switch (input){
            case "?":
                tv.send_message "listar candidatos    | mostra todos os candidatos"
                tv.send_message "listar empresas      | mostra todos as empresas"
                tv.send_message "cadastrar candidato  | "
                tv.send_message "cadastrar empresa    | "
                tv.send_message "sair                 | fecha o programa"
                break

            case ".":
                service.bd.read.get_lista_candidatos().each {
                    String rep = tv.candidatoView.representacao(it)
                    tv.send_message rep
                }
                break

            case "listar empresas":
                service.bd.read.get_lista_empresas().each {
                    String rep = tv.empresaView.representacao(it)
                    tv.send_message rep
                }
                break

            case "cadastrar candidato":
                cadastrar_candidato()
                break

            case "cadastrar empresa":
                cadastrar_empresa()
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
