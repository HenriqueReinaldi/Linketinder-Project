import { Candidato } from "../model/candidato.js";
import { Empresa } from "../model/empresa.js";
import { Vaga } from "../model/vaga.js";

export var lista_candidatos: Candidato[] = [];
export var lista_empresas: Empresa[] = [];
export var lista_vagas: Vaga[] = [];


function get_random_number(min: number, max: number): number {
    return Math.floor(Math.random() * (max - min) + min);
}

function get_random_competencias(): string[]{
    var comps: string[] = ["java", "python", "javascript", "groovy", "typescript", "git", "junit", "spock", "postgresql", "regex"];
    //shuffle
    for (let i : number = 0; i < comps.length; i++){
        for (let j : number = 0; j < comps.length; j++){
            if (get_random_number(1, 4) == 2) {
                [comps[i], comps[j]] = [comps[j], comps[i]];
            }

        }
    }

    //pegar janela
    let i = 0 + get_random_number(0, comps.length/2);
    let j = comps.length-1 - get_random_number(0, comps.length/2);

    return comps.slice(i, j);
}


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
                get_random_competencias(),
                `formacao${i}`
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
function popular_vagas(): void{
    //necessita de que lista_empresas nao esteja vazia.
    if (lista_empresas.length == 0) return;

    for (let i: number = 0; i < 5; i++){
        lista_vagas.push(
            new Vaga(
                `nome${i}`,
                67*(i+1),
                `descricao${i}`,
                lista_empresas[0]
            )
        );
    }
}


function update_localstorage(): void {
    localStorage.setItem("lista_candidatos", JSON.stringify(lista_candidatos));
    localStorage.setItem("lista_empresas", JSON.stringify(lista_empresas));
    localStorage.setItem("lista_vagas", JSON.stringify(lista_vagas));

}
function fetch_localstorage(): void {
    let lista_candidatos_string = localStorage.getItem("lista_candidatos");
    let lista_empresas_string = localStorage.getItem("lista_empresas");
    let lista_vagas_string = localStorage.getItem("lista_vagas");

    lista_candidatos = lista_candidatos_string ? JSON.parse(lista_candidatos_string) : [];
    lista_empresas = lista_empresas_string ? JSON.parse(lista_empresas_string) : [];
    lista_vagas = lista_vagas_string ? JSON.parse(lista_vagas_string) : [];
}

export function get_lista_vagas(): Vaga[]{
    fetch_localstorage();
    return lista_vagas;
}
export function get_lista_candidatos(): Candidato[]{
    fetch_localstorage();
    return lista_candidatos;
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


export function dados_init(): void{
    fetch_localstorage();
    if (lista_candidatos.length <= 0){
        popular_candidatos();
        popular_empresas();
        popular_vagas();
    }

    update_localstorage();
}