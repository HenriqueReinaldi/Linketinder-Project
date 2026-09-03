package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.database.Banco
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga

@TupleConstructor
class Service {
    Banco bd;

    static Candidato assemble_candidato(Map<String, String> candidato_info){
        Candidato candidato = null;
        try{
            List<Competencia> competencias = candidato_info["competencias"]
                .tokenize()
                .collect{new Competencia(tecnologia: it.trim())}

            Endereco endereco = new Endereco(
                CEP: candidato_info.CEP,
                pais: null,
                estado: candidato_info.estado,
            )

            candidato = new Candidato(
                CPF: candidato_info.CPF,
                idade: candidato_info.idade.toInteger(),
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
        catch (Exception e) {
            e.printStackTrace(); return null
        }
        return candidato
    }
    
    static Empresa assemble_empresa(Map<String, String> empresa_info){
        Empresa empresa = null;

        try{
            Endereco endereco = new Endereco(
                    CEP: empresa_info.CEP,
                    pais: empresa_info.pais,
                    estado: empresa_info.estado
            )

            empresa = new Empresa(
                    CNPJ: empresa_info.CNPJ,
                    nome: empresa_info.nome,
                    email: empresa_info.email,
                    descricao: empresa_info.descricao,
                    senha: empresa_info.senha,
                    endereco: endereco
            )

        }
        catch (Exception e) {
            e.printStackTrace(); return null
        }
        return empresa
    }
    
    Vaga assemble_vaga(Map<String, String> vaga_info){
        Vaga vaga = null;

        try{
            List<Competencia> competencias = vaga_info["competencias_desejadas"]
                .tokenize()
                .collect{new Competencia(tecnologia: it.trim())}

            Endereco endereco = new Endereco(
                CEP: vaga_info.CEP,
                pais: vaga_info.pais,
                estado: vaga_info.estado,
            )

            int emp_id = bd.read.get_empresa_id_by_CNPJ(vaga_info.empresa_CNPJ)
            if (emp_id == -1) return false

            Empresa empresa = bd.read.get_empresa_by_id(emp_id.toString())

            vaga = new Vaga(
                competencias_desejadas: competencias,
                nome: vaga_info.nome,
                descricao: vaga_info.descricao,
                endereco: endereco,
                empresa: empresa
            )
        }
        catch (Exception e) {
            e.printStackTrace(); return null
        }

        return  vaga
    }
}
