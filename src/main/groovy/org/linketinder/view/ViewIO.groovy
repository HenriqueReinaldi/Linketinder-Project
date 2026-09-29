package org.linketinder.view

abstract class ViewIO {
    abstract void send_status(boolean sucesso)

    abstract void send_message(String message)

    abstract String get_input(String message)

    abstract int get_generic_id()
}
