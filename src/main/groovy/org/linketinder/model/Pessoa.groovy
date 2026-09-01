package org.linketinder.model

import org.linketinder.model.objetos.Endereco

abstract class Pessoa implements IPessoa{
    String nome, email, descricao, senha
    Endereco endereco
}
