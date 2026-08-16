package org.linketinder

//seu nome
//Henrique de Figueiredo Reinaldi

import org.linketinder.dados.Dados
import org.linketinder.terminal.Terminal

static void main(String[] args) {
    Dados d = new Dados()
    d.init()

    Terminal t = new Terminal(d)
    t.init()

}