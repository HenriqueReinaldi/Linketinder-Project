import { get_lista_candidatos, get_lista_vagas, lista_curtidas, update_localstorage } from "../persistencia/dados.js";
import { Vaga } from "../model/vaga.js";
import { Candidato } from "../model/candidato.js";
import { ja_curtiu_vaga, like_vaga } from "../service/curtidaservice.js";

const campo_vagas: HTMLFieldSetElement = document.getElementById("campo") as HTMLFieldSetElement;
const campo_cpf: HTMLInputElement = document.getElementById("cpf_input") as HTMLInputElement;

const vagas: Vaga[] = get_lista_vagas();
const candidatos: Candidato[] = get_lista_candidatos();

let candidato_selecionado: Candidato | null = null;

function get_indice_de_afinidade(vaga: Vaga): number {
    if (candidato_selecionado === null) return 0;

    let tem: number = 0;
    let nao_tem: number = 0;

    for (let i: number = 0; i < vaga.competencias_desejadas.length; i++) {
        if (candidato_selecionado.competencias.indexOf(vaga.competencias_desejadas[i]) != -1) {
            tem++;
        } else {
            nao_tem++;
        }
    }

    if (nao_tem + tem == 0) return 0;

    const afinidade: number = (tem / (nao_tem + tem)) * 100;

    return Number.parseFloat(afinidade.toFixed(2));
}

function carregar_vagas(): void {
    campo_vagas.innerHTML = "";

    if (!candidato_selecionado) {
        campo_vagas.insertAdjacentHTML("beforeend", "<legend>Vagas cadastradas:</legend><p>Informe um CPF de candidato para curtir uma vaga.</p>");
        return;
    }

    const legenda = document.createElement("legend");
    legenda.textContent = "Vagas cadastradas:";
    campo_vagas.appendChild(legenda);

    for (let i: number = 0; i < vagas.length; i++) {
        const fluxo_ja_curtiu = ja_curtiu_vaga(candidato_selecionado, vagas[i], lista_curtidas);
        const afinidade = get_indice_de_afinidade(vagas[i]);
        const template_vaga: string = `
            <fieldset>
                <legend>${vagas[i].nome}</legend>
                ${vagas[i].descricao}<br>
                Salário: R$ ${vagas[i].salario}<br>
                índice de afinidade: ${afinidade}%<br>
                <button type="button" data-vaga-nome="${vagas[i].nome}" ${fluxo_ja_curtiu ? "disabled" : ""}>
                    ${fluxo_ja_curtiu ? "vaga curtida" : "curtir vaga"}
                </button>
            </fieldset>
            <br>
        `;

        campo_vagas.insertAdjacentHTML("beforeend", template_vaga);
    }

    campo_vagas.querySelectorAll<HTMLButtonElement>("button[data-vaga-nome]").forEach((botao) => {
        botao.addEventListener("click", () => {
            const nome_vaga = botao.dataset.vagaNome;
            const vaga = vagas.find((item) => item.nome === nome_vaga);

            if (!nome_vaga || !vaga || !candidato_selecionado) {
                return;
            }

            const curtida = like_vaga(candidato_selecionado, vaga, lista_curtidas);

            if (!curtida) {
                botao.textContent = "vaga curtida";
                botao.disabled = true;
                return;
            }

            update_localstorage();
            botao.textContent = "vaga curtida";
            botao.disabled = true;
        });
    });
}

campo_cpf.onkeyup = function () {
    campo_vagas.innerHTML = "";

    candidato_selecionado = null;
    for (let i: number = 0; i < candidatos.length; i++) {
        if (candidatos[i].CPF == campo_cpf.value) {
            candidato_selecionado = candidatos[i];
            break;
        }
    }

    carregar_vagas();
};

carregar_vagas();
