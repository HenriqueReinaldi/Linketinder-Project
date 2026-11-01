package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Match
import org.linketinder.service.MatchService

@TupleConstructor
class MatchController {
    MatchService match_service

    List<Match> get_lista_matches(){
        return match_service.get_lista()
    }

    List<Match> get_lista_matches_por_candidato(int candidato_id){
        return match_service.get_lista_por_candidato(new Candidato(id: candidato_id))
    }
}
