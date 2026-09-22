package org.linketinder.view.shared

import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.view.View
import org.linketinder.view.ViewIO
import spock.lang.Shared
import spock.lang.Specification

class EmpresaViewSpec extends Specification {
    ViewIO view = Mock()

    @Shared
    Empresa empresa = new Empresa(
            id: 1,
            nome: "nome",
            descricao: "descricao",
            email: "email",
            endereco: new Endereco(
                    pais: "pais",
                    estado: "estado",
                    CEP: "CEP"
            ),
            CNPJ: "CNPJ"
    )

    void "capturar_dados usa view.get_input para coletar infos"() {
        given:
        EmpresaView empresa_view = new EmpresaView(view)

        when:
        Map<String, String> info = empresa_view.capturar_dados()

        then:
        8 * view.get_input(_ as String) >> "input"

        and:
        info.every { it.value == "input" }
    }

    void "Extensão: capturar_dados com ID usa view.get_input para coletar infos"() {
        given:
        EmpresaView empresa_view = new EmpresaView(view)

        when:
        Map<String, String> info = empresa_view.capturar_dados(true)

        then:
        9 * view.get_input(_ as String) >> "input"

        and:
        info.every { it.value == "input" }
    }

    void "Representacao funciona corretamente"() {
        given:
        EmpresaView empresa_view = new EmpresaView(view)

        when:
        String resposta = empresa_view.representacao(empresa)

        then:
        verifyAll {
            resposta.contains("nome")
            resposta.contains("descricao")
            resposta.contains("email")
            resposta.contains("pais")
            resposta.contains("estado")
            resposta.contains("CEP")
            resposta.contains("CNPJ")
            resposta.contains("1")
        }
    }

    void "Exibir chama representacao e view.send_message"() {
        given:
        EmpresaView empresa_view = Spy(EmpresaView, constructorArgs: [view])

        when:
        empresa_view.exibir(empresa)

        then:
        1 * empresa_view.representacao(empresa)
        1 * view.send_message(_)

    }
}
