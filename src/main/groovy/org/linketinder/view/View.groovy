package org.linketinder.view

abstract class View {
    abstract void send_message(String message)

    abstract String get_input(String message)
}
