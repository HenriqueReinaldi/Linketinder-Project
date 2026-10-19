package org.linketinder.controller.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import groovy.transform.TupleConstructor
import org.linketinder.controller.ModelData
import org.linketinder.controller.RecrutadorController
import org.linketinder.model.objetos.Curtida
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

    void listar_curtidas(HttpExchange exchange) throws IOException {
        def matcher = exchange.requestURI.path =~ /^\/recrutador\/([^\/]+)\/curtidas$/
        if (!matcher.matches()) {
            responder(exchange, "Endpoint desconhecido", 404)
            return
        }

        try {
            List<String> curtidas = controller.get_lista_curtida_recrutador(Integer.parseInt(matcher[0][1])).collect { Curtida curtida ->
                pegar_json([
                        candidato_descricao: curtida.candidato?.descricao,
                        vaga_nome: curtida.vaga?.nome,
                        match: curtida.empresa_curtiu
                ])
            }
            responder(exchange, "[" + curtidas.join(",\n") + "]", 200)
        }
        catch (NumberFormatException ignored) {
            responder(exchange, "parametro id invalido", 400)
        }
    }

    @Override
    void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.requestMethod) {
                case "GET":
                    if (exchange.requestURI.path.startsWith("/recrutador/") && exchange.requestURI.path.endsWith("/curtidas")) {
                        listar_curtidas(exchange)
                    } else {
                        listar(exchange)
                    }
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
