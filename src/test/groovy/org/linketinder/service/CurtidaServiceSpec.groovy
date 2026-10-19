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

    def "curtir como recrutador delega o identificador para validar escopo no DAO"() {
        given:
        Curtida curtida = new Curtida(candidato: new Candidato(id: 7), vaga: new Vaga(id: 9))

        when:
        boolean resultado = service.curtir_como_recrutador(curtida, 21)

        then:
        1 * curtida_dao.recrutador_curtir(curtida, 21) >> true
        resultado
    }

    def "get lista curtida do recrutador delega ao DAO e hidrata objetos relacionados"() {
        given:
        Curtida curtida = new Curtida(candidato: new Candidato(id: 7), vaga: new Vaga(id: 9))

        when:
        List<Curtida> resultado = service.get_lista_curtida_recrutador(3)

        then:
        1 * curtida_dao.get_lista_curtida_recrutador(3) >> [curtida]
        1 * candidato_dao.get_candidato_by_id(7) >> new Candidato(id: 7)
        1 * vaga_dao.get_vaga_by_id(9) >> new Vaga(id: 9)
        resultado.size() == 1
        resultado[0].candidato.id == 7
        resultado[0].vaga.id == 9
    }
}
