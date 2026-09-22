package org.linketinder.view

abstract class ViewIO {
    static final Scanner scan

    abstract void send_message(String message)
    abstract String get_input(String message)
    abstract int get_generic_id()
}
