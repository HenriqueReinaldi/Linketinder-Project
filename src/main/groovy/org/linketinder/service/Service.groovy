package org.linketinder.service

class Service{
    static <GENERICO> GENERICO executar_seguramente(GENERICO retorno_erro = null, Closure<GENERICO> acao){
        try{
            acao()
        }
        catch (Exception e){
            println e.message
            return retorno_erro
        }
    }
}
