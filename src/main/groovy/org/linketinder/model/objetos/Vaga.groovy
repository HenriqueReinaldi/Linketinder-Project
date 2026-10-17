package org.linketinder.model.objetos

import groovy.transform.ToString
import groovy.transform.TupleConstructor

@ToString(includeNames = true)
@TupleConstructor
class Vaga {
    int id = -1

    String nome, descricao, nicho
    Endereco endereco
    Empresa empresa
    List<Competencia> competencias_desejadas = [];
}
