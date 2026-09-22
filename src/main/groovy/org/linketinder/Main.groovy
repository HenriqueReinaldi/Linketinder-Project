package org.linketinder


import org.linketinder.controller.AssembleModel
import org.linketinder.controller.CandidatoController
import org.linketinder.controller.CompetenciaController
import org.linketinder.controller.Controller
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
import org.linketinder.view.shared.CandidatoView
import org.linketinder.view.shared.CompetenciaView
import org.linketinder.view.shared.CurtidaView
import org.linketinder.view.shared.EmpresaView
import org.linketinder.view.shared.VagaView
import org.linketinder.view.shared.ViewBundle
import org.linketinder.view.terminal.TermView

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
            new CandidatoService(bd),
            new EmpresaService(bd),
            new CompetenciaService(bd),
            new CurtidaService(bd),
            new VagaService(bd)
    )
}

static ViewBundle inicializar_views() {
    return new ViewBundle(
            new CandidatoView(),
            new EmpresaView(),
            new VagaView(),
            new CompetenciaView(),
            new CurtidaView()
    )
}

static ControllerBundle inicializar_controllers(ServiceBundle services, AssembleModel assemble_model) {
    return new ControllerBundle(
            new CompetenciaController(assemble_model, services.competencia_service),
            new CandidatoController(assemble_model, services.candidato_service),
            new CurtidaController(services.curtida_service),
            new EmpresaController(assemble_model, services.empresa_service),
            new VagaController(assemble_model, services.vaga_service)
    )
}

static AssembleModel inicializar_assemble_model(ServiceBundle services) {
    return new AssembleModel(
            services.empresa_service,
            services.candidato_service,
            services.vaga_service
    )
}


static void main(String[] args) {
    Banco bd
    bd = exit_on_exception("erro conectando com banco!") { new Banco("linketinder") }

    ServiceBundle services = inicializar_services(bd)
    AssembleModel assemble_model = inicializar_assemble_model(services)
    ControllerBundle controllers = inicializar_controllers(services, assemble_model)
    ViewBundle views = inicializar_views()

    View view = new TermView(controllers, views)


    while (true) {
        if (!view.run()) break
    }

    exit_on_exception("erro desconectando com banco!") { bd.desconectar() }
}