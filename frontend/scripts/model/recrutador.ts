import { Empresa } from "./empresa.js";

export class Recrutador {
    constructor(
        public id: number,
        public nome: string,
        public descricao: string,
        public e_mail: string,
        public CPF: string,
        public data_nascimento: string,
        public senha: string,
        public empresa: Empresa,
        public nicho: string
    ) {}
}