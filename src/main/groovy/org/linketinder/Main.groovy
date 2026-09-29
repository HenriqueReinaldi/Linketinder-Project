package org.linketinder

import org.linketinder.DAO.*
import org.linketinder.controller.*
import org.linketinder.service.*
import org.linketinder.view.View
import org.linketinder.view.ViewIO
import org.linketinder.view.shared.*
import org.linketinder.view.terminal.TermIO
import org.linketinder.view.terminal.Terminal

//Henrique de Figueiredo Reinaldi

static <GENERICO> GENERICO try_exit(Closure<GENERICO> codigo) {
    try {
        return codigo()
    }
    catch (Exception ignored) {
        System.exit(-1)
    }
    return null
}

static ServiceBundle inicializar_services(Banco bd) {
    var candidato_dao = new CandidatoDAO(bd)
    var empresa_dao = new EmpresaDAO(bd)
    var competencia_dao = new CompetenciaDAO(bd)
    var endereco_dao = new EnderecoDAO(bd)
    var vaga_dao = new VagaDAO(bd)
    var curtida_dao = new CurtidaDAO(bd)

    return new ServiceBundle(
            new CandidatoService(candidato_dao, competencia_dao, endereco_dao),
            new EmpresaService(empresa_dao, endereco_dao),
            new CompetenciaService(competencia_dao),
            new CurtidaService(curtida_dao, candidato_dao, vaga_dao),
            new VagaService(vaga_dao, endereco_dao, competencia_dao, empresa_dao)
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
    Banco bd = try_exit { new Banco("postgres") }

    ServiceBundle services = inicializar_services(bd)
    ControllerBundle controllers = inicializar_controllers(services)

    ViewIO view_io = new TermIO()
    ViewBundle views = inicializar_views(view_io)
    View view = new Terminal(controllers, views, view_io)

    while (true) {
        if (!view.run()) break
    }

    try_exit { bd.desconectar() }

}