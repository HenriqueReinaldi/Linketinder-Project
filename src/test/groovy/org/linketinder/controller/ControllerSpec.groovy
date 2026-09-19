package org.linketinder.controller

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Vaga
import org.linketinder.service.CandidatoService
import org.linketinder.service.CompetenciaService
import org.linketinder.service.CurtidaService
import org.linketinder.service.EmpresaService
import org.linketinder.service.VagaService
import spock.lang.Specification

class ControllerSpec extends Specification {
    AssembleModel assemble_model = Mock()
    CandidatoService candidato_service = Mock()
    EmpresaService empresa_service = Mock()
    CompetenciaService competencia_service = Mock()
    CurtidaService curtida_service = Mock()
    VagaService vaga_service = Mock()

    Controller controller = new Controller(assemble_model, candidato_service, empresa_service, competencia_service, curtida_service, vaga_service)

    void "get lista candidato chama o service correto"() {
        when:
        controller.get_lista_candidato()
        then:
        1 * candidato_service.get_lista()
    }
    void "get lista empresa chama o service correto"() {
        when:
        controller.get_lista_empresa()
        then:
        1 * empresa_service.get_lista()
    }
    void "get lista competencia chama o service correto"() {
        when:
        controller.get_lista_competencia()
        then:
        1 * competencia_service.get_lista()
    }
    void "get lista curtida chama o service correto"() {
        when:
        controller.get_lista_curtida()
        then:
        1 * curtida_service.get_lista()
    }
    void "get lista vaga  chama o service correto"() {
        when:
        controller.get_lista_curtida()
        then:
        1 * curtida_service.get_lista()
    }


    void "cadastrar candidato chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Candidato candidato = new Candidato(id: 1)

        when:
        controller.cadastrar_candidato(md)

        then:
        1 * assemble_model.assemble_candidato(md.data) >> candidato
        1 * candidato_service.cadastrar(candidato)
    }
    void "cadastrar empresa chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Empresa empresa = new Empresa(id: 1)

        when:
        controller.cadastrar_empresa(md)

        then:
        1 * assemble_model.assemble_empresa(md.data) >> empresa
        1 * empresa_service.cadastrar(empresa)
    }
    void "cadastrar vaga chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Vaga vaga = new Vaga(id: 1)

        when:
        controller.cadastrar_vaga(md)

        then:
        1 * assemble_model.assemble_vaga(md.data) >> vaga
        1 * vaga_service.cadastrar(vaga)
    }


    void "deletar candidato chama o service correto"(){
        when:
        controller.deletar_candidato(13)
        then:
        1 * candidato_service.deletar(13)
    }
    void "deletar empresa chama o service correto"(){
        when:
        controller.deletar_empresa(14)
        then:
        1 * empresa_service.deletar(14)
    }
    void "deletar vaga chama o service correto"(){
        when:
        controller.deletar_vaga(15)
        then:
        1 * vaga_service.deletar(15)
    }
    void "deletar competencia chama o service correto"(){
        when:
        controller.deletar_competencia(16)
        then:
        1 * competencia_service.deletar(16)
    }


    void "update candidato chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Candidato candidato = new Candidato(id: 1)

        when:
        controller.update_candidato(md)

        then:
        1 * assemble_model.assemble_candidato(md.data) >> candidato
        1 * candidato_service.update(candidato)
    }
    void "update empresa chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Empresa empresa = new Empresa(id: 1)

        when:
        controller.update_empresa(md)

        then:
        1 * assemble_model.assemble_empresa(md.data) >> empresa
        1 * empresa_service.update(empresa)
    }
    void "update vaga chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Vaga vaga = new Vaga(id: 1)

        when:
        controller.update_vaga(md)

        then:
        1 * assemble_model.assemble_vaga(md.data) >> vaga
        1 * vaga_service.update(vaga)
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


    void "candidato curtir chama o service correto e assemblemodel"(){
        given:
        ModelData md = new ModelData()
        Curtida curtida = new Curtida(id: 1)

        when:
        controller.candidato_curtir(md)

        then:
        1 * assemble_model.assemble_curtida(md.data) >> curtida
        1 * candidato_service.curtir(curtida)
    }
    void "empresa curtir chama o service correto e assemblemodel"(){
        given:
        ModelData md = new ModelData()
        Curtida curtida = new Curtida(id: 1)

        when:
        controller.empresa_curtir(md)

        then:
        1 * assemble_model.assemble_curtida(md.data) >> curtida
        1 * empresa_service.curtir(curtida)
    }
}
