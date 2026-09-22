package org.linketinder.controller

import org.linketinder.model.objetos.Competencia
import org.linketinder.service.CompetenciaService
import spock.lang.Specification

class CompetenciaControllerSpec extends Specification {
    AssembleModel assemble_model = Mock()
    CompetenciaService competencia_service = Mock()
    CompetenciaController controller = new CompetenciaController(assemble_model, competencia_service)

    void "get lista competencia chama o service correto"() {
        when:
        controller.get_lista_competencia()
        then:
        1 * competencia_service.get_lista()
    }

    void "deletar competencia chama o service correto"() {
        when:
        controller.deletar_competencia(16)
        then:
        1 * competencia_service.deletar(16)
    }

    void "update competencia chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Competencia competencia = new Competencia(id: 1)

        when:
        controller.update_competencia(md)

        then:
        1 * assemble_model.assemble_competencia(md.data) >> competencia
        1 * competencia_service.update(competencia)
    }
}
