import { Candidato } from "../model/candidato.js";
import { Empresa } from "../model/empresa.js";
import { Vaga } from "../model/vaga.js";

export var lista_candidatos: Candidato[] = [];
export var lista_empresas: Empresa[] = [];
export var lista_vagas: Vaga[] = [];
var VERSAO_PROGRAMA: string = "tres";


export function update_localstorage(): void {
    localStorage.setItem("lista_candidatos", JSON.stringify(lista_candidatos));
    localStorage.setItem("lista_empresas", JSON.stringify(lista_empresas));
    localStorage.setItem("lista_vagas", JSON.stringify(lista_vagas));
}
export function fetch_localstorage(): void {
    if (localStorage.getItem("VERSAO_PROGRAMA") !== VERSAO_PROGRAMA){
        localStorage.clear()
        localStorage.setItem("VERSAO_PROGRAMA", VERSAO_PROGRAMA);

        lista_candidatos = [];
        lista_empresas = [];
        lista_vagas = [];
        return;
    }

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
export function get_lista_empresas(): Empresa[]{
    fetch_localstorage();
    return lista_empresas;
}
