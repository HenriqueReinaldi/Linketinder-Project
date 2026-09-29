package org.linketinder.view.terminal

import org.linketinder.view.ViewIO

import static java.lang.Integer.parseInt

class TermIO extends ViewIO {
    static final Scanner scan = new Scanner(System.in)

    @Override
    void send_message(String message) {
        println message
    }

    @Override
    String get_input(String message) {
        print message
        String input = scan.nextLine()
        return input
    }

    @Override
    int get_generic_id() {
        try {
            return parseInt(get_input("id:"))
        } catch (Exception ignored) {
            return -1
        }
    }

    @Override
    void send_status(boolean sucesso) {
        if (sucesso){
            println "executado com sucesso!"
        }
        else {
            println "falha executando..."
        }
    }

}
