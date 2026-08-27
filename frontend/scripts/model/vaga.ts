import { Empresa } from "./empresa.js";

export class Vaga{
    constructor(
        public nome: string,
        public salario:number,
        public descricao:string,
        public empresa: Empresa
    ){}
}