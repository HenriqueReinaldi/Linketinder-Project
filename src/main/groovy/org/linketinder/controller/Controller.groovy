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


    void deletar_candidato(int id){
        candidato_service.deletar(id)
    }
    void deletar_empresa(int id){
        empresa_service.deletar(id)
    }
    void deletar_vaga(int id){
        vaga_service.deletar(id)
    }
    void deletar_competencia(int id){
        competencia_service.deletar(id)
    }


    void update_candidato(ModelData modelo){
        Candidato c = assemble_model.assemble_candidato(modelo.data)
        candidato_service.update(c)
    }
    void update_empresa(ModelData modelo){
        Empresa m = assemble_model.assemble_empresa(modelo.data)
        empresa_service.update(m)
    }
    void update_vaga(ModelData modelo){
        Vaga v = assemble_model.assemble_vaga(modelo.data)
        vaga_service.update(v)
    }
    void update_competencia(ModelData modelo){
        Competencia c = assemble_model.assemble_competencia(modelo.data)
        competencia_service.update(c)
    }


    void candidato_curtir(ModelData modelo){
        Curtida c = assemble_model.assemble_curtida(modelo.data)
        candidato_service.curtir(c)

    }
    void empresa_curtir(ModelData modelo){
        Curtida c = assemble_model.assemble_curtida(modelo.data)
        empresa_service.curtir(c)
    }
}
