package org.linketinder.view.shared

import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga
import org.linketinder.view.View
import spock.lang.Shared
import spock.lang.Specification

class VagaViewSpec extends Specification{
    View view = Mock()
    
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
    @Shared
    Vaga vaga = new Vaga(
        id: 1,
        nome: "vaga_nome",
        descricao: "descricao",
        endereco: new Endereco(
                estado: "estado",
                CEP: "CEP"
        ),
        empresa: empresa,
        competencias_desejadas: []
    )

    def "capturar_dados usa view.get_input para coletar infos"() {
        given:
        VagaView vaga_view = new VagaView(view)

        when:
        Map<String, String> info = vaga_view.capturar_dados()

        then:
        7 * view.get_input(_ as String) >> "input"

        and:
        info.every { it.value == "input"}
    }

    def "Extensão: capturar_dados com ID usa view.get_input para coletar infos"() {
        given:
        VagaView vaga_view = new VagaView(view)

        when:
        Map<String, String> info = vaga_view.capturar_dados(true)

        then:
        8 * view.get_input(_ as String) >> "input"

        and:
        info.every { it.value == "input"}
    }

    def "Representacao funciona corretamente"(){
        given:
        VagaView vaga_view = new VagaView(view)

        when:
        String resposta = vaga_view.representacao(vaga)

        then:
        verifyAll {
            resposta.contains("nome")
            resposta.contains("1")
        }
    }

    def "Exibir chama representacao e view.send_message"(){
        given:
        VagaView vaga_view = Spy(VagaView, constructorArgs: [view])

        when:
        vaga_view.exibir(vaga)

        then:
        1 * vaga_view.representacao(vaga)
        1 * view.send_message(_)

    }
}
