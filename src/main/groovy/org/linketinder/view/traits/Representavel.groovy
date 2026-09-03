package org.linketinder.view.traits

trait Representavel<Generic> {
    abstract String representacao(Generic objeto);
    abstract void exibir(Generic objeto);
}