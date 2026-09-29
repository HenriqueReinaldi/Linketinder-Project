package org.linketinder.model.objetos

class Vaga {
    int id = -1

    String nome, descricao
    Endereco endereco
    Empresa empresa
    List<Competencia> competencias_desejadas = [];
}
