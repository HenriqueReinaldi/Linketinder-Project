package org.linketinder.controller.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import groovy.transform.TupleConstructor
import org.linketinder.controller.CurtidaController
import org.linketinder.controller.ModelData
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

    void recrutador_curtir(HttpExchange exchange) throws IOException {
        def matcher = exchange.requestURI.path =~ /^\/curtida\/recrutador\/([^\/]+)$/
        if (!matcher.matches()) {
            responder(exchange, "Endpoint desconhecido", 404)
            return
        }

        int recrutador_id
        try {
            recrutador_id = Integer.parseInt(matcher[0][1])
        }
        catch (NumberFormatException ignored) {
            responder(exchange, "parametro recrutador_id invalido", 400)
            return
        }

        ModelData modelo = get_modeldata(exchange)
        if (!controller.recrutador_curtir(modelo, recrutador_id)) {
            responder(exchange, "falha registrando curtida do recrutador...", 400)
            return
        }

        responder(exchange, "curtida registrada pelo recrutador!", 200)
    }


    @Override
    void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.requestMethod) {
                case "GET":
                    listar(exchange)
                    break
                case "POST":
                    recrutador_curtir(exchange)
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
