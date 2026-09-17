package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Vaga
import org.linketinder.service.CandidatoService
import org.linketinder.service.CompetenciaService
import org.linketinder.service.CurtidaService
import org.linketinder.service.EmpresaService
import org.linketinder.service.Service
import org.linketinder.service.VagaService

@TupleConstructor
class Controller {
    AssembleModel assemble_model

    CandidatoService candidato_service
    EmpresaService empresa_service
    CompetenciaService competencia_service
    CurtidaService curtida_service
    VagaService vaga_service



    List<Candidato> get_lista_candidato(){
        return candidato_service.get_lista()
    }
    List<Empresa> get_lista_empresa(){
        return empresa_service.get_lista()
    }
    List<Competencia> get_lista_competencia(){
        return competencia_service.get_lista()
    }
    List<Vaga> get_lista_vaga(){
        return vaga_service.get_lista()
    }
    List<Curtida> get_lista_curtida(){
        return curtida_service.get_lista()
    }


    void cadastrar_candidato(ModelData modelo){
        Candidato c = assemble_model.assemble_candidato(modelo.data)
        candidato_service.cadastrar(c)
    }
    void cadastrar_empresa(ModelData modelo){
        Empresa m = assemble_model.assemble_empresa(modelo.data)
        empresa_service.cadastrar(m)
    }
    void cadastrar_vaga(ModelData modelo){
        Vaga v = assemble_model.assemble_vaga(modelo.data)
        vaga_service.cadastrar(v)
    }




}
