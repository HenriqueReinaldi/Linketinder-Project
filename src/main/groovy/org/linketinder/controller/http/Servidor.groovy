package org.linketinder.controller.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import org.linketinder.DAO.CurtidaDAO
import org.linketinder.controller.ControllerBundle
import org.linketinder.model.objetos.Empresa

import java.nio.charset.StandardCharsets

class Servidor {
    ControllerBundle controllers
    HttpServer http
    String host = "localhost"
    int port = 3040

    void preparar_endpoints(){
        http.createContext("/") { HttpExchange exchange ->
            exchange.responseHeaders.set("Content-Type", "text/plain; charset=UTF-8")
            byte[] bytes = "Linketinder ONLINE!".getBytes(StandardCharsets.UTF_8)
            exchange.sendResponseHeaders(200, bytes.length)
            exchange.getResponseBody().withCloseable { OutputStream os -> os.write(bytes) }
        }

        http.createContext("/candidato", new CandidatoHTTP(controllers.candidato_controller))
        http.createContext("/empresa", new EmpresaHTTP(controllers.empresa_controller))
        http.createContext("/vaga", new VagaHTTP(controllers.vaga_controller))
        http.createContext("/competencia", new CompetenciaHTTP(controllers.competencia_controller))
        http.createContext("/curtida", new CurtidaHTTP(controllers.curtida_controller))

    }

    void ligar(){
        println "http://${host}:${port}/"
        http.start()
    }
    void fechar(){
        http.stop(0)
    }

    Servidor(ControllerBundle controllers){
        this.controllers = controllers

        http = HttpServer.create(new InetSocketAddress(this.host, this.port), 0)
        preparar_endpoints()
    }
}
