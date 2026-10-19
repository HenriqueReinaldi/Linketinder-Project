import { lista_recrutadores, lista_empresas, fetch_localstorage, update_localstorage } from "../persistencia/dados.js";
import { Empresa } from "../model/empresa.js";
import { Recrutador } from "../model/recrutador.js";

export function popular_recrutadores(): void {
    const nichos = ["tech", "design", "geral", "data", "tech"];

    lista_empresas.forEach((empresa, indice) => {
        lista_recrutadores.push(new Recrutador(
            indice + 1,
            `recrutador${indice + 1}`,
            `descricao recrutador${indice + 1}`,
            `recrutador${indice + 1}@email.com`,
            `CPF-R${indice + 1}`,
            `199${indice}-01-01`,
            "senha123",
            empresa,
            nichos[indice % nichos.length]
        ));
    });
}

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