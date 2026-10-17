package org.linketinder.service

import org.linketinder.DAO.Banco
import org.linketinder.DAO.EmpresaDAO
import org.linketinder.DAO.RecrutadorDAO
import org.linketinder.DAO.VagaDAO
import org.linketinder.DAO.CompetenciaDAO
import org.linketinder.DAO.EnderecoDAO
import org.linketinder.model.objetos.Vaga
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Recrutador
import spock.lang.Specification

class VagaServiceSpec extends Specification{
    VagaDAO vaga_dao = Mock()
    CompetenciaDAO competencia_dao = Mock()
    EnderecoDAO endereco_dao = Mock()
    EmpresaDAO empresa_dao = Mock()
    RecrutadorDAO recrutador_dao = Mock()
    VagaService service = new VagaService(vaga_dao, endereco_dao, competencia_dao, empresa_dao, recrutador_dao)

    def "get lista vaga chama o metodo DAO correto"(){
        when:
        service.get_lista()

        then:
        1 * vaga_dao.get_lista_vaga()
    }

    def "cadastrar vaga chama os metodos DAO corretos"(){
        given:
        Vaga vaga = new Vaga(
                id: 1,
                endereco: new Endereco(id: 1),
                competencias_desejadas: [new Competencia(tecnologia: "python"), new Competencia(tecnologia: "cython")]
        )

        when:
        service.cadastrar(vaga)

        then:
        1 * endereco_dao.cadastrar_endereco_se_nao_existe(vaga.endereco)
        1 * vaga_dao.cadastrar_vaga(vaga)
        2 * competencia_dao.create_if_not_exists_competencia(_)
        2 * competencia_dao.cadastrar_competencias_entidade("vaga", *_)
    }

    def "deletar vaga chama o metodo DAO correto"(){
        when:
        service.deletar(20)

        then:
        1 * vaga_dao.delete_vaga_by_id(20)
    }

    def "update vaga chama os metodos DAO corretos"(){
        given:
        Vaga vaga = new Vaga(
                id: 1,
                endereco: new Endereco(id: 1),
                competencias_desejadas: [new Competencia(tecnologia: "python"), new Competencia(tecnologia: "cython")]
        )

        when:
        service.update(vaga)

        then:
        1 * endereco_dao.cadastrar_endereco_se_nao_existe(vaga.endereco)
        1 * vaga_dao.update_vaga(vaga)
        1 * competencia_dao.delete_entidade_competencias_by_entidadeid("vaga", vaga.id)
        2 * competencia_dao.create_if_not_exists_competencia(_)
        2 * competencia_dao.cadastrar_competencias_entidade("vaga", vaga.id, _)
    }

    def "get by id chama o metodo DAO correto"(){
        when:
        service.get_by_id("20")

        then:
        1 * vaga_dao.get_vaga_by_id(20)
    }

    def "lista do recrutador de nicho geral retorna todas as vagas da empresa dele"() {
        given:
        VagaService service = Spy(constructorArgs: [vaga_dao, endereco_dao, competencia_dao, empresa_dao, recrutador_dao])
        service.recrutador_dao = recrutador_dao
        Recrutador recrutador = new Recrutador(
                id: 7,
                nicho: "geral",
                empresa: new Empresa(id: 3)
        )
        List<Vaga> vagas = [
                new Vaga(id: 1, empresa: new Empresa(id: 3), nicho: "tecnologia"),
                new Vaga(id: 2, empresa: new Empresa(id: 3), nicho: "saude"),
                new Vaga(id: 3, empresa: new Empresa(id: 4), nicho: "tecnologia")
        ]

        when:
        List<Vaga> resultado = service.get_lista_vaga_recrutador(7)

        then:
        1 * recrutador_dao.get_recrutador_by_id(7) >> recrutador
        1 * service.get_lista() >> vagas
        resultado*.id == [1, 2]
    }

    def "lista do recrutador de nicho especifico exige empresa e nicho exatos"() {
        given:
        VagaService service = Spy(constructorArgs: [vaga_dao, endereco_dao, competencia_dao, empresa_dao, recrutador_dao])
        service.recrutador_dao = recrutador_dao
        Recrutador recrutador = new Recrutador(
                id: 7,
                nicho: "tecnologia",
                empresa: new Empresa(id: 3)
        )
        List<Vaga> vagas = [
                new Vaga(id: 1, empresa: new Empresa(id: 3), nicho: "tecnologia"),
                new Vaga(id: 2, empresa: new Empresa(id: 3), nicho: "geral"),
                new Vaga(id: 3, empresa: new Empresa(id: 4), nicho: "tecnologia")
        ]

        when:
        List<Vaga> resultado = service.get_lista_vaga_recrutador(7)

        then:
        1 * recrutador_dao.get_recrutador_by_id(7) >> recrutador
        1 * service.get_lista() >> vagas
        resultado*.id == [1]
    }

    def "lista do recrutador inexistente retorna lista vazia sem consultar vagas"() {
        given:
        service.recrutador_dao = recrutador_dao

        when:
        List<Vaga> resultado = service.get_lista_vaga_recrutador(99)

        then:
        1 * recrutador_dao.get_recrutador_by_id(99) >> null
        0 * vaga_dao.get_lista_vaga()
        resultado == []
    }
}
