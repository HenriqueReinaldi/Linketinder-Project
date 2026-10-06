package org.linketinder.controller.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import groovy.transform.TupleConstructor
import org.linketinder.controller.CompetenciaController
import org.linketinder.model.objetos.Competencia

@TupleConstructor
class CompetenciaHTTP extends EntidadeHTTP implements HttpHandler {
    CompetenciaController controller

    void listar(HttpExchange exchange) throws IOException {
        if (exchange.requestURI.path != "/competencia") {
            responder(exchange, "Endpoint desconhecido", 404)
            return
        }

        List<String> competencias = controller.get_lista_competencia().collect() { Competencia comp -> pegar_json(comp) }
        responder(exchange, "[" + competencias.join(",\n") + "]", 200)
    }


    @Override
    void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.requestMethod) {
                case "GET":
                    listar(exchange)
                    break
                default:
                    responder(exchange, "Metodo http Proibido", 405)
            }

        }
        catch (Exception e) {
            e.printStackTrace()
            responder(exchange, "Deu erro...", 500)
        }
    }
}
