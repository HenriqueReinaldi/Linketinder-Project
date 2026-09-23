package org.linketinder.view.terminal

import org.linketinder.view.ViewIO

import static java.lang.Integer.parseInt

class TermIO extends ViewIO {
    static final Scanner scan = new Scanner(System.in)

    void send_message(String message) {
        println message
    }

    String get_input(String message) {
        print message
        String input = scan.nextLine()
        return input
    }

    int get_generic_id() {
        try {
            return parseInt(get_input("id:"))
        } catch (Exception ignored) {
            return -1
        }
    }
}
