package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga
import org.linketinder.service.CandidatoService
import org.linketinder.service.EmpresaService
import org.linketinder.service.ServiceBundle
import org.linketinder.service.VagaService

@TupleConstructor
class AssembleModel {
    EmpresaService empresa_service
    CandidatoService candidato_service
    VagaService vaga_service

    static <GENERICO> GENERICO assemble_com_seguranca(Closure<GENERICO> operacao){
        try{
            return operacao()
        }
        catch (Exception e){
            println "erro criando o modelo:"
            println "    " + e.message
            println ""
        }
        return null
    }

    Candidato assemble_candidato(Map<String, String> candidato_info){
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
                    id: candidato_info.id ? candidato_info.id.toInteger() : -1
            )
        }
    }

    Empresa assemble_empresa(Map<String, String> empresa_info){
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
                    endereco: endereco,
                    id: empresa_info.id ? empresa_info.id.toInteger() : -1
            )
        }
    }

    Competencia assemble_competencia(Map<String, String> competencia_info){
        return assemble_com_seguranca {
            return new Competencia(
                tecnologia: competencia_info.tecnologia,
                id: competencia_info.id ? competencia_info.id.toInteger() : -1
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

            Empresa empresa = empresa_service.get_by_CNPJ(vaga_info.empresa_CNPJ)

            return new Vaga(
                    competencias_desejadas: competencias,
                    nome: vaga_info.nome,
                    descricao: vaga_info.descricao,
                    endereco: endereco,
                    empresa: empresa,
                    id: vaga_info.id ? vaga_info.id.toInteger() : -1
            )
        }
    }

    Curtida assemble_curtida(Map<String, String> curtida_info){
        return assemble_com_seguranca{
            Candidato candidato = candidato_service.get_by_id(curtida_info["candidato_id"])
            Vaga vaga = vaga_service.get_by_id(curtida_info["vaga_id"])

            return new Curtida(
                candidato: candidato,
                vaga: vaga,
                id: curtida_info.id ? curtida_info.id.toInteger() : -1
            )
        }
    }
}
