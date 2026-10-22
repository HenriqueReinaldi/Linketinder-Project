import { Candidato } from "./candidato.js";
import { Vaga } from "./vaga.js";

export class Curtida {
    constructor(
        public candidato: Candidato,
        public vaga: Vaga,
        public empresa_curtiu: boolean = false
    ) {}
}