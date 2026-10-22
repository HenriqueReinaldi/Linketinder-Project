import { Curtida } from "../model/curtida.js";
import { Recrutador } from "../model/recrutador.js";
import { Candidato } from "../model/candidato.js";
import { Vaga } from "../model/vaga.js";
import { lista_candidatos, lista_curtidas, lista_vagas } from "../persistencia/dados.js";

export function popular_curtidas(): void {
    if (!lista_candidatos.length || !lista_vagas.length) return;

    lista_vagas.forEach((vaga, indice) => {
        const candidato: Candidato = lista_candidatos[indice % lista_candidatos.length];
        const ja_curtiu = lista_curtidas.some((curtida) =>
            curtida.candidato.CPF === candidato.CPF && curtida.vaga.nome === vaga.nome
        );

        if (!ja_curtiu) {
            lista_curtidas.push(new Curtida(candidato, vaga, indice % 3 === 0));
        }
    });
}

export function ja_curtiu_vaga(candidato: Candidato | null, vaga: Vaga | null, curtidas: Curtida[] = lista_curtidas): boolean {
    if (!candidato || !vaga) return false;

    return curtidas.some((curtida) =>
        curtida.candidato?.CPF === candidato.CPF && curtida.vaga?.nome === vaga.nome
    );
}

export function like_vaga(candidato: Candidato, vaga: Vaga, curtidas: Curtida[] = lista_curtidas): Curtida | null {
    if (!candidato || !vaga || ja_curtiu_vaga(candidato, vaga, curtidas)) {
        return null;
    }

    const nova_curtida = new Curtida(candidato, vaga, false);
    curtidas.push(nova_curtida);
    return nova_curtida;
}

function normalizar_nicho(nicho: string | undefined): string {
    return (nicho || "geral").trim().toLocaleLowerCase();
}

export function listar_curtidas_do_recrutador(recrutador: Recrutador, curtidas: Curtida[]): Curtida[] {
    const cnpj_empresa = recrutador.empresa?.CNPJ;
    const nicho_recrutador = normalizar_nicho(recrutador.nicho);

    if (!cnpj_empresa) return [];

    return curtidas.filter((curtida) => {
        const vaga = curtida.vaga;
        if (!vaga || vaga.empresa?.CNPJ !== cnpj_empresa) return false;

        return nicho_recrutador === "geral"
            || normalizar_nicho(vaga.nicho) === nicho_recrutador;
    });
}