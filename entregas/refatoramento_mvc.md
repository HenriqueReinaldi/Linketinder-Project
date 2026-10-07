- projeto já seguia a estrutura MVC +  S, DAO
- contudo, falhava o ciclo de comunicacao: view -> controller -> service existia, mas service -> controller -> view estava incompleto.
        ... foi portanto adicionado essa comunicação de "respostas"
- refatoramentos simples também de organização de código