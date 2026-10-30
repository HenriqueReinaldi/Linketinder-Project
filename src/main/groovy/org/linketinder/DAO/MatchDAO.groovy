package org.linketinder.DAO

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Match
import org.linketinder.model.objetos.Recrutador
import org.linketinder.model.objetos.Vaga

import java.sql.ResultSet
import java.sql.SQLException

@TupleConstructor
class MatchDAO {
    Banco banco

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
}
