package org.linketinder.view.terminal

class TermView {
    static final Scanner scan = new Scanner(System.in)

    final CandidatoViewTerm candidatoView = new CandidatoViewTerm()
    final EmpresaViewTerm empresaView = new EmpresaViewTerm()

    static void send_message(String message){
        println message
    }

    static String get_input(String message){
        print message
        String input = scan.nextLine()
        input
    }
}
