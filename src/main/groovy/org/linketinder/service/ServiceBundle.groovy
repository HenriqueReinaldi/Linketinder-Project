package org.linketinder.service

import groovy.transform.TupleConstructor

@TupleConstructor
class ServiceBundle {
    CandidatoService candidato_service
    EmpresaService empresa_service
    CompetenciaService competencia_service
    CurtidaService curtida_service
    VagaService vaga_service
}