package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga
import org.linketinder.service.Service
import org.linketinder.view.terminal.TermView

@TupleConstructor
class Controller {
    TermView tv
    Service service

    void listar_candidatos(){
        service.bd.read.get_lista_candidatos().each {
            String rep = tv.candidato_view.representacao(it)
            tv.send_message rep
        }
    }
    void listar_empresas(){
        service.bd.read.get_lista_empresas().each {
            String rep = tv.empresa_view.representacao(it)
            tv.send_message rep
        }
    }
    void listar_vagas(){
        service.bd.read.get_lista_vagas().each {
            String rep = tv.vaga_view.representacao(it)
            tv.send_message rep
        }
    }
    void listar_competencias(){
        service.bd.read.get_lista_competencias().each {
            String rep = tv.competencia_view.representacao(it)
            tv.send_message rep
        }
    }

    boolean cadastrar_candidato(){
        Map<String, String> ci = tv.candidato_view.capturar_dados()
        Candidato c = service.assemble_candidato(ci)
        return service.bd.create.cadastrar_candidato(c)
    }
    boolean cadastrar_empresa(){
        Map<String, String> ei = tv.empresa_view.capturar_dados()
        Empresa m = service.assemble_empresa(ei)
        return service.bd.create.cadastrar_empresa(m);
    }
    boolean cadastrar_vaga(){
        Map<String, String> vi = tv.vaga_view.capturar_dados()
        Vaga v = service.assemble_vaga(vi)
        return service.bd.create.cadastrar_vaga(v)
    }

    boolean deletar_candidato(){
        try{
            int id = Integer.parseInt(tv.get_input("id:"))

            return service.bd.delete.delete_candidato_by_id(id)
        }catch (Exception ignored) {}

        return false
    }
    boolean deletar_empresa(){
        try{
            int id = Integer.parseInt(tv.get_input("id:"))

            return service.bd.delete.delete_empresa_by_id(id)
        }catch (Exception ignored) {}

        return false
    }
    boolean deletar_vaga(){
        try{
            int id = Integer.parseInt(tv.get_input("id:"))

            return service.bd.delete.delete_vaga_by_id(id)
        }catch (Exception ignored) {}

        return false
    }
    boolean deletar_competencia(){
        try{
            int id = Integer.parseInt(tv.get_input("id:"))

            return service.bd.delete.delete_competencia_by_id(id)
        }catch (Exception ignored) {}

        return false
    }

    boolean update_candidato(){
        int id = -1
        try{
            id = Integer.parseInt(tv.get_input("id:"))
        }catch (Exception ignored) {return false}

        Map<String, String> ci = tv.candidato_view.capturar_dados()
        Candidato c = service.assemble_candidato(ci)
        c.id = id
        return service.bd.update.update_candidato(c)
    }
    boolean update_vaga(){
        int id = -1
        try{
            id = Integer.parseInt(tv.get_input("id:"))
        }catch (Exception ignored) {return false}

        Map<String, String> vi = tv.vaga_view.capturar_dados()
        Vaga v = service.assemble_vaga(vi)
        v.id = id
        return service.bd.update.update_vaga(v)
    }
    boolean update_empresa(){
        int id = -1
        try{
            id = Integer.parseInt(tv.get_input("id:"))
        }catch (Exception ignored) {return false}

        Map<String, String> vi = tv.empresa_view.capturar_dados()
        Empresa m = service.assemble_empresa(vi)
        m.id = id
        return service.bd.update.update_empresa(m)
    }
    boolean update_competencia(){
        int id = -1
        try{
            id = Integer.parseInt(tv.get_input("id:"))
        }catch (Exception ignored) {return false}

        String tecnologia = tv.get_input("tecnologia:")

        Competencia c = new Competencia(tecnologia: tecnologia, id: id)
        return service.bd.update.update_competencia(c)
    }

    int receber_input(String input){
        switch (input){
            case "?":
                tv.send_message "Comandos read:"
                tv.send_message "listar <candidatos / empresas / vagas / competencias>\n"

                tv.send_message "Comandos create:"
                tv.send_message "cadastrar <candidato / empresa / vaga>"
                tv.send_message "nota: competencias são criadas automaticamente por demanda.\n"

                tv.send_message "Comandos delete:"
                tv.send_message "deletar <candidato / empresa / vaga / competencia>\n"

                tv.send_message "Comandos update:"
                tv.send_message "update <candidato / empresa / vaga / competencia>\n"

                tv.send_message "Outros:"
                tv.send_message "sair"
                break

            case "listar candidatos":
                listar_candidatos()
                break

            case "listar empresas":
                listar_empresas()
                break

            case "listar vagas":
                listar_vagas()
                break

            case "listar competencias":
                listar_competencias()
                break

            case "cadastrar candidato":
                cadastrar_candidato()
                break

            case "cadastrar empresa":
                cadastrar_empresa()
                break

            case "cadastrar vaga":
                cadastrar_vaga()
                break

            case "deletar candidato":
                deletar_candidato()
                break

            case "deletar empresa":
                deletar_empresa()
                break

            case "deletar vaga":
                deletar_vaga()
                break

            case "deletar competencia":
                deletar_competencia()
                break

            case "update candidato":
                update_candidato()
                break

            case "update vaga":
                update_vaga()
                break

            case "update empresa":
                update_empresa()
                break

            case "update competencia":
                update_competencia()
                break

            case "sair":
                return 1
        }
        0
    }

    void init() {
        tv.send_message "Digite ? para ajuda\n"
        while (true){
            String res = tv.get_input "@>"
            if (receber_input(res)) break
        }
    }
}
