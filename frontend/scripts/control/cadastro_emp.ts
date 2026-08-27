import { cadastrar_empresa } from "../service/dados.js";

const input_nome: HTMLInputElement = document.getElementById("input_nome") as HTMLInputElement;
const input_email: HTMLInputElement = document.getElementById("input_email") as HTMLInputElement;
const input_estado: HTMLInputElement = document.getElementById("input_estado") as HTMLInputElement;
const input_CEP: HTMLInputElement = document.getElementById("input_CEP") as HTMLInputElement;
const input_descricao: HTMLInputElement = document.getElementById("input_descricao") as HTMLInputElement;
const input_pais: HTMLInputElement = document.getElementById("input_pais") as HTMLInputElement;
const input_CNPJ: HTMLInputElement = document.getElementById("input_CNPJ") as HTMLInputElement;
const input_competencias: HTMLInputElement = document.getElementById("input_competencias") as HTMLInputElement;

const submit: HTMLButtonElement = document.getElementById("cadastrar") as HTMLButtonElement;
const status: HTMLParagraphElement = document.getElementById("status") as HTMLParagraphElement;

function status_sucesso(){
    status.innerHTML = "Cadastrado!";
}
function status_deuruim(){
    status.innerHTML = "Falha com cadastro!";
}

submit.onclick = (evento: MouseEvent) => {
    evento.preventDefault();

    let res: boolean = cadastrar_empresa(
        input_nome.value,
        input_email.value,
        input_estado.value,
        input_CEP.value,
        input_descricao.value,
        input_pais.value,
        input_CNPJ.value,
        input_competencias.value.trim().split(" ")
    );

    if (res) status_sucesso();
    else status_deuruim();
}