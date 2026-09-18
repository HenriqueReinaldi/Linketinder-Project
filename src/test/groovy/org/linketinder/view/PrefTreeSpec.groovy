package org.linketinder.view

import spock.lang.Specification

class PrefTreeSpec extends Specification{

    def "Entradas sem colisao"(){
        given:
            PrefTree pt = new PrefTree()
            List<String> entradas = ["entrada", "coisa", "paciencia", "zalfabeto", "pendamonhagama"]

        when:
            entradas.each(){String entrada ->
                pt.inserir(entrada, {entrada})
            }

        then:
            entradas.every() { String entrada ->
                pt.buscar(entrada)() == entrada
            }
    }

    def "Entradas com colisao"(){
        given:
            PrefTree pt = new PrefTree()
            List<String> entradas = ["entrada", "entranha", "e", "entendo", "errata", "erroneo", "paciencia", "pacas"]

        when:
            entradas.each(){String entrada ->
                pt.inserir(entrada, {entrada})
            }

        then:
            entradas.every() { String entrada ->
                pt.buscar(entrada)() == entrada
            }
    }

    def "Buscas sem resultados"(){
        given:
            PrefTree pt = new PrefTree()
            List<String> entradas = ["entrada", "entranha", "e", "entendo", "errata", "erroneo", "paciencia", "pacas"]
            List<String> inexistentes = ["lol!", "pendrive", "en", "east", "wow!"]

        when:
            entradas.each(){String entrada ->
                pt.inserir(entrada, {entrada})
            }

        then:
            inexistentes.every() { String entrada ->
                pt.buscar(entrada)() != entrada
            }
    }
}
