package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.CandidatoDAO
import org.linketinder.DAO.CompetenciaDAO
import org.linketinder.DAO.CurtidaDAO
import org.linketinder.DAO.MatchDAO
import org.linketinder.DAO.VagaDAO
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Match
import org.linketinder.model.objetos.Recrutador

import java.time.LocalDate

@TupleConstructor
class CurtidaService extends Service {
    CurtidaDAO dao
    CandidatoDAO candidato_dao
    CompetenciaDAO competencia_dao
    VagaDAO vaga_dao
    MatchDAO match_dao

    List<Curtida> get_lista() {
        executar_seguramente([]) {
            List<Curtida> curtidas = dao.get_lista_curtida()

            curtidas.each { Curtida curtida ->
                curtida.candidato = candidato_dao.get_candidato_by_id(curtida.candidato.id)
                curtida.candidato.competencias = competencia_dao.get_lista_competencias_of_entidade("candidato", curtida.candidato.id)
                curtida.vaga = vaga_dao.get_vaga_by_id(curtida.vaga.id)
            }

            return curtidas
        }
    }

    List<Curtida> get_lista_curtida_recrutador(int recrutador_id) {
        executar_seguramente([]) {
            List<Curtida> curtidas = dao.get_lista_curtida_recrutador(recrutador_id)

            curtidas.each { Curtida curtida ->
                curtida.candidato = candidato_dao.get_candidato_by_id(curtida.candidato.id)
                curtida.candidato.competencias = competencia_dao.get_lista_competencias_of_entidade("candidato", curtida.candidato.id)
                curtida.vaga = vaga_dao.get_vaga_by_id(curtida.vaga.id)
            }

            return curtidas
        }
    }

    boolean curtir_como_candidato(Curtida c) {
        executar_seguramente(false) {
            dao.cadastrar_curtida(c)
            return true
        }
    }

    boolean curtir_como_empresa(Curtida c) {
        executar_seguramente(false) {
            if (c == null || c.candidato == null || c.vaga == null) return false

            boolean status_curtida = dao.empresa_curtir(c)

            Match match = new Match(c.candidato, c.vaga, null, LocalDate.now().toString())
            boolean status_match = match_dao.cadastrar_match(match)

            return status_curtida && status_match
        }
    }

    boolean curtir_como_recrutador(Curtida c, int recrutador_id) {
        executar_seguramente(false) {
            if (c == null || c.candidato == null || c.vaga == null) return false
            if (recrutador_id < 0) return false

            boolean status_curtida = dao.recrutador_curtir(c, recrutador_id)

            Match match = new Match(c.candidato, c.vaga, new Recrutador(id: recrutador_id), LocalDate.now().toString())
            boolean status_match = match_dao.cadastrar_match(match)

            return status_curtida && status_match
        }
    }

}
