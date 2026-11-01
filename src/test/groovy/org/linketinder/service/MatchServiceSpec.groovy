package org.linketinder.service

import org.linketinder.DAO.CandidatoDAO
import org.linketinder.DAO.MatchDAO
import org.linketinder.DAO.RecrutadorDAO
import org.linketinder.DAO.VagaDAO
import org.linketinder.model.objetos.Candidato
import spock.lang.Specification

class MatchServiceSpec extends Specification {
    MatchDAO dao = Mock()
    CandidatoDAO candidato_dao = Mock()
    VagaDAO vaga_dao = Mock()
    RecrutadorDAO recrutador_dao = Mock()
    MatchService service = new MatchService(dao, candidato_dao, vaga_dao, recrutador_dao)


    def "get lista match chama o metodo DAO correto"() {
        when:
        service.get_lista()

        then:
        1 * dao.get_lista_match()
    }

    def "get lista match por candidato chama o metodo DAO correto"() {
        given:
        Candidato candidato = new Candidato(id: 38)

        when:
        service.get_lista_por_candidato(candidato)

        then:
        1 * dao.get_lista_match_por_candidato(candidato)
    }
}
