package org.linketinder.controller

import groovy.transform.TupleConstructor

@TupleConstructor
class ControllerBundle {
    CompetenciaController competencia_controller
    CandidatoController candidato_controller
    CurtidaController curtida_controller
    EmpresaController empresa_controller
    VagaController vaga_controller
}
