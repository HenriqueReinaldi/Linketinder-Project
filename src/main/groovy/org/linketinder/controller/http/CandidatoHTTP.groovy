package org.linketinder.controller.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import groovy.json.JsonBuilder
import groovy.transform.TupleConstructor
import org.linketinder.controller.CandidatoController
import org.linketinder.model.objetos.Candidato

import java.nio.charset.StandardCharsets

@TupleConstructor
class CandidatoHTTP implements HttpHandler {
    CandidatoController controller

    static <GENERICO> String pegar_json(GENERICO objeto){
        if (objeto == null) return ""
        return new JsonBuilder(objeto).toPrettyString()
    }

    @Override
    void handle(HttpExchange exchange) throws IOException {
        //println exchange.requestURI.path

        try {
            switch (exchange.requestMethod) {
                case "GET":
                    List<String> candidatos = controller.get_lista_candidato().collect() { Candidato cand -> pegar_json(cand)}
                    responder(exchange, candidatos.join(",\n"), 200)
                    break
                default:
                    responder(exchange, "Metodo http Proibido", 405)
            }
        }
        catch (Exception ignored) {
            responder(exchange, "Deu erro...", 500)
        }
    }

    private static void responder(HttpExchange exchange, String mensagem, int status) {
        if (mensagem == null) mensagem = ""

        exchange.responseHeaders.set("Content-Type", "text/plain; charset=UTF-8")
        byte[] bytes = mensagem.getBytes(StandardCharsets.UTF_8)

        exchange.sendResponseHeaders(status, bytes.length)
        exchange.getResponseBody().withCloseable { OutputStream os -> os.write(bytes) }
    }

}
