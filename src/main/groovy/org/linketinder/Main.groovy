package org.linketinder

import groovy.transform.Undefined.EXCEPTION
import org.linketinder.controller.Controller
import org.linketinder.database.Banco
import org.linketinder.service.Service
import org.linketinder.view.terminal.TermView


//Henrique de Figueiredo Reinaldi

static <GENERICO> GENERICO exit_on_exception(String msg, Closure<GENERICO> codigo){
    try{
        return codigo()
    }
    catch(Exception ignored){
        println msg
        System.exit(-1)
    }
    return null
}

static void main(String[] args) {
    Banco bd;
    bd = exit_on_exception("erro conectando com banco!") {
        new Banco("linketinder")
    }
    TermView term_view = new TermView()
    Service service = new Service(bd)

    Controller controller = new Controller(term_view, service)
    controller.init()

    exit_on_exception("erro desconectando com banco!") {
        bd.desconectar()
    }

}