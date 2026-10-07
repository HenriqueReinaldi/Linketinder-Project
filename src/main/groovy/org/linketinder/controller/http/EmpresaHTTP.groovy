package org.linketinder.controller.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import groovy.transform.TupleConstructor
import org.linketinder.controller.EmpresaController
import org.linketinder.controller.ModelData
import org.linketinder.model.objetos.Empresa

@TupleConstructor
class EmpresaHTTP extends EntidadeHTTP implements HttpHandler {
    EmpresaController controller

    void listar(HttpExchange exchange) throws IOException {
        if (exchange.requestURI.path != "/empresa") {
            responder(exchange, "Endpoint desconhecido", 404)
            return
        }

        List<String> empresas = controller.get_lista_empresa().collect() { Empresa emp -> pegar_json(emp) }
        responder(exchange, "[" + empresas.join(",\n") + "]", 200)
    }

    void cadastrar(HttpExchange exchange) throws IOException {
        if (exchange.requestURI.path != "/empresa") {
            responder(exchange, "Endpoint desconhecido", 404)
            return
        }
        
        ModelData modelo = get_modeldata(exchange)
        println modelo.data

        if (!controller.cadastrar_empresa(modelo)){
            responder(exchange, "falha cadastrando empresa...", 400)
            return
        }

        responder(exchange, "empresa cadastrado!", 201)
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
