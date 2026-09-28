import { lista_empresas, lista_vagas } from "../persistencia/dados.js";
import { Vaga } from "../model/vaga.js";
import { get_random_competencias } from "./dados.js";

export function popular_vagas(): void{
    //necessita de que lista_empresas nao esteja vazia.
    if (lista_empresas.length == 0) return;

    for (let i: number = 0; i < 10; i++){
        lista_vagas.push(
            new Vaga(
                `nome${i}`,
                67*(i+1),
                `descricao${i}`,
                lista_empresas[i%5],
                get_random_competencias()

            )
        );
    }
}
