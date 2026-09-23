package org.linketinder

import org.linketinder.DAO.CandidatoDAO
import org.linketinder.DAO.CompetenciaDAO
import org.linketinder.DAO.CurtidaDAO
import org.linketinder.DAO.EmpresaDAO
import org.linketinder.DAO.EnderecoDAO
import org.linketinder.DAO.VagaDAO
import org.linketinder.controller.CandidatoController
import org.linketinder.controller.CompetenciaController

import org.linketinder.DAO.Banco
import org.linketinder.controller.ControllerBundle
import org.linketinder.controller.CurtidaController
import org.linketinder.controller.EmpresaController
import org.linketinder.controller.VagaController
import org.linketinder.service.CandidatoService
import org.linketinder.service.CompetenciaService
import org.linketinder.service.CurtidaService
import org.linketinder.service.EmpresaService
import org.linketinder.service.ServiceBundle
import org.linketinder.service.VagaService
import org.linketinder.view.View
import org.linketinder.view.ViewIO
import org.linketinder.view.shared.CandidatoView
import org.linketinder.view.shared.CompetenciaView
import org.linketinder.view.shared.CurtidaView
import org.linketinder.view.shared.EmpresaView
import org.linketinder.view.shared.VagaView
import org.linketinder.view.shared.ViewBundle
import org.linketinder.view.terminal.TermIO
import org.linketinder.view.terminal.Terminal

//Henrique de Figueiredo Reinaldi

static <GENERICO> GENERICO exit_on_exception(String msg, Closure<GENERICO> codigo) {
    try {
        return codigo()
    }
    catch (Exception ignored) {
        println msg
        System.exit(-1)
    }
    return null
}


static ServiceBundle inicializar_services(Banco bd) {
    return new ServiceBundle(
            new CandidatoService(bd, new CandidatoDAO(bd), new CompetenciaDAO(bd), new EnderecoDAO(bd)),
            new EmpresaService(bd, new EmpresaDAO(bd), new EnderecoDAO(bd)),
            new CompetenciaService(bd, new CompetenciaDAO(bd)),
            new CurtidaService(bd, new CurtidaDAO(bd), new CandidatoDAO(bd), new VagaDAO(bd)),
            new VagaService(bd, new VagaDAO(bd), new EnderecoDAO(bd), new CompetenciaDAO(bd), new EmpresaDAO(bd))
    )
}

static ViewBundle inicializar_views(ViewIO view_io) {
    return new ViewBundle(
            new CandidatoView(view_io),
            new EmpresaView(view_io),
            new VagaView(view_io),
            new CompetenciaView(view_io),
            new CurtidaView(view_io)
    )
}

static ControllerBundle inicializar_controllers(ServiceBundle services) {
    return new ControllerBundle(
            new CompetenciaController(services.competencia_service),
            new CandidatoController(services.candidato_service),
            new CurtidaController(services.curtida_service, services.candidato_service, services.vaga_service),
            new EmpresaController(services.empresa_service),
            new VagaController(services.vaga_service, services.empresa_service)
    )
}

static void main(String[] args) {
    Banco bd
    bd = exit_on_exception("erro conectando com banco!") { new Banco("linketinder") }

    ServiceBundle services = inicializar_services(bd)
    ControllerBundle controllers = inicializar_controllers(services)

    ViewIO view_io = new TermIO()
    ViewBundle views = inicializar_views(view_io)
    View view = new Terminal(controllers, views, view_io)

    while (true) {
        if (!view.run()) break
    }

    exit_on_exception("erro desconectando com banco!") { bd.desconectar() }
}