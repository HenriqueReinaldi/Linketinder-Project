package org.linketinder.view.traits

trait Representavel<GENERICO> {
    abstract String representacao(GENERICO objeto);
    abstract void exibir(GENERICO objeto);
}