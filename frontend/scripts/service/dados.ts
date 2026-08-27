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

function update_localstorage(): void {
    localStorage.setItem("lista_candidatos", JSON.stringify(lista_candidatos));
    localStorage.setItem("lista_empresas", JSON.stringify(lista_empresas));
}
function fetch_localstorage(): void {
    let lista_candidatos_string = localStorage.getItem("lista_candidatos");
    let lista_empresas_string = localStorage.getItem("lista_empresas");

    lista_candidatos = lista_candidatos_string ? JSON.parse(lista_candidatos_string) : [];
    lista_empresas = lista_empresas_string ? JSON.parse(lista_empresas_string) : [];
}



export function cadastrar_empresa(nome: string, email: string, estado:string, CEP:string, descricao:string, pais:string, CNPJ:string, competencias_desejadas:string[]): boolean{
    fetch_localstorage();
    
    try{
        let emp: Empresa = new Empresa(
            nome, email, estado, CEP, descricao, pais, CNPJ, competencias_desejadas
        )

        lista_empresas.push(emp);
    } catch (_){
        return false;
    }
    
    update_localstorage();
    return true;
}

export function cadastrar_candidato(nome: string, email: string, estado:string, CEP:string, descricao:string, idade:number, CPF:string, competencias:string[]): boolean{
    fetch_localstorage();
    
    try{
        let cand: Candidato = new Candidato(
            nome, email, estado, CEP, descricao, idade, CPF, competencias
        )

        lista_candidatos.push(cand);
    } catch (_){
        return false;
    }
    
    update_localstorage();
    return true;
}

export function dados_init(): void{
    fetch_localstorage();
    if (lista_candidatos.length <= 0){
        popular_candidatos();
        popular_empresas();
    }

    update_localstorage();
}