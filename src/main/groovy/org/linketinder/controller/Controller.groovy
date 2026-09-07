package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga
import org.linketinder.service.Service
import org.linketinder.view.terminal.TermView

@TupleConstructor
class Controller {
    TermView tv
    Service service
    AssembleModel am

    void listar_candidatos(){
        service.bd.read.get_lista_candidatos().each {
            tv.candidato_view.exibir(it)
        }
    }
    void listar_empresas(){
        service.bd.read.get_lista_empresas().each {
            tv.empresa_view.exibir(it)
        }
    }
    void listar_vagas(){
        service.bd.read.get_lista_vagas().each {
            tv.vaga_view.exibir(it)
        }
    }
    void listar_competencias(){
        service.bd.read.get_lista_competencias().each {
            tv.competencia_view.exibir(it)
        }
    }
    void listar_curtidas(){
        service.bd.read.get_lista_curtidas().each {
            tv.curtida_view.exibir(it)
        }
    }


    void cadastrar_candidato(){
        Map<String, String> ci = tv.candidato_view.capturar_dados()
        Candidato c = am.assemble_candidato(ci)
        service.bd.create.cadastrar_candidato(c)
    }
    void cadastrar_empresa(){
        Map<String, String> ei = tv.empresa_view.capturar_dados()
        Empresa m = am.assemble_empresa(ei)
        service.bd.create.cadastrar_empresa(m);
    }
    boolean cadastrar_vaga(){
        Map<String, String> vi = tv.vaga_view.capturar_dados()
        Vaga v = am.assemble_vaga(vi)
        service.bd.create.cadastrar_vaga(v)
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
        Candidato c = am.assemble_candidato(ci)
        c.id = id
        return service.bd.update.update_candidato(c)
    }
    boolean update_vaga(){
        int id = -1
        try{
            id = Integer.parseInt(tv.get_input("id:"))
        }catch (Exception ignored) {return false}

        Map<String, String> vi = tv.vaga_view.capturar_dados()
        Vaga v = am.assemble_vaga(vi)
        v.id = id
        return service.bd.update.update_vaga(v)
    }
    boolean update_empresa(){
        int id = -1
        try{
            id = Integer.parseInt(tv.get_input("id:"))
        }catch (Exception ignored) {return false}

        Map<String, String> vi = tv.empresa_view.capturar_dados()
        Empresa m = am.assemble_empresa(vi)
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

    void candidato_curtir(){
        Map<String, String> ci = tv.curtida_view.capturar_dados()
        Curtida c = am.assemble_curtida(ci)
        service.bd.create.cadastrar_curtida(c)

    }
    void empresa_curtir(){
        Map<String, String> ci = tv.curtida_view.capturar_dados()
        Curtida c = am.assemble_curtida(ci)
        service.bd.update.empresa_curtir(c)
    }

    int receber_input(String input){
        switch (input){
            case "?":
                tv.send_message "É importante destacar que todos esses comandos são usados pela perspectiva de um ADM, por isso falta anonimidade.\n"

                tv.send_message "Comandos read:"
                tv.send_message "listar <candidatos / empresas / vagas / competencias / curtidas>\n"

                tv.send_message "Comandos create:"
                tv.send_message "cadastrar <candidato / empresa / vaga>"
                tv.send_message "candidato.curtir"
                tv.send_message "nota: competencias são criadas automaticamente por demanda.\n"

                tv.send_message "Comandos delete:"
                tv.send_message "deletar <candidato / empresa / vaga / competencia>\n"

                tv.send_message "Comandos update:"
                tv.send_message "update <candidato / empresa / vaga / competencia>"
                tv.send_message "empresa.curtir\n"

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

            case "listar curtidas":
                listar_curtidas()
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

            case "candidato.curtir":
                candidato_curtir()
                break

            case "empresa.curtir":
                empresa_curtir()
                break

            case "sair":
                return 1
        }
        0
    }

    void init() {
        am = new AssembleModel(service: service)

        tv.send_message "Digite ? para ajuda\n"
        while (true){
            String res = tv.get_input "@>"
            if (receber_input(res)) break
        }
    }
}
