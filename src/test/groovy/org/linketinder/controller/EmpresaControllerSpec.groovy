package org.linketinder.controller

import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.service.EmpresaService
import spock.lang.Specification

class EmpresaControllerSpec extends Specification {
    AssembleModel assemble_model = Mock()
    EmpresaService empresa_service = Mock()
    EmpresaController controller = new EmpresaController(assemble_model, empresa_service)

    void "get lista empresa chama o service correto"() {
        when:
        controller.get_lista_empresa()
        then:
        1 * empresa_service.get_lista()
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

    void "deletar empresa chama o service correto"() {
        when:
        controller.deletar_empresa(14)
        then:
        1 * empresa_service.deletar(14)
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

    void "empresa curtir chama o service correto e assemblemodel"() {
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
