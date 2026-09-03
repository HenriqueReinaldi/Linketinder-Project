package org.linketinder.model.objetos

import groovy.transform.ToString

class Vaga {
    int id = -1

    String nome, descricao
    Endereco endereco
    Empresa empresa
    List<Competencia> competencias_desejadas = [];
}
