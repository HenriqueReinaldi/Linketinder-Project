package org.linketinder.service

class Service {
    static <GENERICO> GENERICO executar_seguramente(GENERICO retorno_erro = null, Closure<GENERICO> acao) {
        try {
            return acao()
        }
        catch (Exception e) {
            e.printStackTrace()
            return retorno_erro
        }
    }
}
