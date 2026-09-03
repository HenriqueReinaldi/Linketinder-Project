### Sobre:

Esté é uma aplicação que mistura o Linkedin com o Tinder, para facilitar o processo de recrutamento.
## BACKEND:

### Para executar:
    1. Crie um banco de dados PostgreSQL nomeado 'linketinder'
    2. Utilize o script de criação em banco_de_dados/entrega.sql para criar as tabelas e popular o banco.
    3. Execute na raiz do projeto o seguinte comando:

        ./gradlew run -q --console=plain

    ALTERNATIVAMENTE

    4. Abra o projeto no Intellij IDEA.
    5 Vá para src/main/groovy/org/linketinder/Main.groovy e clique no botão RUN do método main()

## FRONTEND:

### Para executar:

    1. Vá para frontend/
    2. Execute o seguinte comando:

        npm run this

    3. Em um navegador, vá para o endereço "network" fornecido:

        ┌───────────────────────────────────────────┐
        │                                           │
        │   Serving!                                │
        │                                           │
        │   - Local:    http://localhost:3000       │
        │   - Network:  http://192.168.0.227:3000   │  <- esse endereço
        │                                           │
        │   Copied local address to clipboard!      │
        │                                           │
        └───────────────────────────────────────────┘
         
## BANCO DE DADOS:

Para juntar o novo banco de dados com o aplicativo, tive que re-estruturar algumas (várias) coisas tanto no código quanto no modelo do banco. Como exemplo, movi "competencias desejadas" da classe Empresa para uma nova classe "Vaga".<br>
Antes dessa junção, tentei também refatorar o projeto com a arquitetura MVCS (minha primeira vez usando ela).<br>
Uma ferramenta que serviu grande auxílio foi o próprio Intellij, que permitiu uma conexão com o banco para fazer queries de forma rápida e adicionar IntelliSense a strings SQL.


<br>
<br>
<br>

### Informações pertinentes:

1. As classes sugeridas estão em src/main/groovy/org/linketinder/classes
2. src/main/groovy/org/linketinder/dados contém "Dados.groovy", que armazena os usuários e "Random.groovy", que pode ser usado para criar usuários aleatóriamente
3. src/main/groovy/org/linketinder/terminal contém a classe que serve como interface
4. Backend feito com groovy e build tool gradle
5. Frontend feito com Typescript + HTML, com o uso da biblioteca [chart.js](https://www.chartjs.org/)
6. Frontend ainda é completamente independente do backend.
7. Banco de dados feito com PostgreSQL
8. Modelo DER feito com [dbdiagram](https://dbdiagram.io/home)


### Diagrama Entidade-Relacionamento:

![DER](banco_de_dados/image.png)