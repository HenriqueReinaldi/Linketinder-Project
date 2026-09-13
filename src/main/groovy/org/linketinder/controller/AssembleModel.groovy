package org.linketinder.controller

import org.linketinder.database.Banco
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga
import org.linketinder.service.Service

class AssembleModel {
    Service service

    static <GENERICO> GENERICO assemble_com_seguranca(Closure<GENERICO> operacao){
        try{
            return operacao()
        }
        catch (Exception e){
            println "erro executando operação:"
            println "    " + e.message
            println ""
        }
        return null
    }

    static Candidato assemble_candidato(Map<String, String> candidato_info){
        return assemble_com_seguranca {
            List<Competencia> competencias = candidato_info["competencias"]
                    .tokenize()
                    .collect{new Competencia(tecnologia: it.trim())}

            Endereco endereco = new Endereco(
                    CEP: candidato_info.CEP,
                    pais: null,
                    estado: candidato_info.estado,
            )

            return new Candidato(
                    CPF: candidato_info.CPF,
                    competencias: competencias,
                    nome: candidato_info.nome,
                    sobrenome: candidato_info.sobrenome,
                    data_nascimento: candidato_info.nascimento,
                    email: candidato_info.email,
                    descricao: candidato_info.descricao,
                    senha: candidato_info.senha,
                    endereco: endereco,
            )
        }
    }

    static Empresa assemble_empresa(Map<String, String> empresa_info){
        return assemble_com_seguranca {
            Endereco endereco = new Endereco(
                    CEP: empresa_info.CEP,
                    pais: empresa_info.pais,
                    estado: empresa_info.estado
            )

            return new Empresa(
                    CNPJ: empresa_info.CNPJ,
                    nome: empresa_info.nome,
                    email: empresa_info.email,
                    descricao: empresa_info.descricao,
                    senha: empresa_info.senha,
                    endereco: endereco
            )
        }
    }

    Vaga assemble_vaga(Map<String, String> vaga_info){
        return assemble_com_seguranca{
            List<Competencia> competencias = vaga_info["competencias_desejadas"]
                    .tokenize()
                    .collect{new Competencia(tecnologia: it.trim())}

            Endereco endereco = new Endereco(
                    CEP: vaga_info.CEP,
                    pais: vaga_info.pais,
                    estado: vaga_info.estado,
            )

            Empresa empresa = service.get_empresa_by_CNPJ(vaga_info.empresa_CNPJ)

            return new Vaga(
                    competencias_desejadas: competencias,
                    nome: vaga_info.nome,
                    descricao: vaga_info.descricao,
                    endereco: endereco,
                    empresa: empresa
            )
        }
    }

    Curtida assemble_curtida(Map<String, String> curtida_info){
        return assemble_com_seguranca{
            Candidato candidato = service.get_candidato_by_id(curtida_info["candidato_id"])
            Vaga vaga = service.get_vaga_by_id(curtida_info["vaga_id"])

            return new Curtida(
                candidato: candidato,
                vaga: vaga,
            )
        }
    }
}
