import { cadastrar_candidato  } from "../service/dados.js";

const input_nome: HTMLInputElement = document.getElementById("input_nome") as HTMLInputElement;
const input_email: HTMLInputElement = document.getElementById("input_email") as HTMLInputElement;
const input_estado: HTMLInputElement = document.getElementById("input_estado") as HTMLInputElement;
const input_CEP: HTMLInputElement = document.getElementById("input_CEP") as HTMLInputElement;
const input_descricao: HTMLInputElement = document.getElementById("input_descricao") as HTMLInputElement;
const input_idade: HTMLInputElement = document.getElementById("input_idade") as HTMLInputElement;
const input_CPF: HTMLInputElement = document.getElementById("input_CPF") as HTMLInputElement;
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

    let idade: number;
    try{
        idade = parseInt(input_idade.value);
    } 
    catch(_){
        status_deuruim();
        return;
    }

    let res: boolean = cadastrar_candidato(
        input_nome.value,
        input_email.value,
        input_estado.value,
        input_CEP.value,
        input_descricao.value,
        idade,
        input_CPF.value,
        input_competencias.value.trim().split(" ")
    );

    if (res) status_sucesso();
    else status_deuruim();
}