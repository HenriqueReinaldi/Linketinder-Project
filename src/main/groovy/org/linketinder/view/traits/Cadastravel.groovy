package org.linketinder.view.traits

trait Cadastravel<GENERICO> {
    abstract Map<String, String> capturar_dados(boolean com_id = false);
}