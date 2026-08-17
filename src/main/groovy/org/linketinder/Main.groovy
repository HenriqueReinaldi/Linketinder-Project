package org.linketinder

import org.linketinder.dados.Cadastro

//seu nome
//Henrique de Figueiredo Reinaldi

import org.linketinder.dados.Dados
import org.linketinder.terminal.Terminal

static void main(String[] args) {


    Dados d = new Dados()
    d.init()

    Cadastro c = new Cadastro(d)

    Terminal t = new Terminal(d, c)
    t.init()

}