Para usar HTTPSERVER:
    
1. defina uma variavel HttpServer e instancie:

        HttpServer http 
                                                        host      porta
                                                         |          |
                                                         V          V                                               
        http = HttpServer.create(new InetSocketAddress("localhost", 6767), 0)
    
<br> 

2. Para criar endpoints, use:

        http.createContext("/") { HttpExchange exchange -> }
        
    ou, crie uma classse que implementa a interface HttpHandler:  
    
    
        class handler implements HttpHandler {
            @Override
            void handle(HttpExchange exchange) throws IOException {

            }
        }
    <br>

        http.createContext("/endpoint", new handler())
    
<br> 

3. Use o seguinte comando para iniciar o servidor:

        http.start()

    3.1 Para parar o servidor, use:
    <br>

        http.stop(0) <- argumento define quanto tempo(segundos) esperar antes de fechar o servidor.

<br> 


4. Utilizando HttpExchange (exchange):

    Pegando path e método HTTP da exchange:

        exchange.requestMethod
        exchange.requestURI.path

    Definindo e enviando headers da reposta:

                                      CHAVE           VALOR
                                        |               |
                                        V               V
        exchange.responseHeaders.set("Content-Type", "application/json; charset=UTF-8")

    Pegandos os byts da mensagem a ser enviada:

        String mensagem = "CONTEUDO RESPOSTA"
        byte[] bytes = mensagem.getBytes(StandardCharsets.UTF_8)

    Enviando headers e tamanho da mensagem:

                             código status HTTP
                                      |
                                      V    
        exchange.sendResponseHeaders(200, bytes.length)

    Envinado os bytes da mensagm:

        exchange.getResponseBody().withCloseable { OutputStream os -> os.write(bytes) }
<br>

<hr>
<br>

5. Exemplo:

        HttpServer http 
        http = HttpServer.create(new InetSocketAddress("localhost", 6767), 0)

        http.createContext("/") { HttpExchange exchange ->
            exchange.responseHeaders.set("Content-Type", "text/plain; charset=UTF-8")
            byte[] bytes = "Linketinder ONLINE!".getBytes(StandardCharsets.UTF_8)
            exchange.sendResponseHeaders(200, bytes.length)
            exchange.getResponseBody().withCloseable { OutputStream os -> os.write(bytes) }
        }

        http.start()

