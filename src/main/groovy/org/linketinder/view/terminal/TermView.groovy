package org.linketinder.view.terminal

import org.linketinder.controller.CandidatoController
import org.linketinder.controller.CompetenciaController
import org.linketinder.controller.Controller
import org.linketinder.controller.ControllerBundle
import org.linketinder.controller.CurtidaController
import org.linketinder.controller.EmpresaController
import org.linketinder.controller.ModelData
import org.linketinder.controller.VagaController
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
import org.linketinder.view.shared.ViewBundle

import static java.lang.Integer.parseInt

class TermView extends View {
    static final Scanner scan = new Scanner(System.in)

    CompetenciaView competencia_view
    CompetenciaController competencia_controller

    CandidatoView candidato_view
    CandidatoController candidato_controller

    CurtidaView curtida_view
    CurtidaController curtida_controller

    EmpresaView empresa_view
    EmpresaController empresa_controller

    VagaView vaga_view
    VagaController vaga_controller

    PrefTree comandos


    void listar(String entidade) {
        switch (entidade) {
            case "candidatos":
                List<Candidato> candidatos = candidato_controller.get_lista_candidato()
                candidatos.forEach { Candidato c -> candidato_view.exibir(c) }
                break
            case "empresas":
                List<Empresa> empresas = empresa_controller.get_lista_empresa()
                empresas.forEach { Empresa m -> empresa_view.exibir(m) }
                break
            case "vagas":
                List<Vaga> vagas = vaga_controller.get_lista_vaga()
                vagas.forEach { Vaga v -> vaga_view.exibir(v) }
                break
            case "curtidas":
                List<Curtida> curtidas = curtida_controller.get_lista_curtida()
                curtidas.forEach { Curtida c -> curtida_view.exibir(c) }
                break
            case "competencias":
                List<Competencia> competencias = competencia_controller.get_lista_competencia()
                competencias.forEach { Competencia c -> competencia_view.exibir(c) }
                break
        }
    }

    void cadastrar(String entidade) {
        ModelData md = new ModelData()

        switch (entidade) {
            case "candidato":
                md.data = candidato_view.capturar_dados()
                candidato_controller.cadastrar_candidato(md)
                break
            case "empresa":
                md.data = empresa_view.capturar_dados()
                empresa_controller.cadastrar_empresa(md)
                break
            case "vaga":
                md.data = vaga_view.capturar_dados()
                vaga_controller.cadastrar_vaga(md)
                break
        }
    }

    void deletar(String entidade) {
        int id = get_generic_id()

        switch (entidade) {
            case "candidato":
                candidato_controller.deletar_candidato(id)
                break
            case "empresa":
                empresa_controller.deletar_empresa(id)
                break
            case "vaga":
                vaga_controller.deletar_vaga(id)
                break
            case "competencia":
                competencia_controller.deletar_competencia(id)
                break
        }
    }

    void update(String entidade) {
        ModelData md = new ModelData()

        switch (entidade) {
            case "candidato":
                md.data = candidato_view.capturar_dados(true)
                candidato_controller.update_candidato(md)
                break
            case "empresa":
                md.data = empresa_view.capturar_dados(true)
                empresa_controller.update_empresa(md)
                break
            case "vaga":
                md.data = vaga_view.capturar_dados(true)
                vaga_controller.update_vaga(md)
                break
            case "competencia":
                md.data = competencia_view.capturar_dados(true)
                competencia_controller.update_competencia(md)
                break
        }
    }

    void curtir_pela_perspectiva(String entidade) {
        ModelData md = new ModelData()

        switch (entidade) {
            case "candidato":
                md.data = curtida_view.capturar_dados()
                candidato_controller.candidato_curtir(md)
                break
            case "empresa":
                md.data = curtida_view.capturar_dados()
                empresa_controller.empresa_curtir(md)
                break
        }
    }


    void send_message(String message) {
        println message
    }

    String get_input(String message) {
        print message
        String input = scan.nextLine()
        return input
    }

    void citar_ajuda() {
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

    int get_generic_id() {
        try {
            return parseInt(get_input("id:"))
        } catch (Exception ignored) {
            return -1
        }
    }

    boolean run() {
        String input = get_input "@>"

        if (input == "sair") return false

        Closure executor = comandos.buscar(input)
        List<String> args = input.tokenize()

        if (args.size() - 1 < 0) {
            executor("")
        } else {
            executor(args[args.size() - 1] ?: "")
        }

        return true
    }

    TermView(ControllerBundle controller_bundle, ViewBundle view_bundle) {
        this.competencia_view = view_bundle.competencia_view
        competencia_view.view = this
        this.candidato_view = view_bundle.candidato_view
        candidato_view.view = this
        this.curtida_view = view_bundle.curtida_view
        curtida_view.view = this
        this.empresa_view = view_bundle.empresa_view
        empresa_view.view = this
        this.vaga_view = view_bundle.vaga_view
        vaga_view.view = this

        this.competencia_controller = controller_bundle.competencia_controller
        this.candidato_controller = controller_bundle.candidato_controller
        this.curtida_controller = controller_bundle.curtida_controller
        this.empresa_controller = controller_bundle.empresa_controller
        this.vaga_controller = controller_bundle.vaga_controller


        comandos = new PrefTree()
        Map<String, Closure> lista_comandos = [
                "?"          : { citar_ajuda() },
                "listar"     : this.&listar,
                "cadastrar"  : this.&cadastrar,
                "deletar"    : this.&deletar,
                "update"     : this.&update,
                "curtir como": this.&curtir_pela_perspectiva
        ]

        lista_comandos.each { String prefixo, Closure comando ->
            comandos.inserir(prefixo, comando)
        }

        send_message "Digite ? para ajuda\n"
    }
}
