package org.linketinder.controller.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import groovy.transform.TupleConstructor
import org.linketinder.controller.CandidatoController
import org.linketinder.controller.MatchController
import org.linketinder.controller.ModelData
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Match

import java.util.regex.Matcher


@TupleConstructor
class CandidatoHTTP extends EntidadeHTTP implements HttpHandler {
    CandidatoController controller
    MatchController match_controller

    void listar(HttpExchange exchange) throws IOException {
        if (exchange.requestURI.path == "/candidato") {
            List<String> candidatos = controller.get_lista_candidato().collect() { Candidato cand -> pegar_json(cand) }
            responder(exchange, "[" + candidatos.join(",\n") + "]", 200)
            return
        }

        Matcher matcher = exchange.requestURI.path =~ /^\/candidato\/([^\/]+)\/match$/
        if (matcher.matches()) {
            try {
                List<Match> matches = match_controller.get_lista_matches_por_candidato(Integer.parseInt(matcher[0][1])).collect() { Match match -> pegar_json(match) }
                responder(exchange, "[" + matches.join(",\n") + "]", 200)
            }
            catch (Exception e) {
                e.printStackTrace()
                responder(exchange, "parametro id invalido", 400)
            }
            return
        }

        responder(exchange, "Endpoint desconhecido", 404)
    }

    void cadastrar(HttpExchange exchange) throws IOException {
        if (exchange.requestURI.path != "/candidato") {
            responder(exchange, "Endpoint desconhecido", 404)
            return
        }

        ModelData modelo = get_modeldata(exchange)

        if (!controller.cadastrar_candidato(modelo)) {
            responder(exchange, "falha cadastrando candidato...", 400)
            return
        }

        responder(exchange, "candidato cadastrado!", 201)
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
            e.printStackTrace()
            responder(exchange, "Deu erro...", 500)
        }
    }

}
