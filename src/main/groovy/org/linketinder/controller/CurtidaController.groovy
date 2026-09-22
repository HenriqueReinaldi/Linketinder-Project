package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Curtida
import org.linketinder.service.CurtidaService

@TupleConstructor
class CurtidaController {
    CurtidaService curtida_service

    List<Curtida> get_lista_curtida(){
        return curtida_service.get_lista()
    }
}
