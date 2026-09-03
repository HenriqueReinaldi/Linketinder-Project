package org.linketinder.view.terminal

class TermView {
    static final Scanner scan = new Scanner(System.in)

    final CandidatoViewTerm candidato_view = new CandidatoViewTerm()
    final EmpresaViewTerm empresa_view = new EmpresaViewTerm()
    final VagaViewTerm vaga_view = new VagaViewTerm()
    final CompetenciaViewTerm competencia_view = new CompetenciaViewTerm()

    static void send_message(String message){
        println message
    }

    static String get_input(String message){
        print message
        String input = scan.nextLine()
        input
    }
}
