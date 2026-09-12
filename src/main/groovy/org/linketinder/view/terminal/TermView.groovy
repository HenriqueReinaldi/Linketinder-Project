package org.linketinder.view.terminal

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
}
