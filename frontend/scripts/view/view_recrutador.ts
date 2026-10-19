import { get_lista_curtidas, get_lista_recrutadores, update_localstorage } from "../persistencia/dados.js";
import { listar_curtidas_do_recrutador } from "../service/curtidaservice.js";
import { Curtida } from "../model/curtida.js";
import { Recrutador } from "../model/recrutador.js";

const campo_id_recrutador = document.getElementById("recrutador_id") as HTMLInputElement;
const mensagem = document.getElementById("mensagem") as HTMLParagraphElement;
const lista = document.getElementById("lista_curtidas") as HTMLDivElement;
const recrutadores: Recrutador[] = get_lista_recrutadores();
const curtidas: Curtida[] = get_lista_curtidas();

function marcar_match(curtida: Curtida): void {
    if (curtida.empresa_curtiu) return;

    curtida.empresa_curtiu = true;
    update_localstorage();
    exibir_curtidas();
}

function exibir_curtidas(): void {
    lista.replaceChildren();

    const recrutador = recrutadores.find((item) => String(item.id) === campo_id_recrutador.value.trim());
    if (!recrutador) {
        mensagem.textContent = recrutadores.length
            ? "Informe um ID de recrutador válido para consultar as curtidas."
            : "Nenhum recrutador está cadastrado neste navegador.";
        return;
    }

    const curtidas_compativeis = listar_curtidas_do_recrutador(recrutador, curtidas);
    mensagem.textContent = curtidas_compativeis.length
        ? `${curtidas_compativeis.length} curtida(s) encontrada(s).`
        : "Nenhuma curtida encontrada para este recrutador.";

    curtidas_compativeis.forEach((curtida, indice) => {
        const item = document.createElement("fieldset");
        const titulo = document.createElement("legend");
        const detalhe = document.createElement("div");
        const resumo = document.createElement("p");
        const descricao = document.createElement("p");
        const match = document.createElement("strong");
        const botao_match = document.createElement("button");

        titulo.textContent = `Curtida ${indice + 1}`;
        resumo.textContent = `Vaga: ${curtida.vaga.nome} · Nicho: ${curtida.vaga.nicho || "geral"}`;
        descricao.textContent = `Descrição do candidato: ${curtida.candidato.descricao || "Sem descrição informada."}`;
        match.textContent = curtida.empresa_curtiu ? "MATCH" : "";
        match.style.color = curtida.empresa_curtiu ? "green" : "";
        match.style.fontWeight = "700";
        botao_match.type = "button";
        botao_match.textContent = curtida.empresa_curtiu ? "MATCH" : "Criar match";
        botao_match.disabled = curtida.empresa_curtiu;
        botao_match.addEventListener("click", () => marcar_match(curtida));

        detalhe.append(resumo, descricao);
        if (curtida.empresa_curtiu) {
            detalhe.append(match);
        } else {
            detalhe.append(botao_match);
        }
        item.append(titulo, detalhe);
        lista.append(item);
    });
}

campo_id_recrutador.addEventListener("keyup", exibir_curtidas);
exibir_curtidas();