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

    void listar_candidatos(){
        bd.read.get_lista_candidatos().each {Candidato candidato ->
            tv.candidato_view.exibir(candidato)
        }
    }
    void listar_empresas(){
        bd.read.get_lista_empresas().each {Empresa empresa ->
            tv.empresa_view.exibir(empresa)
        }
    }
    void listar_vagas(){
        bd.read.get_lista_vagas().each {Vaga vaga ->
            tv.vaga_view.exibir(vaga)
        }
    }
    void listar_competencias(){
        bd.read.get_lista_competencias().each {Competencia competencia ->
            tv.competencia_view.exibir(competencia)
        }
    }
    void listar_curtidas(){
        bd.read.get_lista_curtidas().each {Curtida curtida ->
            tv.curtida_view.exibir(curtida)
        }
    }


    void cadastrar_candidato(Candidato c){
        bd.create.cadastrar_candidato(c)
    }
    void cadastrar_empresa(Empresa m){
        bd.create.cadastrar_empresa(m);
    }
    boolean cadastrar_vaga(Vaga v){
        bd.create.cadastrar_vaga(v)
    }

    boolean deletar_candidato(){
        try{
            int id = Integer.parseInt(tv.get_input("id:"))

            return bd.delete.delete_candidato_by_id(id)
        }catch (Exception ignored) {}

        return false
    }
    boolean deletar_empresa(){
        try{
            int id = Integer.parseInt(tv.get_input("id:"))

            return bd.delete.delete_empresa_by_id(id)
        }catch (Exception ignored) {}

        return false
    }
    boolean deletar_vaga(){
        try{
            int id = Integer.parseInt(tv.get_input("id:"))

            return bd.delete.delete_vaga_by_id(id)
        }catch (Exception ignored) {}

        return false
    }
    boolean deletar_competencia(){
        try{
            int id = Integer.parseInt(tv.get_input("id:"))

            return bd.delete.delete_competencia_by_id(id)
        }catch (Exception ignored) {}

        return false
    }

    boolean update_candidato(){
        int id = -1
        try{
            id = Integer.parseInt(tv.get_input("id:"))
        }catch (Exception ignored) {return false}

        Map<String, String> ci = tv.candidato_view.capturar_dados()
        Candidato c = am.assemble_candidato(ci)
        c.id = id
        return bd.update.update_candidato(c)
    }
    boolean update_vaga(){
        int id = -1
        try{
            id = Integer.parseInt(tv.get_input("id:"))
        }catch (Exception ignored) {return false}

        Map<String, String> vi = tv.vaga_view.capturar_dados()
        Vaga v = am.assemble_vaga(vi)
        v.id = id
        return bd.update.update_vaga(v)
    }
    boolean update_empresa(){
        int id = -1
        try{
            id = Integer.parseInt(tv.get_input("id:"))
        }catch (Exception ignored) {return false}

        Map<String, String> vi = tv.empresa_view.capturar_dados()
        Empresa m = am.assemble_empresa(vi)
        m.id = id
        return bd.update.update_empresa(m)
    }
    boolean update_competencia(){
        int id = -1
        try{
            id = Integer.parseInt(tv.get_input("id:"))
        }catch (Exception ignored) {return false}

        String tecnologia = tv.get_input("tecnologia:")

        Competencia c = new Competencia(tecnologia: tecnologia, id: id)
        return bd.update.update_competencia(c)
    }

    void candidato_curtir(){
        Map<String, String> ci = tv.curtida_view.capturar_dados()
        Curtida c = am.assemble_curtida(ci)
        bd.create.cadastrar_curtida(c)

    }
    void empresa_curtir(){
        Map<String, String> ci = tv.curtida_view.capturar_dados()
        Curtida c = am.assemble_curtida(ci)
        bd.update.empresa_curtir(c)
    }
}
