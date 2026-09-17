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

class TermView extends View {
    static final Scanner scan = new Scanner(System.in)
    final CandidatoViewTerm candidato_view = new CandidatoViewTerm()
    final EmpresaViewTerm empresa_view = new EmpresaViewTerm()
    final VagaViewTerm vaga_view = new VagaViewTerm()
    final CompetenciaViewTerm competencia_view = new CompetenciaViewTerm()
    final CurtidaViewTerm curtida_view = new CurtidaViewTerm()
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

    static void deletar(String entidade){
        switch (entidade){
            case "candidato":
                break
        }
    }

    static void update(String entidade){
        switch (entidade){
            case "candidato":
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

        comandos.inserir("candidato.", {})
        comandos.inserir("empresa.", {})

        send_message "Digite ? para ajuda\n"
    }

    boolean run() {
        String input = get_input "@>"

        if (input == "sair") return false

        Closure executor = comandos.buscar(input)
        executor(input.tokenize()[1] ?: "")

        return true
    }

    private int get_generic_id() throws NumberFormatException{
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
}
