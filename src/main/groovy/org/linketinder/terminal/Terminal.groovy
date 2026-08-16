package org.linketinder.terminal

import groovy.transform.TupleConstructor
import org.linketinder.dados.Dados

@TupleConstructor
class Terminal {
    Dados dados

    int receber_input(String input){

        switch (input){
            case "?":
                println "listar candidatos | mostra todos os candidatos"
                println "listar empresas   | mostra todos as empresas"
                println "sair              | fecha o programa"
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
