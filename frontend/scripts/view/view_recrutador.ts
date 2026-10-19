import { get_lista_curtidas, get_lista_recrutadores } from "../persistencia/dados.js";
import { listar_curtidas_do_recrutador } from "../service/curtidaservice.js";
import { Curtida } from "../model/curtida.js";
import { Recrutador } from "../model/recrutador.js";

const campo_id_recrutador = document.getElementById("recrutador_id") as HTMLInputElement;
const mensagem = document.getElementById("mensagem") as HTMLParagraphElement;
const lista = document.getElementById("lista_curtidas") as HTMLDivElement;
const recrutadores: Recrutador[] = get_lista_recrutadores();
const curtidas: Curtida[] = get_lista_curtidas();

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
        const detalhe = document.createElement("span");

        titulo.textContent = `Curtida ${indice + 1} · ${curtida.vaga.nome}`;
        detalhe.textContent = `Nicho: ${curtida.vaga.nicho || "geral"}`;
        item.append(titulo, detalhe);
        lista.append(item);
    });
}

campo_id_recrutador.addEventListener("keyup", exibir_curtidas);
exibir_curtidas();