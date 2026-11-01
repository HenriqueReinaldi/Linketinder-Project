package org.linketinder.controller

import org.linketinder.service.MatchService
import spock.lang.Specification

class MatchControllerSpec extends Specification {
    MatchService match_service = Mock()
    MatchController controller = new MatchController(match_service)

    void "get lista matches chama o service correto"() {
        when:
        controller.get_lista_matches()
        then:
        1 * match_service.get_lista()
    }

    void "get lista matches por candidato id chama o service correto"() {
        when:
        controller.get_lista_matches_por_candidato(1)
        then:
        1 * match_service.get_lista_por_candidato(_)
    }


}
