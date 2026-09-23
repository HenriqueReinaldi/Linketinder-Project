package org.linketinder.controller

import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.service.EmpresaService
import spock.lang.Specification

class EmpresaControllerSpec extends Specification {
    EmpresaService empresa_service = Mock()
    EmpresaController controller = new EmpresaController(empresa_service)

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
        EmpresaController controller = Spy(constructorArgs: [empresa_service])

        when:
        controller.cadastrar_empresa(md)

        then:
        1 * controller.assemble_empresa(md.data) >> empresa
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
        EmpresaController controller = Spy(constructorArgs: [empresa_service])

        when:
        controller.update_empresa(md)

        then:
        1 * controller.assemble_empresa(md.data) >> empresa
        1 * empresa_service.update(empresa)
    }

    void "assemble empresa monta empresa corretamente"() {
        given:
        EmpresaController controller = Spy(constructorArgs: [empresa_service])
        Map<String, String> empresa_info = [
                "estado"   : "estado",
                "descricao": "descricao",
                "CEP"      : "CEP",
                "pais"     : "pais",
                "CNPJ"     : "CNPJ",
                "nome"     : "nome",
                "email"    : "email",
                "senha"    : "senha",
                "id"       : "1"
        ]

        when:
        Empresa emp = controller.assemble_empresa(empresa_info)

        then:
        emp.CNPJ == "CNPJ"
        emp.nome == "nome"
        emp.email == "email"
        emp.descricao == "descricao"
        emp.senha == "senha"
        emp.id == 1

        emp.endereco.CEP == "CEP"
        emp.endereco.pais == "pais"
        emp.endereco.estado == "estado"
    }
}
