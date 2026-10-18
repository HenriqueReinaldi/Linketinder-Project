package org.linketinder.controller.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import groovy.transform.TupleConstructor
import org.linketinder.controller.ModelData
import org.linketinder.controller.RecrutadorController
import org.linketinder.model.objetos.Recrutador

@TupleConstructor
class RecrutadorHTTP extends EntidadeHTTP implements HttpHandler {
    RecrutadorController controller

    void listar(HttpExchange exchange) throws IOException {
        if (exchange.requestURI.path != "/recrutador") {
            responder(exchange, "Endpoint desconhecido", 404)
            return
        }

        List<String> recrutadores = controller.get_lista_recrutador().collect() { Recrutador recrutador -> pegar_json(recrutador) }
        responder(exchange, "[" + recrutadores.join(",\n") + "]", 200)
    }

    void cadastrar(HttpExchange exchange) throws IOException {
        if (exchange.requestURI.path != "/recrutador") {
            responder(exchange, "Endpoint desconhecido", 404)
            return
        }

        ModelData modelo = get_modeldata(exchange)

        if (!controller.cadastrar_recrutador(modelo)) {
            responder(exchange, "falha cadastrando recrutador...", 400)
            return
        }

        responder(exchange, "recrutador cadastrado!", 201)
    }

    @Override
    void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.requestMethod) {
                case "GET":
                    listar(exchange)
                    break
                case "POST":
                    cadastrar(exchange)
                    break
                default:
                    responder(exchange, "Metodo http Proibido", 405)
            }
        }
        catch (Exception e) {
            responder(exchange, "Deu erro...", 500)
        }
    }
}
