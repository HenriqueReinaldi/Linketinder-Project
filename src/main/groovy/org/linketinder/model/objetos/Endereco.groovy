package org.linketinder.model.objetos

import groovy.transform.ToString
import groovy.transform.TupleConstructor

@ToString(includeSuperProperties = true, includeNames = true)
@TupleConstructor(includeSuperProperties = true)

class Endereco{
    String CEP, pais, estado
}
