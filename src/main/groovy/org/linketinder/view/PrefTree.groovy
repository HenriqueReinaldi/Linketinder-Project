package org.linketinder.view

class PrefTree {
    PrefNode raiz;

    PrefTree(){
        raiz = new PrefNode()
        raiz.letra = '.'
        raiz.valor = {println "raiz!"}
        raiz.filhos = []
    }

    void inserir(String palavra, Closure acao){
        PrefNode nivel_atual = raiz

        for (int i = 0; i < palavra.length(); i++){
            String letra = palavra[i]
            boolean proximo = false

            for (PrefNode node in nivel_atual.filhos){
                if (node.letra == letra){
                    nivel_atual = node
                    proximo = true
                    break
                }
            }
            if (proximo) continue

            PrefNode novo = new PrefNode()
            novo.letra = letra
            novo.valor = {println "?"}
            novo.filhos = []
            nivel_atual.filhos << novo
            nivel_atual = novo
        }
        nivel_atual.valor = acao
    }

    Closure get_closure(String busca){
        PrefNode nivel_atual = raiz

        for (int i = 0; i < busca.length(); i++){
            String letra = busca[i]
            boolean proximo = false

            for (PrefNode node in nivel_atual.filhos){
                if (node.letra == letra){
                    nivel_atual = node
                    proximo = true
                    break
                }
            }
            if (proximo) continue

            return {println "nao encontrado no trie"}
        }

        return nivel_atual.valor
    }

    void funcao(){
        raiz.valor()
    }
}
