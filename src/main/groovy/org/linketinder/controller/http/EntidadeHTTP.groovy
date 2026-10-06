package org.linketinder.controller.http

import com.sun.net.httpserver.HttpExchange
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.linketinder.controller.ModelData

import java.nio.charset.StandardCharsets

abstract class EntidadeHTTP {
    protected final static <GENERICO> String pegar_json(GENERICO objeto) {
        if (objeto == null) return ""
        return new JsonBuilder(objeto).toPrettyString()
    }

    protected final static void responder(HttpExchange exchange, String mensagem, int status) {
        if (exchange == null) return
        if (mensagem == null) mensagem = ""

        exchange.responseHeaders.set("Content-Type", "application/json; charset=UTF-8")
        byte[] bytes = mensagem.getBytes(StandardCharsets.UTF_8)

        exchange.sendResponseHeaders(status, bytes.length)
        exchange.getResponseBody().withCloseable { OutputStream os -> os.write(bytes) }
    }

    protected final static ModelData get_modeldata(HttpExchange exchange) {
        String corpo = exchange.requestBody.text
        Map<String, String> parametros = new JsonSlurper().parseText(corpo) as Map<String, String>

        ModelData md = new ModelData()
        md.data = parametros
        return md
    }
}
