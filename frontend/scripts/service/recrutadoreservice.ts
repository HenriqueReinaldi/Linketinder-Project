import { lista_recrutadores, fetch_localstorage, update_localstorage } from "../persistencia/dados.js";
import { Empresa } from "../model/empresa.js";
import { Recrutador } from "../model/recrutador.js";

export function cadastrar_recrutador(
    nome: string,
    descricao: string,
    e_mail: string,
    CPF: string,
    data_nascimento: string,
    senha: string,
    empresa: Empresa,
    nicho: string
): Recrutador | null {
    fetch_localstorage();

    if (!nome.trim() || !descricao.trim() || !e_mail.trim() || !CPF.trim() || !data_nascimento.trim() || senha.length < 6 || !empresa || !nicho.trim()) return null;

    const id = lista_recrutadores.reduce((maior_id, recrutador) => Math.max(maior_id, recrutador.id), 0) + 1;
    const recrutador = new Recrutador(id, nome, descricao, e_mail, CPF, data_nascimento, senha, empresa, nicho.trim());
    lista_recrutadores.push(recrutador);
    update_localstorage();
    return recrutador;
}