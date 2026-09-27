import { get_lista_candidatos, get_lista_vagas, get_lista_empresas } from "../persistencia/dados.js";
import { Candidato } from "../model/candidato.js";
import { Vaga } from "../model/vaga.js";
import { Empresa } from "../model/empresa.js";
declare const Chart: any;

const campo_candidatos: HTMLFieldSetElement = document.getElementById("campo") as HTMLFieldSetElement;
const grafico:HTMLCanvasElement = document.getElementById("grafico") as HTMLCanvasElement;
const campo_cnpj: HTMLInputElement = document.getElementById("cnpj_input") as HTMLInputElement;

var candidatos: Candidato[] = get_lista_candidatos();
var vagas: Vaga[] = get_lista_vagas();
var empresas: Empresa[] = get_lista_empresas();
var empresa_selecionada: Empresa | null = null;

var competencias: Map<string, number> = new Map<string, number>();


function get_indice_de_afinidade(candidato: Candidato): number{
    if (empresa_selecionada === null) return 0;

    let competencias_pesos: Map<string, number> = new Map<string, number>();


    for (let i: number = 0; i < vagas.length; i++){
        if(vagas[i].empresa.CNPJ != empresa_selecionada.CNPJ) continue

        for (let j: number = 0; j < vagas[i].competencias_desejadas.length; j++){            
            if (competencias_pesos.has(vagas[i].competencias_desejadas[j])) {
                let valor_velho: number = competencias_pesos.get(vagas[i].competencias_desejadas[j]) as number;
                competencias_pesos.set(vagas[i].competencias_desejadas[j], valor_velho + 1)
            }
            else{
                competencias_pesos.set(vagas[i].competencias_desejadas[j], 1)
            }
        }

    }


    let tem: number = 0;
    let nao_tem: number = 0;

    for (let i: number = 0; i < candidato.competencias.length; i++){
        if (competencias_pesos.has(candidato.competencias[i])){
            tem += competencias_pesos.get(candidato.competencias[i]) as number
        }
        else{
            nao_tem += 1
        }
    }

    if (nao_tem + tem == 0) return 0

    let afinidade: number = (tem / (nao_tem+tem)) * 100

    return Number.parseFloat(afinidade.toFixed(2));
}

function carregar_candidatos(){
    for (let i : number = 0; i < candidatos.length; i++){

        for (let j : number =0; j < candidatos[i].competencias.length; j++){
            if (competencias.has(candidatos[i].competencias[j])){
                let valor_velho: number = competencias.get(candidatos[i].competencias[j]) as number;
                competencias.set(candidatos[i].competencias[j], valor_velho+1);
            }
            else{
                competencias.set(candidatos[i].competencias[j], 1);
            }
        }

        let afinidade = get_indice_de_afinidade(candidatos[i]);

        let template_vaga: string =  `
            <fieldset>
                <legend>candidato${i+1} (anonimo)</legend>
                Formação: ${candidatos[i].formacao}<br>
                Competencias: ${candidatos[i].competencias.toString()}<br>
                Índice de afinidiade: ${afinidade}%<br>
                <button>ver mais</button>
            </fieldset>
            <br>
        `;

        campo_candidatos.insertAdjacentHTML("beforeend", template_vaga);
    }
    carregar_grafico();
}

function carregar_grafico(){
    new Chart(grafico, {
        type: 'bar',

        data: {
            labels: [...competencias.keys()],
            datasets: [{
                label: 'quantia de candidatos',
                data: [...competencias.values()],
                borderWidth: 1
            }]
        },

        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 1,
                        precision: 0
                    }
                }
            }
        }
    });
}

campo_cnpj.onkeyup = function () {
    campo_candidatos.innerHTML = "";

    empresa_selecionada = null;
    for (let i: number = 0; i < empresas.length; i++){
        if (empresas[i].CNPJ == campo_cnpj.value){
            empresa_selecionada = empresas[i];
            break;
        } 
    }

    carregar_candidatos();
}