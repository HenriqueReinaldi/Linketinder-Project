package org.linketinder.service

import org.linketinder.DAO.EmpresaDAO
import org.linketinder.DAO.RecrutadorDAO
import org.linketinder.DAO.VagaDAO
import org.linketinder.DAO.CompetenciaDAO
import org.linketinder.DAO.EnderecoDAO
import org.linketinder.model.objetos.Vaga
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Empresa
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

    def "lista de vagas do recrutador delega o filtro e hidrata as vagas retornadas"() {
        given:
        Vaga vaga = new Vaga(
                id: 14,
                nicho: "tecnologia",
                endereco: new Endereco(id: 2),
                empresa: new Empresa(id: 3)
        )
        Endereco endereco = new Endereco(id: 2)
        Empresa empresa = new Empresa(id: 3)
        List<Competencia> competencias = [new Competencia(tecnologia: "groovy")]

        when:
        List<Vaga> resultado = service.get_lista_vaga_recrutador(7)

        then:
        1 * vaga_dao.get_lista_vaga_recrutador(7) >> [vaga]
        1 * endereco_dao.get_endereco_by_id(2) >> endereco
        1 * competencia_dao.get_lista_competencias_of_entidade("vaga", 14) >> competencias
        1 * empresa_dao.get_empresa_by_id(3) >> empresa
        resultado.size() == 1
        resultado[0].is(vaga)
        resultado[0].nicho == "tecnologia"
        resultado[0].endereco.is(endereco)
        resultado[0].competencias_desejadas == competencias
        resultado[0].empresa.is(empresa)
    }
}
