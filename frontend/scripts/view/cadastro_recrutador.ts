import { get_lista_empresas } from "../persistencia/dados.js";
import { cadastrar_recrutador } from "../service/recrutadoreservice.js";

const input_nome = document.getElementById("input_nome") as HTMLInputElement;
const input_email = document.getElementById("input_email") as HTMLInputElement;
const input_CPF = document.getElementById("input_CPF") as HTMLInputElement;
const input_data_nascimento = document.getElementById("input_data_nascimento") as HTMLInputElement;
const input_senha = document.getElementById("input_senha") as HTMLInputElement;
const input_empresa = document.getElementById("input_empresa") as HTMLSelectElement;
const input_nicho = document.getElementById("input_nicho") as HTMLInputElement;
const input_descricao = document.getElementById("input_descricao") as HTMLTextAreaElement;
const submit = document.getElementById("cadastrar") as HTMLButtonElement;
const status = document.getElementById("status") as HTMLParagraphElement;

function preencher_empresas(): void {
    const empresas = get_lista_empresas();
    input_empresa.replaceChildren();

    if (empresas.length === 0) {
        const opcao_vazia = document.createElement("option");
        opcao_vazia.value = "";
        opcao_vazia.textContent = "Cadastre uma empresa primeiro";
        input_empresa.append(opcao_vazia);
        input_empresa.disabled = true;
        return;
    }

    input_empresa.disabled = false;
    empresas.forEach((empresa, indice) => {
        const opcao = document.createElement("option");
        opcao.value = String(indice);
        opcao.textContent = `${empresa.nome} (${empresa.CNPJ})`;
        input_empresa.append(opcao);
    });
}

function status_sucesso(): void {
    status.textContent = "Cadastrado!";
}

function status_deuruim(): void {
    status.textContent = "Falha com cadastro!";
}

preencher_empresas();

submit.onclick = (evento: MouseEvent) => {
    evento.preventDefault();

    const empresas = get_lista_empresas();
    if (empresas.length === 0 || !input_empresa.value) {
        status_deuruim();
        status.textContent = "Cadastre uma empresa antes de criar um recrutador.";
        return;
    }

    const empresa = empresas[Number(input_empresa.value)];
    const recrutador = cadastrar_recrutador(
        input_nome.value,
        input_descricao.value,
        input_email.value,
        input_CPF.value,
        input_data_nascimento.value,
        input_senha.value,
        empresa,
        input_nicho.value
    );

    if (recrutador) {
        status_sucesso();
        return;
    }

    status_deuruim();
};
