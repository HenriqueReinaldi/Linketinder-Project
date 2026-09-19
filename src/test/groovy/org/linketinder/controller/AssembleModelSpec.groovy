package org.linketinder.controller

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Vaga
import org.linketinder.service.CandidatoService
import org.linketinder.service.EmpresaService
import org.linketinder.service.VagaService
import spock.lang.Specification

class AssembleModelSpec extends Specification {
    EmpresaService empresa_service = Mock()
    CandidatoService candidato_service = Mock()
    VagaService vaga_service = Mock()

    AssembleModel assemble_model = new AssembleModel(empresa_service, candidato_service, vaga_service)

    def "assemble com seguranca retorna valor da closure quando nao ha erros"() {
        expect:
        assemble_model.assemble_com_seguranca(entrada) == saida

        where:
        entrada                                  | saida
        ({ Integer.parseInt("sigma da bahia") }) | null
        ({ throw new Exception("teste") })       | null
    }

    def "assemble com seguranca retorna null quando ha erros na closure"() {
        expect:
        assemble_model.assemble_com_seguranca(entrada) == saida

        where:
        entrada                                  | saida
        ({ Integer.parseInt("sigma da bahia") }) | null
        ({ throw new Exception("teste") })       | null
    }

    def "assemble candidato monta candidato corretamente"() {
        given:
        Map<String, String> candidato_info = [
                "competencias": "competencia",
                "CEP"         : "CEP",
                "estado"      : "estado",
                "CPF"         : "CPF",
                "nome"        : "nome",
                "sobrenome"   : "sobrenome",
                "nascimento"  : "nascimento",
                "email"       : "email",
                "descricao"   : "descricao",
                "senha"       : "senha",
                "id"          : "1"
        ]

        when:
        Candidato cand = assemble_model.assemble_candidato(candidato_info)

        then:
        cand.nome == "nome"
        cand.sobrenome == "sobrenome"
        cand.CPF == "CPF"
        cand.data_nascimento == "nascimento"
        cand.email == "email"
        cand.descricao == "descricao"
        cand.senha == "senha"
        cand.id == 1

        cand.endereco.CEP == "CEP"
        cand.endereco.estado == "estado"

        cand.competencias.size() == 1
        cand.competencias[0].tecnologia == "competencia"
    }

    def "assemble empresa monta empresa corretamente"() {
        given:
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
        Empresa emp = assemble_model.assemble_empresa(empresa_info)

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

    def "assemble competencia monta competencia corretamente"() {
        given:
        Map<String, String> competencia_info = [
                "tecnologia": "tecnologia",
                "id"        : "1"
        ]

        when:
        Competencia comp = assemble_model.assemble_competencia(competencia_info)

        then:
        comp.tecnologia == "tecnologia"
        comp.id == 1
    }

    def "assemble_vaga monta vaga corretamente"() {
        given:
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
        Vaga vaga = assemble_model.assemble_vaga(vaga_info)

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

    def "assemble_curtida monta curtida corretamente"() {
        given:
        Map<String, String> curtida_info = [
                "candidato_id": "12",
                "vaga_id"     : "13",
                "id"          : "1"
        ]
        Candidato candidato = new Candidato(id: 12)
        Vaga vaga = new Vaga(id: 13)

        when:
        Curtida curtida = assemble_model.assemble_curtida(curtida_info)

        then:
        1 * candidato_service.get_by_id("12") >> candidato
        1 * vaga_service.get_by_id("13") >> vaga

        curtida.id == 1
        curtida.candidato == candidato
        curtida.vaga == vaga
    }
}
