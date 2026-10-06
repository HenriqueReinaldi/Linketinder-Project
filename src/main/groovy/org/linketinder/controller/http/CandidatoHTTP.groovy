package org.linketinder.controller.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import groovy.json.JsonBuilder
import groovy.transform.TupleConstructor
import org.linketinder.controller.CandidatoController
import org.linketinder.model.objetos.Candidato

import java.nio.charset.StandardCharsets

@TupleConstructor
class CandidatoHTTP extends ModelHTTP implements HttpHandler {
    CandidatoController controller

    void listar(HttpExchange exchange) throws IOException{
        if (exchange.requestURI.path != "/candidato"){
            responder(exchange, "Endpoint desconhecido", 404)
            return
        }

        List<String> candidatos = controller.get_lista_candidato().collect() { Candidato cand -> pegar_json(cand)}
        responder(exchange, candidatos.join(",\n"), 200)
    }



    @Override
    void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.requestMethod){
                case "GET" :
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
