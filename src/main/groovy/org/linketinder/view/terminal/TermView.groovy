package org.linketinder.view.terminal

import org.linketinder.controller.Controller
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Curtida
import org.linketinder.view.View

class TermView extends View {
    static final Scanner scan = new Scanner(System.in)

    final CandidatoViewTerm candidato_view = new CandidatoViewTerm()
    final EmpresaViewTerm empresa_view = new EmpresaViewTerm()
    final VagaViewTerm vaga_view = new VagaViewTerm()
    final CompetenciaViewTerm competencia_view = new CompetenciaViewTerm()
    final CurtidaViewTerm curtida_view = new CurtidaViewTerm()

    void send_message(String message){
        println message
    }
    String get_input(String message){
        print message
        String input = scan.nextLine()
        input
    }

    Controller controller

    private final Map<String, Closure> inputs = [
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



    private void citar_ajuda(){
        send_message "É importante destacar que todos esses comandos são usados pela perspectiva de um ADM, por isso falta anonimidade.\n"

        send_message "Comandos read:"
        send_message "listar <candidatos / empresas / vagas / competencias / curtidas>\n"

        send_message "Comandos create:"
        send_message "cadastrar <candidato / empresa / vaga>"
        send_message "candidato.curtir"
        send_message "nota: competencias são criadas automaticassemble_modelente por demanda.\n"

        send_message "Comandos delete:"
        send_message "deletar <candidato / empresa / vaga / competencia>\n"

        send_message "Comandos update:"
        send_message "update <candidato / empresa / vaga / competencia>"
        send_message "empresa.curtir\n"

        send_message "Outros:"
        send_message "sair"
    }



    private boolean interpretar_input(String input){
        if (input == "sair") return false
        inputs[input]?.call()
        return true
    }



    boolean run() {
        String resposta = get_input "@>"
        return interpretar_input(resposta)
    }


    private int get_generic_id() throws NumberFormatException{
        Integer.parseInt(get_input("id:"))
    }

}
