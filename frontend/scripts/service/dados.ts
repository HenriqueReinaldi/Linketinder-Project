import { Candidato } from "../model/candidato.js";
import { Empresa } from "../model/empresa.js";

export var lista_candidatos: Candidato[] = [];
export var lista_empresas: Empresa[] = [];


function popular_candidatos(): void{
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
                []
            )
        );
    }
}
function popular_empresas(): void{
    for (let i: number = 0; i < 5; i++){
        lista_empresas.push(
            new Empresa(
                `nome${i}`,
                `email${i}`,
                `estado${i}`,
                `CEP${i}`, 
                `descricao${i}`,
                `pais${i}`,
                `CNPJ${i}`,
                []
            )
        );
    }
}

export function dados_init(): void{
    

    popular_candidatos();
    popular_empresas();
}