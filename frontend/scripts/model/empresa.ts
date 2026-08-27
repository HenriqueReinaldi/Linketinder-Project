import { Pessoa } from "./pessoa.js"

export class Empresa extends Pessoa{
    constructor (
        nome: string,
        email:string,
        estado:string,
        CEP:string,
        descricao:string,

        public pais: string,
        public CNPJ: string,
        public competencias_desejadas: string[]
    ){ super(nome, email, estado, CEP, descricao) }
}

// ainda nao sei como vaga se conecta com empresas, entao vou deixar separado por enquanto pra evitar retrabalho