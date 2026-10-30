package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.CandidatoDAO
import org.linketinder.DAO.MatchDAO
import org.linketinder.DAO.RecrutadorDAO
import org.linketinder.DAO.VagaDAO
import org.linketinder.model.objetos.Match

@TupleConstructor
class MatchService extends Service{
    MatchDAO dao
    CandidatoDAO candidato_dao
    VagaDAO vaga_dao
    RecrutadorDAO recrutador_dao

    List<Match> get_lista() {
        executar_seguramente([]) {
            List<Match> matches = dao.get_lista_match()

            matches.each { Match match ->
                match.candidato = candidato_dao.get_candidato_by_id(match.candidato.id)
                match.vaga = vaga_dao.get_vaga_by_id(match.vaga.id)
                match.recrutador = recrutador_dao.get_recrutador_by_id(match.recrutador.id)
            }

            return matches
        }
    }
}
