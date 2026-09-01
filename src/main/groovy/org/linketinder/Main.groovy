package org.linketinder
import org.linketinder.controller.Controller
import org.linketinder.dao.Banco
import org.linketinder.service.Service
import org.linketinder.view.terminal.TermView


//Henrique de Figueiredo Reinaldi

static void main(String[] args) {

    Banco bd = new Banco()

    TermView term_view = new TermView();
    Service service = new Service(bd);

    Controller controller = new Controller(term_view, service)

    controller.init();
}