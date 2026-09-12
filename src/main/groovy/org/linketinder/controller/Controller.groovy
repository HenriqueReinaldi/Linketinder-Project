package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Vaga
import org.linketinder.service.Service
import org.linketinder.view.View

@TupleConstructor
class Controller {
    View view
    Service service
    AssembleModel assemble_model

    Map<String, Runnable> inputs = [
            "?": this.&citar_ajuda,
            "listar candidatos":   service.&listar_candidatos,
            "listar empresas":     service.&listar_empresas,
            "listar vagas":        service.&listar_vagas,
            "listar competencias": service.&listar_competencias,
            "listar curtidas":     service.&listar_curtidas,

            "cadastrar candidato": this.&cadastrar_candidato,
            "cadastrar empresa":   this.&cadastrar_empresa,
            "cadastrar vaga":      this.&cadastrar_vaga,
    ]

    private void cadastrar_candidato(){
        Map<String, String> candidato_info = view.candidato_view.capturar_dados()
        Candidato c = assemble_info.assemble_candidato(candidato_info)
        service.cadastrar_candidato(c)
    }
    private void cadastrar_empresa(){
        Map<String, String> empresa_info = view.empresa_view.capturar_dados()
        Empresa m = assemble_info.assemble_empresa(empresa_info)
        service.cadastrar_empresa(m)
    }
    private void cadastrar_vaga(){
        Map<String, String> vaga_info = view.vaga_view.capturar_dados()
        Vaga v = assemble_info.assemble_vaga(vaga_info)
        service.cadastrar_vaga(v)
    }



    private void citar_ajuda(){
        view.send_message "É importante destacar que todos esses comandos são usados pela perspectiva de um ADM, por isso falta anonimidade.\n"

        view.send_message "Comandos read:"
        view.send_message "listar <candidatos / empresas / vagas / competencias / curtidas>\n"

        view.send_message "Comandos create:"
        view.send_message "cadastrar <candidato / empresa / vaga>"
        view.send_message "candidato.curtir"
        view.send_message "nota: competencias são criadas automaticassemble_modelente por demanda.\n"

        view.send_message "Comandos delete:"
        view.send_message "deletar <candidato / empresa / vaga / competencia>\n"

        view.send_message "Comandos update:"
        view.send_message "update <candidato / empresa / vaga / competencia>"
        view.send_message "empresa.curtir\n"

        view.send_message "Outros:"
        view.send_message "sair"
    }

    private void interpretar_input(String input){
        inputs[input]?.call()


//            case "deletar candidato":
//                service.deletar_candidato()
//                break
//
//            case "deletar empresa":
//                service.deletar_empresa()
//                break
//
//            case "deletar vaga":
//                service.deletar_vaga()
//                break
//
//            case "deletar competencia":
//                service.deletar_competencia()
//                break
//
//            case "update candidato":
//                service.update_candidato()
//                break
//
//            case "update vaga":
//                service.update_vaga()
//                break
//
//            case "update empresa":
//                service.update_empresa()
//                break
//
//            case "update competencia":
//                service.update_competencia()
//                break
//
//            case "candidato.curtir":
//                service.candidato_curtir()
//                break
//
//            case "empresa.curtir":
//                service.empresa_curtir()
//                break
//
//            case "sair":
//                return 1
//        }
//        return 0
    }

    void init() {
        assemble_model = new AssembleModel(service: service)

        view.send_message "Digite ? para ajuda\n"
        while (true){
            String resposta = view.get_input "@>"
            interpretar_input(resposta)
        }
    }
}
