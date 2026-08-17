package org.linketinder.terminal

import groovy.transform.TupleConstructor
import org.linketinder.classes.Candidato
import org.linketinder.classes.Empresa
import org.linketinder.dados.Cadastro
import org.linketinder.dados.Dados

@TupleConstructor
class Terminal {
    Dados dados
    Cadastro cadastro

    Candidato cadastrar_candidato(){
        Scanner scan = new Scanner(System.in);
        def pergunta = {String it -> print it; scan.nextLine()}

        try{
            def nome = pergunta("Nome: ")
            def email = pergunta("Email: ")
            def estado = pergunta("Estado: ")
            def CEP = pergunta("CEP: ")
            def descricao = pergunta("Descrição: ")
            def CPF = pergunta("CPF: ")
            def idade = pergunta("Idade: ").toInteger()
            def competencias = pergunta("Competencias: ").tokenize()

            Candidato c = new Candidato(nome, email, estado, CEP, descricao, CPF, idade, competencias)

            if (cadastro.cadastrar_candidato(c)){
                return c
            }
            return null
        }
        catch (Exception e){
            println e
            return null;
        }
    }

    Empresa cadastrar_empresa(){
        Scanner scan = new Scanner(System.in);
        def pergunta = {String it -> print it; scan.nextLine()}

        try{
            def nome = pergunta("Nome: ")
            def email = pergunta("Email: ")
            def estado = pergunta("Estado: ")
            def CEP = pergunta("CEP: ")
            def descricao = pergunta("Descrição: ")
            def CNPJ = pergunta("CPF: ")
            def pais = pergunta("País: ")
            def competencias = pergunta("Competencias: ").tokenize()

            Empresa e = new Empresa(nome, email, estado, CEP, descricao, CNPJ, pais, competencias)

            if (cadastro.cadastrar_empresa(e)){
                return e
            }
            return null
        }
        catch (Exception e){
            println e
            return null;
        }
    }


    int receber_input(String input){
        switch (input){
            case "?":
                println "listar candidatos    | mostra todos os candidatos"
                println "listar empresas      | mostra todos as empresas"
                println "cadastrar candidato  | "
                println "cadastrar empresa    | "
                println "sair                 | fecha o programa"
                return 0
            case "listar candidatos":
                dados.candidatos.each {it.representacao()}
                return 0
            case "listar empresas":
                dados.empresas.each {it.representacao()}
                return 0
            case "cadastrar candidato":
                cadastrar_candidato();
                return 0
            case "cadastrar empresa":
                cadastrar_empresa();
                return 0
            case "sair":
                return 1
        }

        0
    }

    void init() {
        Scanner scan = new Scanner(System.in);
        println "? para ajuda..."

        while (true){
            print "@>"

            String input = scan.nextLine();
            if (receber_input(input)) break
        }

    }
}
