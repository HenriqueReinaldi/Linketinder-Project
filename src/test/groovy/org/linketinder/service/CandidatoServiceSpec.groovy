package org.linketinder.service

import org.linketinder.DAO.Banco
import org.linketinder.DAO.CandidatoDAO
import org.linketinder.DAO.CompetenciaDAO
import org.linketinder.DAO.EnderecoDAO
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Endereco
import spock.lang.Specification

class CandidatoServiceSpec extends Specification{
    CandidatoDAO candidato_dao = Mock()
    CompetenciaDAO competencia_dao = Mock()
    EnderecoDAO endereco_dao = Mock()
    CandidatoService service = new CandidatoService(candidato_dao, competencia_dao, endereco_dao)

    def "get lista candidato chama o metodo DAO correto"(){
        when:
        service.get_lista()

        then:
        1 * candidato_dao.get_lista_candidato()
    }

    def "cadastrar candidato chama os metodos DAO corretos"(){
        given:
        Candidato candidato = new Candidato(
                id: 1,
                endereco: new Endereco(id: 1),
                competencias: [new Competencia(tecnologia: "python"), new Competencia(tecnologia: "cython")]
        )

        when:
        service.cadastrar(candidato)

        then:
        1 * endereco_dao.cadastrar_endereco_se_nao_existe(candidato.endereco)
        1 * candidato_dao.cadastrar_candidato_se_nao_existe(candidato)
        2 * competencia_dao.create_if_not_exists_competencia(_)
        2 * competencia_dao.cadastrar_competencias_entidade("candidato", *_)
    }

    def "deletar candidato chama o metodo DAO correto"(){
        when:
        service.deletar(20)

        then:
        1 * candidato_dao.delete_candidato_by_id(20)
    }

    def "update candidato chama os metodos DAO corretos"(){
        given:
        Candidato candidato = new Candidato(
                id: 1,
                endereco: new Endereco(id: 1),
                competencias: [new Competencia(tecnologia: "python"), new Competencia(tecnologia: "cython")]
        )

        when:
        service.update(candidato)

        then:
        1 * endereco_dao.cadastrar_endereco_se_nao_existe(candidato.endereco)
        1 * candidato_dao.update_candidato(candidato)
        1 * competencia_dao.delete_entidade_competencias_by_entidadeid("candidato", candidato.id)
        2 * competencia_dao.create_if_not_exists_competencia(_)
        2 * competencia_dao.cadastrar_competencias_entidade("candidato", candidato.id, _)
    }

    def "get by id chama o metodo DAO correto"(){
        when:
        service.get_by_id("20")

        then:
        1 * candidato_dao.get_candidato_by_id(20)
    }
}
