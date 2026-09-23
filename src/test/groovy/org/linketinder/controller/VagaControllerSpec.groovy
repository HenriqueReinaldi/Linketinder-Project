package org.linketinder.controller

import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Vaga
import org.linketinder.service.CurtidaService
import org.linketinder.service.EmpresaService
import org.linketinder.service.VagaService
import spock.lang.Specification

class VagaControllerSpec extends Specification {
    VagaService vaga_service = Mock()
    EmpresaService empresa_service = Mock()
    VagaController controller = new VagaController(vaga_service, empresa_service)


    void "get lista vaga  chama o service correto"() {
        when:
        controller.get_lista_vaga()
        then:
        1 * vaga_service.get_lista()
    }

    void "cadastrar vaga chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Vaga vaga = new Vaga(id: 1)
        VagaController controller = Spy(constructorArgs: [vaga_service])

        when:
        controller.cadastrar_vaga(md)

        then:
        1 * controller.assemble_vaga(md.data) >> vaga
        1 * vaga_service.cadastrar(vaga)
    }

    void "deletar vaga chama o service correto"() {
        when:
        controller.deletar_vaga(15)
        then:
        1 * vaga_service.deletar(15)
    }

    void "update vaga chama o service correto e assemblemodel"() {
        given:
        ModelData md = new ModelData()
        Vaga vaga = new Vaga(id: 1)
        VagaController controller = Spy(constructorArgs: [vaga_service])

        when:
        controller.update_vaga(md)

        then:
        1 * controller.assemble_vaga(md.data) >> vaga
        1 * vaga_service.update(vaga)
    }

    void "assemble_vaga monta vaga corretamente"() {
        given:
        VagaController controller = Spy(constructorArgs: [vaga_service, empresa_service])
        Map<String, String> vaga_info = [
                "competencias_desejadas": "competencia",
                "CEP"                   : "CEP",
                "pais"                  : "pais",
                "estado"                : "estado",
                "empresa_CNPJ"          : "empresa_CNPJ",
                "nome"                  : "nome",
                "descricao"             : "descricao",
                "id"                    : "1"
        ]
        Empresa empresa = new Empresa(CNPJ: "empresa_CNPJ")

        when:
        Vaga vaga = controller.assemble_vaga(vaga_info)

        then:
        1 * empresa_service.get_by_CNPJ("empresa_CNPJ") >> empresa

        vaga.nome == "nome"
        vaga.descricao == "descricao"
        vaga.id == 1
        vaga.endereco.CEP == "CEP"
        vaga.endereco.pais == "pais"
        vaga.endereco.estado == "estado"
        vaga.empresa == empresa

        vaga.competencias_desejadas.size() == 1
        vaga.competencias_desejadas[0].tecnologia == "competencia"
    }
}
