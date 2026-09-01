package org.linketinder.model

import org.linketinder.model.objetos.Endereco

interface IPessoa {
    String getNome();
    String getEmail();
    String getDescricao();
    String getSenha();

    Endereco getEndereco();
}