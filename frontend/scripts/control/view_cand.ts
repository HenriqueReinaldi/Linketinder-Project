import { get_lista_candidatos, get_lista_vagas, lista_vagas } from "../service/dados.js";
import { Vaga } from "../model/vaga.js";
import { Candidato } from "../model/candidato.js";
import { Empresa } from "../model/empresa.js";

const campo_vagas: HTMLFieldSetElement = document.getElementById("campo") as HTMLFieldSetElement;
const campo_cpf: HTMLInputElement = document.getElementById("cpf_input") as HTMLInputElement;

var vagas: Vaga[] = get_lista_vagas();
var candidatos: Candidato[] = get_lista_candidatos();


var candidato_selecionado: Candidato | null = null;
function get_indice_de_afinidade(vaga: Vaga): number{
    if (candidato_selecionado === null) return 0;

    let tem: number = 0;
    let nao_tem: number = 0;

    for (let i : number = 0; i < vaga.competencias_desejadas.length; i++){
        if (candidato_selecionado.competencias.indexOf(vaga.competencias_desejadas[i]) != -1){
            tem++;
        }
        else{
            nao_tem++;
        }
    }

    if (nao_tem + tem == 0) return 0

    let afinidade: number = (tem / (nao_tem+tem)) * 100

    return Number.parseFloat(afinidade.toFixed(2));
}

function carregar_vagas(){
    for (let i : number = 0; i < vagas.length; i++){
        let afinidade = get_indice_de_afinidade(vagas[i])

        let template_vaga: string =  `
            <fieldset>
                <legend>${vagas[i].nome}</legend>
                ${vagas[i].descricao}<br>
                Salário: R$ ${vagas[i].salario}<br>
                índice de afinidade: ${afinidade}%<br>
                <button>ver mais</button>
            </fieldset>
            <br>
        `;

        campo_vagas.insertAdjacentHTML("beforeend", template_vaga);
    }
}

campo_cpf.onkeyup = function () { 
    campo_vagas.innerHTML = "";

    candidato_selecionado = null;
    for (let i: number = 0; i < candidatos.length; i++){
        if (candidatos[i].CPF == campo_cpf.value){
            candidato_selecionado = candidatos[i];
            break;
        } 
    }

    carregar_vagas();
}


