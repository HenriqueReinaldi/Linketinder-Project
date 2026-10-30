package org.linketinder.view.terminal

import org.linketinder.controller.*
import org.linketinder.model.objetos.*
import org.linketinder.view.PrefTree
import org.linketinder.view.View
import org.linketinder.view.ViewIO
import org.linketinder.view.shared.*

class Terminal extends View {
    ViewIO io

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

    RecrutadorView recrutador_view
    RecrutadorController recrutador_controller

    MatchView match_view
    MatchController match_controller

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
            case "recrutadores":
                List<Recrutador> recrutadores = recrutador_controller.get_lista_recrutador()
                recrutadores.forEach { Recrutador r -> recrutador_view.exibir(r) }
                break
            case "recrutador.curtidas":
                int recrutador_id = io.get_generic_id()
                List<Curtida> curtidas = recrutador_controller.get_lista_curtida_recrutador(recrutador_id)
                curtidas.forEach { Curtida c -> io.send_message(curtida_view.representacao_secreta(c)) }
                break
            case "matches":
                List<Match> matches = match_controller.get_lista_matches()
                matches.forEach { Match match -> io.send_message(match_view.representacao(match)) }
                break
        }
    }

    void cadastrar(String entidade) {
        ModelData md = new ModelData()

        switch (entidade) {
            case "candidato":
                md.data = candidato_view.capturar_dados()
                io.send_status candidato_controller.cadastrar_candidato(md)
                break
            case "empresa":
                md.data = empresa_view.capturar_dados()
                io.send_status empresa_controller.cadastrar_empresa(md)
                break
            case "vaga":
                md.data = vaga_view.capturar_dados()
                io.send_status vaga_controller.cadastrar_vaga(md)
                break
            case "recrutador":
                md.data = recrutador_view.capturar_dados()
                io.send_status recrutador_controller.cadastrar_recrutador(md)
                break
        }
    }

    void deletar(String entidade) {
        int id = io.get_generic_id()

        switch (entidade) {
            case "candidato":
                io.send_status candidato_controller.deletar_candidato(id)
                break
            case "empresa":
                io.send_status empresa_controller.deletar_empresa(id)
                break
            case "vaga":
                io.send_status vaga_controller.deletar_vaga(id)
                break
            case "competencia":
                io.send_status competencia_controller.deletar_competencia(id)
                break
            case "recrutador":
                io.send_status recrutador_controller.deletar_recrutador(id)
                break
        }
    }

    void update(String entidade) {
        ModelData md = new ModelData()

        switch (entidade) {
            case "candidato":
                md.data = candidato_view.capturar_dados(true)
                io.send_status candidato_controller.update_candidato(md)
                break
            case "empresa":
                md.data = empresa_view.capturar_dados(true)
                io.send_status empresa_controller.update_empresa(md)
                break
            case "vaga":
                md.data = vaga_view.capturar_dados(true)
                io.send_status vaga_controller.update_vaga(md)
                break
            case "competencia":
                md.data = competencia_view.capturar_dados(true)
                io.send_status competencia_controller.update_competencia(md)
                break
            case "recrutador":
                md.data = recrutador_view.capturar_dados(true)
                io.send_status recrutador_controller.update_recrutador(md)
                break
        }
    }

    void curtir_pela_perspectiva(String entidade) {
        ModelData md = new ModelData()

        switch (entidade) {
            case "candidato":
                md.data = curtida_view.capturar_dados()
                io.send_status curtida_controller.candidato_curtir(md)
                break
            case "empresa":
                md.data = curtida_view.capturar_dados()
                io.send_status curtida_controller.empresa_curtir(md)
                break
            case "recrutador":
                md.data = curtida_view.capturar_dados()
                int recrutador_id = io.get_generic_id()
                io.send_status curtida_controller.recrutador_curtir(md, recrutador_id)
                break
        }
    }

    void citar_ajuda() {
        io.send_message "É importante destacar que todos esses comandos são usados pela perspectiva de um ADM, por isso falta anonimidade.\n"

        io.send_message "Comandos read:"
        io.send_message "listar <candidatos / empresas / vagas / competencias / curtidas / recrutadores / matches>"
        io.send_message "listar recrutador.curtidas\n"

        io.send_message "Comandos create:"
        io.send_message "cadastrar <candidato / empresa / vaga / recrutador>"
        io.send_message "curtir como candidato\n"

        io.send_message "Comandos delete:"
        io.send_message "deletar <candidato / empresa / vaga / competencia / recrutador>\n"

        io.send_message "Comandos update:"
        io.send_message "update <candidato / empresa / vaga / competencia / recrutador>"
        io.send_message "curtir como <empresa/recrutador>\n"

        io.send_message "Outros:"
        io.send_message "sair"
    }

    boolean run() {
        String input = io.get_input "@>"

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

    Terminal(ControllerBundle controller_bundle, ViewBundle view_bundle, ViewIO view_io) {
        this.io = view_io

        this.competencia_view = view_bundle.competencia_view
        this.candidato_view = view_bundle.candidato_view
        this.curtida_view = view_bundle.curtida_view
        this.empresa_view = view_bundle.empresa_view
        this.vaga_view = view_bundle.vaga_view
        this.recrutador_view = view_bundle.recrutador_view
        this.match_view = view_bundle.match_view

        this.competencia_controller = controller_bundle.competencia_controller
        this.candidato_controller = controller_bundle.candidato_controller
        this.curtida_controller = controller_bundle.curtida_controller
        this.empresa_controller = controller_bundle.empresa_controller
        this.vaga_controller = controller_bundle.vaga_controller
        this.recrutador_controller = controller_bundle.recrutador_controller
        this.match_controller = controller_bundle.match_controller

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

        io.send_message "Digite ? para ajuda\n"
    }
}
