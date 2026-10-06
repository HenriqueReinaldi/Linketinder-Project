package org.linketinder.controller.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import groovy.transform.TupleConstructor
import org.linketinder.controller.VagaController
import org.linketinder.controller.ModelData
import org.linketinder.model.objetos.Vaga

@TupleConstructor
class VagaHTTP extends EntidadeHTTP implements HttpHandler {
    VagaController controller

    void listar(HttpExchange exchange) throws IOException {
        if (exchange.requestURI.path != "/vaga") {
            responder(exchange, "Endpoint desconhecido", 404)
            return
        }

        List<String> vagas = controller.get_lista_vaga().collect() { Vaga vag -> pegar_json(vag) }
        responder(exchange, "[" + vagas.join(",\n") + "]", 200)
    }

    void cadastrar(HttpExchange exchange) throws IOException {
        if (exchange.requestURI.path != "/vaga") {
            responder(exchange, "Endpoint desconhecido", 404)
            return
        }

        ModelData modelo = get_modeldata(exchange)

        if (!controller.cadastrar_vaga(modelo)) {
            responder(exchange, "falha cadastrando vaga...", 400)
            return
        }

        responder(exchange, "vaga cadastrado!", 201)
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
