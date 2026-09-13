package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.controller.AssembleModel
import org.linketinder.database.Banco
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga

@TupleConstructor
class Service {
    Banco bd

    List<Candidato> get_lista_candidatos(){
        bd.read.get_lista_candidatos()
    }
    List<Empresa> get_lista_empresas(){
        bd.read.get_lista_empresas()
    }
    List<Vaga> get_lista_vagas(){
        bd.read.get_lista_vagas()
    }
    List<Competencia> get_lista_competencias(){
        bd.read.get_lista_competencias()
    }
    List<Curtida> get_lista_curtidas(){
        bd.read.get_lista_curtidas()
    }

    void cadastrar_candidato(Candidato c){
        bd.create.cadastrar_candidato(c)
    }
    void cadastrar_empresa(Empresa m){
        bd.create.cadastrar_empresa(m);
    }
    void cadastrar_vaga(Vaga v){
        bd.create.cadastrar_vaga(v)
    }

    void deletar_candidato(int id){
        bd.delete.delete_candidato_by_id(id)
    }
    void deletar_empresa(int id){
        bd.delete.delete_empresa_by_id(id)
    }
    void deletar_vaga(int id){
        bd.delete.delete_vaga_by_id(id)
    }
    void deletar_competencia(int id){
        bd.delete.delete_competencia_by_id(id)
    }

    void update_candidato(Candidato c){
        bd.update.update_candidato(c)
    }
    void update_vaga(Vaga v){
        bd.update.update_vaga(v)
    }
    void update_empresa(Empresa m){
        bd.update.update_empresa(m)
    }
    void update_competencia(Competencia c){
        bd.update.update_competencia(c)
    }

    void candidato_curtir(Curtida c){
        bd.create.cadastrar_curtida(c)
    }
    void empresa_curtir(Curtida c){
        bd.update.empresa_curtir(c)
    }
}
