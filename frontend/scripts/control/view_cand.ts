import { get_lista_vagas, lista_vagas } from "../service/dados.js";
import { Vaga } from "../model/vaga.js";

const campo_vagas: HTMLFieldSetElement = document.getElementById("campo") as HTMLFieldSetElement;


var vagas: Vaga[] = get_lista_vagas();


for (let i : number = 0; i < vagas.length; i++){
    let template_vaga: string =  `
        <fieldset>
            <legend>${vagas[i].nome}</legend>
            ${vagas[i].descricao}<br>
            Salário: R$ ${vagas[i].salario}<br>
            <button>ver mais</button>
        </fieldset>
        <br>
    `;

    campo_vagas.insertAdjacentHTML("beforeend", template_vaga);
}

