package org.linketinder.model.objetos

import groovy.transform.ToString
import groovy.transform.TupleConstructor
import org.linketinder.model.Pessoa

@ToString(includeSuperProperties = true, includeNames = true)
@TupleConstructor(includeSuperProperties = true)
class Recrutador {
    int id = -1
    String CPF, data_nascimento, nicho
    Empresa empresa
    String nome, email, descricao, senha
}
