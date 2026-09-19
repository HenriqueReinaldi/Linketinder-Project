package org.linketinder.view.terminal

import org.linketinder.controller.Controller
import org.linketinder.controller.ModelData
import spock.lang.Specification

class TermViewSpec extends Specification {
    Controller controller = Mock()

    def "listar chama o controller certo"() {
        given:
        TermView termView = new TermView(controller)

        when:
        termView.listar(input)

        then:
        1 * controller."$metodo"() >> []

        where:
        input          | metodo
        "candidatos"   | "get_lista_candidato"
        "empresas"     | "get_lista_empresa"
        "vagas"        | "get_lista_vaga"
        "curtidas"     | "get_lista_curtida"
        "competencias" | "get_lista_competencia"
    }

    def "cadastrar chama o controller certo"() {
        given:
        TermView termView = Spy(TermView, constructorArgs: [controller])
        termView.get_input(*_) >> "input"

        when:
        termView.cadastrar(input)

        then:
        1 * controller."$metodo"(_ as ModelData)

        where:
        input       | metodo
        "candidato" | "cadastrar_candidato"
        "empresa"   | "cadastrar_empresa"
        "vaga"      | "cadastrar_vaga"
    }

    def "deletar chama o controller certo"() {
        given:
        TermView termView = Spy(TermView, constructorArgs: [controller])
        termView.get_generic_id() >> 67

        when:
        termView.deletar(input)

        then:
        1 * controller."$metodo"(_ as int)

        where:
        input         | metodo
        "candidato"   | "deletar_candidato"
        "empresa"     | "deletar_empresa"
        "vaga"        | "deletar_vaga"
        "competencia" | "deletar_competencia"
    }

    def "update chama o controller certo"() {
        given:
        TermView termView = Spy(TermView, constructorArgs: [controller])
        termView.get_input(*_) >> "input"
        termView.get_generic_id() >> 67

        when:
        termView.update(input)

        then:
        1 * controller."$metodo"(_ as ModelData)

        where:
        input         | metodo
        "candidato"   | "update_candidato"
        "empresa"     | "update_empresa"
        "vaga"        | "update_vaga"
        "competencia" | "update_competencia"
    }

    def "curtir pela perspectiva chama o controller certo"(){
        given:
        TermView termView = Spy(TermView, constructorArgs: [controller])
        termView.get_input(*_) >> "input"

        when:
        termView.curtir_pela_perspectiva(input)

        then:
        1 * controller."$metodo"(_ as ModelData)

        where:
        input         | metodo
        "candidato"   | "candidato_curtir"
        "empresa"     | "empresa_curtir"
    }

}
