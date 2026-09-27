import { lista_candidatos } from "../persistencia/dados.js";
import { get_random_competencias } from "./dados.js";
import { Candidato } from "../model/candidato.js";
import { fetch_localstorage, update_localstorage } from "../persistencia/dados.js";


export function popular_candidatos(): void{
    for (let i: number = 0; i < 5; i++){
        lista_candidatos.push(
            new Candidato(
                `nome${i}`,
                `email${i}`,
                `estado${i}`,
                `CEP${i}`, 
                `descricao${i}`,
                i,
                `CPF${i}`,
                get_random_competencias(),
                `formacao${i}`
            )
        );
    }
}

export function cadastrar_candidato(nome: string, email: string, estado:string, CEP:string, descricao:string, idade:number, CPF:string, competencias:string[], formacao:string): boolean{
    fetch_localstorage();
    
    try{
        let cand: Candidato = new Candidato(
            nome, email, estado, CEP, descricao, idade, CPF, competencias, formacao
        )

        lista_candidatos.push(cand);
    } catch (_){
        return false;
    }
    
    update_localstorage();
    return true;
}
