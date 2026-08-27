import { get_lista_candidatos } from "../service/dados.js";
import { Candidato } from "../model/candidato.js";
declare const Chart: any;


const campo_candidatos: HTMLFieldSetElement = document.getElementById("campo") as HTMLFieldSetElement;
const grafico:HTMLCanvasElement = document.getElementById("grafico") as HTMLCanvasElement;

var candidatos: Candidato[] = get_lista_candidatos();
var competencias: Map<string, number> = new Map<string, number>();

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


    let template_vaga: string =  `
        <fieldset>
            <legend>candidato${i+1} (anonimo)</legend>
            Formação: ${candidatos[i].formacao}<br>
            Competencias: ${candidatos[i].competencias.toString()}<br>
            <button>ver mais</button>
        </fieldset>
        <br>
    `;

    campo_candidatos.insertAdjacentHTML("beforeend", template_vaga);
}

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