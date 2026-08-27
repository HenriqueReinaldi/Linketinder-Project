import { Pessoa } from "./pessoa.js"

export class Candidato extends Pessoa{

    constructor (
        nome: string,
        email:string,
        estado:string,
        CEP:string,
        descricao:string,

        public idade: number,
        public CPF: string,
        public competencias: string[],
        public formacao: string
        
    ){ super(nome, email, estado, CEP, descricao) }
}