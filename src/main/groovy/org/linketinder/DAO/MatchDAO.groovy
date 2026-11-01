package org.linketinder.DAO

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Match
import org.linketinder.model.objetos.Recrutador
import org.linketinder.model.objetos.Vaga

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

@TupleConstructor
class MatchDAO {
    Banco banco

    boolean cadastrar_match(Match match) throws SQLException {
        if (match == null) return false
        String busca
        Closure busca_args

        if (match.recrutador == null){
            busca = """
                insert into match (candidato_id, vaga_id, data_match) 
                values (?, ?, ?) on conflict (candidato_id, vaga_id) do nothing
            """
            busca_args = { PreparedStatement pst ->
                pst.setInt(1, match.candidato.id)
                pst.setInt(2, match.vaga.id)
                pst.setDate(3,  java.sql.Date.valueOf(match.data_match))
            }
        }
        else {
            busca = """
                insert into match (candidato_id, vaga_id, recrutador_id, data_match) 
                values (?, ?, ?, ?) on conflict (candidato_id, vaga_id) do nothing
            """
            busca_args = { PreparedStatement pst ->
                pst.setInt(1, match.candidato.id)
                pst.setInt(2, match.vaga.id)
                pst.setInt(3, match.recrutador.id)
                pst.setDate(4, java.sql.Date.valueOf(match.data_match))
            }
        }

        banco.executar_detectar_updates(busca, busca_args)
    }

    List<Match> get_lista_match() throws SQLException {
        return banco.executar("select * from match", {}) { ResultSet res ->
            List<Match> matches = []

            while (res.next()) {
                matches << new Match(
                    new Candidato(id: res.getInt("candidato_id")),
                    new Vaga(id: res.getInt("vaga_id")),
                    new Recrutador(id: res.getInt("recrutador_id")),
                    res.getString("data_match")
                )
            }

            return matches
        }
    }

    List<Match> get_lista_match_por_candidato(Candidato candidato) throws SQLException {
        String busca = "select * from match where candidato_id = ?"
        Closure busca_args = { PreparedStatement pst -> pst.setInt(1, candidato.id) }

        return banco.executar(busca, busca_args) { ResultSet res ->
            List<Match> matches = []

            while (res.next()) {
                matches << new Match(
                        new Candidato(id: res.getInt("candidato_id")),
                        new Vaga(id: res.getInt("vaga_id")),
                        new Recrutador(id: res.getInt("recrutador_id")),
                        res.getString("data_match")
                )
            }

            return matches
        }
    }
}
