package org.linketinder.model.objetos

import groovy.transform.ToString
import groovy.transform.TupleConstructor

@ToString(includeSuperProperties = true, includeNames = true)
@TupleConstructor(includeSuperProperties = true)

class Competencia{
    int id = -1
    String tecnologia
}
