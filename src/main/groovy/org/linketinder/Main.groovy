package org.linketinder


import org.linketinder.controller.AssembleModel
import org.linketinder.controller.Controller
import org.linketinder.DAO.Banco
import org.linketinder.service.Service
import org.linketinder.view.PrefTree
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
//    Banco bd;
//    bd = exit_on_exception("erro conectando com banco!") {
//        new Banco("linketinder")
//    }
//
//    Service service = new Service(bd)
//    AssembleModel assemble_model = new AssembleModel(service: service)
//    Controller controller = new Controller(service: service, assemble_model: assemble_model)
//    TermView view = new TermView(controller: controller)
//
//
//    view.send_message "Digite ? para ajuda\n"
//    while (view.run()) {}
//
//    exit_on_exception("erro desconectando com banco!") {
//        bd.desconectar()
//    }


    PrefTree pt = new PrefTree()

    pt.inserir("carro", {println "carro"})
    pt.inserir("carta", {println "carta"})

    pt.get_closure("carro")()
    pt.get_closure("laura")()
    pt.get_closure("carta")()
}