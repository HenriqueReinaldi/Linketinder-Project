package org.linketinder.controller.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import groovy.transform.TupleConstructor
import org.linketinder.controller.CurtidaController
import org.linketinder.model.objetos.Curtida

@TupleConstructor
class CurtidaHTTP extends EntidadeHTTP implements HttpHandler {
    CurtidaController controller

    void listar(HttpExchange exchange) throws IOException {
        if (exchange.requestURI.path != "/curtida") {
            responder(exchange, "Endpoint desconhecido", 404)
            return
        }

        List<String> curtidas = controller.get_lista_curtida().collect() { Curtida curt -> pegar_json(curt) }
        responder(exchange, "[" + curtidas.join(",\n") + "]", 200)
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
