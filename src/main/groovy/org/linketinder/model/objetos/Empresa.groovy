package org.linketinder.model.objetos

import groovy.transform.ToString
import groovy.transform.TupleConstructor
import org.linketinder.model.Pessoa

@ToString(includeSuperProperties = true, includeNames = true)
@TupleConstructor(includeSuperProperties = true)
class Empresa extends Pessoa{
    int id = -1

    String CNPJ
}
