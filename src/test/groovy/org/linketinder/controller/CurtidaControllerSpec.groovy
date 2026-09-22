package org.linketinder.controller

import org.linketinder.service.CurtidaService
import spock.lang.Specification

class CurtidaControllerSpec extends Specification {
    CurtidaService curtida_service = Mock()
    CurtidaController controller = new CurtidaController(curtida_service)

    void "get lista curtida chama o service correto"() {
        when:
        controller.get_lista_curtida()
        then:
        1 * curtida_service.get_lista()
    }

}
