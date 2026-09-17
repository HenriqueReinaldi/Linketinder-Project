package org.linketinder


import org.linketinder.controller.AssembleModel
import org.linketinder.controller.Controller
import org.linketinder.DAO.Banco
import org.linketinder.service.CandidatoService
import org.linketinder.service.CompetenciaService
import org.linketinder.service.CurtidaService
import org.linketinder.service.EmpresaService

import org.linketinder.service.VagaService
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
    Banco bd
    bd = exit_on_exception("erro conectando com banco!") { new Banco("linketinder") }


    CandidatoService candidato_service = new CandidatoService(bd)
    EmpresaService empresa_service = new EmpresaService(bd)
    CompetenciaService competencia_service = new CompetenciaService(bd)
    CurtidaService curtida_service = new CurtidaService(bd)
    VagaService vaga_service = new VagaService(bd)


    AssembleModel assemble_model = new AssembleModel(empresa_service, candidato_service, vaga_service)
    Controller controller = new Controller(assemble_model, candidato_service, empresa_service, competencia_service, curtida_service, vaga_service)


    TermView view = new TermView(controller)
    while (view.run()) {;}

    exit_on_exception("erro desconectando com banco!") { bd.desconectar() }
}