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
        Map<String, String> cd = tv.candidatoView.capturar_dados()

        Candidato candidato = null;
        try{
            List<Competencia> competencias = cd["competencias"]
                .tokenize()
                .collect{new Competencia(it.trim())}

            Endereco endereco = new Endereco(
                CEP: cd.CEP,
                pais: null,
                estado: cd.estado
            )

            candidato = new Candidato(
                CPF: cd.CPF,
                idade: cd.idade.toInteger(),
                competencias: competencias,
                nome: cd.nome,
                email: cd.email,
                descricao: cd.descricao,
                senha: cd.senha,
                endereco: endereco
            )

        }
        catch (Exception e) {
            e.printStackTrace(); return false
        }

        return service.cadastrar_candidato(candidato)
    }

    boolean cadastrar_empresa(){
        Map<String, String> cd = tv.empresaView.capturar_dados()

        Empresa empresa = null;
        try{
            List<Competencia> competencias = cd["competencias"]
                .tokenize()
                .collect{new Competencia(it.trim())}

            Endereco endereco = new Endereco(
                CEP: cd.CEP,
                pais: cd.pais,
                estado: cd.estado
            )

            empresa = new Empresa(
                CNPJ: cd.CNPJ,
                competencias_desejadas: competencias,
                nome: cd.nome,
                email: cd.email,
                descricao: cd.descricao,
                senha: cd.senha,
                endereco: endereco
            )

        }
        catch (Exception e) {
            e.printStackTrace(); return false
        }

        return service.cadastrar_empresa(empresa)
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

            case "listar candidatos":
                service.bd.candidatos.each {
                    String rep = tv.candidatoView.representacao(it)
                    tv.send_message rep
                }
                break

            case "listar empresas":
                service.bd.empresas.each {
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
