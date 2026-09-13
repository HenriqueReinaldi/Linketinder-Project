package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Vaga
import org.linketinder.service.Service
import org.linketinder.view.View

@TupleConstructor()
class Controller {
    View view
    Service service
    AssembleModel assemble_model

    private final Map<String, Runnable> inputs = [
        "?": this.&citar_ajuda,

        "listar candidatos":   this.&listar_candidatos,
        "listar empresas":     this.&listar_empresas,
        "listar vagas":        this.&listar_vagas,
        "listar competencias": this.&listar_competencias,
        "listar curtidas":     this.&listar_curtidas,

        "cadastrar candidato": this.&cadastrar_candidato,
        "cadastrar empresa":   this.&cadastrar_empresa,
        "cadastrar vaga":      this.&cadastrar_vaga,

        "deletar candidato":   this.&deletar_candidato,
        "deletar empresa":     this.&deletar_empresa,
        "deletar vaga":        this.&deletar_vaga,
        "deletar competencia": this.&deletar_competencia,

        "update candidato":    this.&update_candidato,
        "update vaga":         this.&update_vaga,
        "update empresa":      this.&update_empresa,
        "update competencia":  this.&update_competencia,

        "candidato.curtir":    this.&candidato_curtir,
        "empresa.curtir":      this.&empresa_curtir
    ]

    private <Generico> void listar_generico(String tipo){
        service."get_lista_${tipo}s"().each {Generico gen ->
            view."${tipo}_view".exibir(gen)
        }
    }
    private <Generico> void cadastrar_generico(String tipo){
        Map<String, String> generico_info = view."${tipo}_view".capturar_dados()
        Generico c = assemble_model."assemble_${tipo}"(generico_info)
        service."cadastrar_${tipo}"(c)
    }
    private void deletar_generico(String tipo){
        try{
            service."deletar_${tipo}" get_generic_id()
        } catch(Exception ignored) {}
    }
    private <Generico> void update_generico(String tipo){
        try{
            Map<String, String> generico_info = view."tipo_${view}".capturar_dados()
            Generico c = assemble_model."assemble_${tipo}"(generico_info)
            c.id = get_generic_id()

            service."update_${tipo}"(c)
        } catch(Exception ignored) {}
    }

    private void listar_candidatos(){
        listar_generico "candidato"
    }
    private void listar_empresas(){
        listar_generico "empresa"
    }
    private void listar_vagas(){
        listar_generico "vaga"
    }
    private void listar_competencias(){
        listar_generico "competencia"
    }
    private void listar_curtidas(){
        listar_generico "curtida"
    }

    private void cadastrar_candidato(){
        cadastrar_generico"candidato"
    }
    private void cadastrar_empresa(){
        cadastrar_generico"empresa"
    }
    private void cadastrar_vaga(){
        cadastrar_generico "vaga"
    }

    private void deletar_candidato(){
        deletar_generico "candidato"
    }
    private void deletar_empresa(){
        deletar_generico "empresa"
    }
    private void deletar_vaga(){
        deletar_generico "vaga"
    }
    private void deletar_competencia(){
        deletar_generico "competencia"
    }

    private void update_candidato(){
        update_generico "candidato"
    }
    private void update_empresa(){
        update_generico "empresa"
    }
    private void update_vaga(){
        update_generico "vaga"
    }
    private void update_competencia(){
        try{
            Competencia c = new Competencia(
                tecnologia: view.get_input("tecnologia:"),
                id: get_generic_id()
            )
            service.update_competencia(c)
        } catch(Exception ignored) {}
    }

    private Curtida get_curtida(){
        Map<String, String> curtida_info = view.curtida_view.capturar_dados()
        retun assemble_model.assemble_curtida(curtida_info)
    }
    private void candidato_curtir(){
        Curtida c = get_curtida()
        service.candidato_curtir(c)
    }
    private void empresa_curtir(){
        Curtida c = get_curtida()
        service.empresa_curtir(c)
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

    private boolean interpretar_input(String input){
        if (input == "sair") return false
        inputs[input]?.call()
        return true
    }

    private int get_generic_id() throws NumberFormatException{
        Integer.parseInt(view.get_input("id:"))
    }

    void init() {
        assemble_model = new AssembleModel(service: service)

        view.send_message "Digite ? para ajuda\n"

        while (true){
            String resposta = view.get_input "@>"
            if (! interpretar_input(resposta)) break
        }
    }
}
