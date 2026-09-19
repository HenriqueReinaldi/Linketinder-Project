package org.linketinder.view.terminal

import org.linketinder.controller.Controller
import org.linketinder.controller.ModelData
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Vaga
import org.linketinder.view.PrefTree
import org.linketinder.view.View
import org.linketinder.view.shared.CandidatoView
import org.linketinder.view.shared.CompetenciaView
import org.linketinder.view.shared.CurtidaView
import org.linketinder.view.shared.EmpresaView
import org.linketinder.view.shared.VagaView

class TermView extends View {
    static final Scanner scan = new Scanner(System.in)

    final CandidatoView candidato_view = new CandidatoView(this)
    final EmpresaView empresa_view = new EmpresaView(this)
    final VagaView vaga_view = new VagaView(this)
    final CompetenciaView competencia_view = new CompetenciaView(this)
    final CurtidaView curtida_view = new CurtidaView(this)

    Controller controller
    PrefTree comandos = new PrefTree()

    void listar(String entidade){
        switch (entidade){
            case "candidatos":
                List<Candidato> candidatos = controller.get_lista_candidato()
                candidatos.forEach {Candidato c -> candidato_view.exibir(c)}
                break
            case "empresas":
                List<Empresa> empresas = controller.get_lista_empresa()
                empresas.forEach {Empresa m -> empresa_view.exibir(m)}
                break
            case "vagas":
                List<Vaga> vagas = controller.get_lista_vaga()
                vagas.forEach {Vaga v -> vaga_view.exibir(v)}
                break
            case "curtidas":
                List<Curtida> curtidas = controller.get_lista_curtida()
                curtidas.forEach {Curtida c -> curtida_view.exibir(c)}
                break
            case "competencias":
                List<Competencia> competencias = controller.get_lista_competencia()
                competencias.forEach {Competencia c -> competencia_view.exibir(c)}
                break
        }
    }

    void cadastrar(String entidade){
        ModelData md = new ModelData()

        switch (entidade){
            case "candidato":
                md.data = candidato_view.capturar_dados()
                controller.cadastrar_candidato(md)
                break
            case "empresa":
                md.data = empresa_view.capturar_dados()
                controller.cadastrar_empresa(md)
                break
            case "vaga":
                md.data = vaga_view.capturar_dados()
                controller.cadastrar_vaga(md)
                break
        }
    }

    void deletar(String entidade){
        int id = get_generic_id()
        switch (entidade){
            case "candidato":
                controller.deletar_candidato(id)
                break
            case "empresa":
                controller.deletar_empresa(id)
                break
            case "vaga":
                controller.deletar_vaga(id)
                break
            case "competencia":
                controller.deletar_competencia(id)
                break
        }
    }

    void update(String entidade){
        ModelData md = new ModelData()

        switch (entidade){
            case "candidato":
                md.data = candidato_view.capturar_dados(true)
                controller.update_candidato(md)
                break
            case "empresa":
                md.data = empresa_view.capturar_dados(true)
                controller.update_empresa(md)
                break
            case "vaga":
                md.data = vaga_view.capturar_dados(true)
                controller.update_vaga(md)
                break
            case "competencia":
                md.data = competencia_view.capturar_dados(true)
                controller.update_competencia(md)
                break
        }
    }

    void curtir_pela_perspectiva(String entidade){
        ModelData md = new ModelData()

        switch (entidade){
            case "candidato":
                md.data = curtida_view.capturar_dados()
                controller.candidato_curtir(md)
                break
            case "empresa":
                md.data = curtida_view.capturar_dados()
                controller.empresa_curtir(md)
                break
        }
    }

    TermView(Controller controller){
        this.controller = controller

        comandos.inserir("?", {citar_ajuda()})
        comandos.inserir("listar ", this.&listar)
        comandos.inserir("cadastrar ", this.&cadastrar)
        comandos.inserir("deletar ", this.&deletar)
        comandos.inserir("update ", this.&update)

        comandos.inserir("curtir como ", this.&curtir_pela_perspectiva)
        comandos.inserir("curtir como ", this.&curtir_pela_perspectiva)

        send_message "Digite ? para ajuda\n"
    }

    boolean run() {
        String input = get_input "@>"

        if (input == "sair") return false

        Closure executor = comandos.buscar(input)
        List<String> args = input.tokenize()

        executor(args[args.size()-1] ?: "")

        return true
    }

    int get_generic_id() throws NumberFormatException{
        Integer.parseInt(get_input("id:"))
    }

    void send_message(String message){
        println message
    }
    String get_input(String message){
        print message
        String input = scan.nextLine()
        input
    }

    void citar_ajuda(){
        send_message "É importante destacar que todos esses comandos são usados pela perspectiva de um ADM, por isso falta anonimidade.\n"

        send_message "Comandos read:"
        send_message "listar <candidatos / empresas / vagas / competencias / curtidas>\n"

        send_message "Comandos create:"
        send_message "cadastrar <candidato / empresa / vaga>"
        send_message "curtir como candidato"
        send_message "nota: competencias são criadas automaticassemble_modelente por demanda.\n"

        send_message "Comandos delete:"
        send_message "deletar <candidato / empresa / vaga / competencia>\n"

        send_message "Comandos update:"
        send_message "update <candidato / empresa / vaga / competencia>"
        send_message "curtir como empresa\n"

        send_message "Outros:"
        send_message "sair"
    }
}
