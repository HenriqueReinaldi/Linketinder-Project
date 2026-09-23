package org.linketinder.service

import org.linketinder.DAO.Banco
import org.linketinder.DAO.CandidatoDAO
import org.linketinder.DAO.CurtidaDAO
import org.linketinder.DAO.CompetenciaDAO
import org.linketinder.DAO.EnderecoDAO
import org.linketinder.DAO.VagaDAO
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Vaga
import spock.lang.Specification

class CurtidaServiceSpec extends Specification{
    CurtidaDAO curtida_dao = Mock()
    CandidatoDAO candidato_dao = Mock()
    VagaDAO vaga_dao = Mock()
    CurtidaService service = new CurtidaService(curtida_dao, candidato_dao, vaga_dao)

    def "get lista curtida chama o metodo DAO correto"(){
        when:
        service.get_lista()

        then:
        1 * curtida_dao.get_lista_curtida() >> [new Curtida(candidato: new Candidato(id: 1), vaga: new Vaga(id: 3))]
        1 * candidato_dao.get_candidato_by_id(1)
        1 * vaga_dao.get_vaga_by_id(3)
    }

    def "curtir como candidato chama o metodo DAO correto"(){
        given:
        Curtida curtida = new Curtida()

        when:
        service.curtir_como_candidato(curtida)

        then:
        1 * curtida_dao.cadastrar_curtida(curtida)
    }


    def "curtir como empresa chama o metodo DAO correto"(){
        given:
        Curtida curtida = new Curtida()

        when:
        service.curtir_como_empresa(curtida)

        then:
        1 * curtida_dao.empresa_curtir(curtida)

    }
}
