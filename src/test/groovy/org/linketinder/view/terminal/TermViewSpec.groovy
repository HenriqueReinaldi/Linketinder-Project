package org.linketinder.view.terminal

import org.linketinder.controller.CandidatoController
import org.linketinder.controller.CandidatoControllerSpec
import org.linketinder.controller.CompetenciaController
import org.linketinder.controller.CompetenciaControllerSpec
import org.linketinder.controller.ControllerBundle
import org.linketinder.controller.CurtidaController
import org.linketinder.controller.CurtidaControllerSpec
import org.linketinder.controller.EmpresaController
import org.linketinder.controller.EmpresaControllerSpec
import org.linketinder.controller.ModelData
import org.linketinder.controller.VagaController
import org.linketinder.controller.VagaControllerSpec
import org.linketinder.view.shared.CandidatoView
import org.linketinder.view.shared.CompetenciaView
import org.linketinder.view.shared.CurtidaView
import org.linketinder.view.shared.EmpresaView
import org.linketinder.view.shared.VagaView
import org.linketinder.view.shared.ViewBundle
import spock.lang.Specification

class TermViewSpec extends Specification {
    CandidatoController candidato_controller = Mock()
    EmpresaController empresa_controller = Mock()
    CompetenciaController competencia_controller = Mock()
    CurtidaController curtida_controller = Mock()
    VagaController vaga_controller = Mock()
    ControllerBundle controller_bundle = new ControllerBundle(
            competencia_controller, candidato_controller, curtida_controller, empresa_controller, vaga_controller
    )
    CandidatoView candidato_view = Mock()
    EmpresaView empresa_view = Mock()
    VagaView vaga_view = Mock()
    CompetenciaView competencia_view = Mock()
    CurtidaView curtida_view = Mock()
    ViewBundle view_bundle = new ViewBundle(
            candidato_view, empresa_view, vaga_view, competencia_view, curtida_view
    )

    def "listar chama o controller certo"() {
        given:
        TermView termView = new TermView(controller_bundle, view_bundle)

        when:
        termView.listar(input)

        then:
        1 * controller_bundle."$controller"."$metodo"() >> []

        where:
        input          | controller               | metodo
        "candidatos"   | "candidato_controller"   | "get_lista_candidato"
        "empresas"     | "empresa_controller"     | "get_lista_empresa"
        "vagas"        | "vaga_controller"        | "get_lista_vaga"
        "curtidas"     | "curtida_controller"     | "get_lista_curtida"
        "competencias" | "competencia_controller" | "get_lista_competencia"
    }

    def "cadastrar chama o controller certo"() {
        given:
        TermView termView = Spy(TermView, constructorArgs: [controller_bundle, view_bundle])
        termView.get_input(*_) >> "input"

        when:
        termView.cadastrar(input)

        then:
        1 * controller_bundle."$controller"."$metodo"(_ as ModelData)

        where:
        input       | controller             | metodo
        "candidato" | "candidato_controller" | "cadastrar_candidato"
        "empresa"   | "empresa_controller"   | "cadastrar_empresa"
        "vaga"      | "vaga_controller"      | "cadastrar_vaga"
    }

    def "deletar chama o controller certo"() {
        given:
        TermView termView = Spy(TermView, constructorArgs: [controller_bundle, view_bundle])
        termView.get_generic_id() >> 67

        when:
        termView.deletar(input)

        then:
        1 * controller_bundle."$controller"."$metodo"(_ as int)

        where:
        input         | controller               | metodo
        "candidato"   | "candidato_controller"   | "deletar_candidato"
        "empresa"     | "empresa_controller"     | "deletar_empresa"
        "vaga"        | "vaga_controller"        | "deletar_vaga"
        "competencia" | "competencia_controller" | "deletar_competencia"
    }

    def "update chama o controller certo"() {
        given:
        TermView termView = Spy(TermView, constructorArgs: [controller_bundle, view_bundle])
        termView.get_input(*_) >> "input"
        termView.get_generic_id() >> 67

        when:
        termView.update(input)

        then:
        1 * controller_bundle."$controller"."$metodo"(_ as ModelData)

        where:
        input         | controller               | metodo
        "candidato"   | "candidato_controller"   | "update_candidato"
        "empresa"     | "empresa_controller"     | "update_empresa"
        "vaga"        | "vaga_controller"        | "update_vaga"
        "competencia" | "competencia_controller" | "update_competencia"
    }

    def "curtir pela perspectiva chama o controller certo"() {
        given:
        TermView termView = Spy(TermView, constructorArgs: [controller_bundle, view_bundle])
        termView.get_input(*_) >> "input"

        when:
        termView.curtir_pela_perspectiva(input)

        then:
        1 * controller_bundle."$controller"."$metodo"(_ as ModelData)

        where:
        input         | controller               | metodo
        "candidato"   | "candidato_controller"   | "candidato_curtir"
        "empresa"     | "empresa_controller"     | "empresa_curtir"
    }

    def "funcao run retorna falso quando input for sair"() {
        given:
        TermView termView = Spy(TermView, constructorArgs: [controller_bundle, view_bundle])
        termView.get_input(*_) >> saida

        expect:
        termView.run() == esperado

        where:
        saida              | esperado
        "comando"          | true
        "sair"             | false
        "outros fdsaf asd" | true
        " "                | true
    }
}
