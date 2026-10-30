package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Match
import org.linketinder.service.MatchService

@TupleConstructor
class MatchController {
    MatchService match_service

    List<Match> get_lista_matches(){
        return match_service.get_lista()
    }
}
