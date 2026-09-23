package org.linketinder.service

import org.linketinder.DAO.CandidatoDAO
import org.linketinder.DAO.CompetenciaDAO
import org.linketinder.DAO.EnderecoDAO
import org.linketinder.model.objetos.Competencia
import spock.lang.Specification

class CompetenciaServiceSpec extends Specification{
    CompetenciaDAO competencia_dao = Mock()
    CompetenciaService service = new CompetenciaService(competencia_dao)

    def "get lista competencia chama o metodo DAO correto"(){
        when:
        service.get_lista()

        then:
        1 * competencia_dao.get_lista_competencia()
    }

    def "deletar competencia chama o metodo DAO correto"(){
        when:
        service.deletar(20)

        then:
        1 * competencia_dao.delete_competencia_by_id(20)
    }

    def "atualizar competencia chama o metodo DAO correto"(){
        given:
        Competencia competencia = new Competencia(id:20)

        when:
        service.update(competencia)

        then:
        1 * competencia_dao.update_competencia(competencia)

    }
}
