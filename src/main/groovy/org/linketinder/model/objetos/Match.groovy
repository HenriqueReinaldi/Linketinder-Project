package org.linketinder.model.objetos

import groovy.transform.ToString
import groovy.transform.TupleConstructor

@ToString(includeSuperProperties = true, includeNames = true)
@TupleConstructor(includeSuperProperties = true)
class Match {
    Candidato candidato
    Vaga vaga
    Recrutador recrutador
    String data_match
}
