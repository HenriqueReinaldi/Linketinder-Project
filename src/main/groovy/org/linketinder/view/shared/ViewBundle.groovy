package org.linketinder.view.shared

import groovy.transform.TupleConstructor

@TupleConstructor
class ViewBundle {
    CandidatoView candidato_view
    EmpresaView empresa_view
    VagaView vaga_view
    CompetenciaView competencia_view
    CurtidaView curtida_view
}
