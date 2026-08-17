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

        try{
            String nome = scan.nextLine();
            String email = scan.nextLine();
            String estado  = scan.nextLine();
            String CEP = scan.nextLine();
            String descricao = scan.nextLine();
            String CPF = scan.nextLine();
            int idade = Integer.parseInt(scan.nextLine());
            List<String> competencias  = scan.nextLine().split(" ");

            Candidato c = new Candidato(nome, email, estado, CEP, descricao, CPF, idade, competencias)

            if (cadastro.cadastrar_candidato(c)){
                return c
            }
            return null
        }
        catch (Exception ignored){
            return null;
        }
    }

    Empresa cadastrar_empresa(){
        Scanner scan = new Scanner(System.in);

        try{
            String nome = scan.nextLine();
            String email = scan.nextLine();
            String estado  = scan.nextLine();
            String CEP = scan.nextLine();
            String descricao = scan.nextLine();
            String CNPJ = scan.nextLine();
            String pais = scan.nextLine();
            List<String> competencias  = scan.nextLine().split(" ");

            Empresa e = new Empresa(nome, email, estado, CEP, descricao, CNPJ, pais, competencias)

            if (cadastro.cadastrar_empresa(e)){
                return e
            }
            return null
        }
        catch (Exception ignored){
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
