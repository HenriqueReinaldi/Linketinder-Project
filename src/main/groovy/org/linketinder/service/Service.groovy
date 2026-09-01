package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.dao.Banco
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco

@TupleConstructor
class Service {
    Banco bd;
    
    //ci = candidato_info
    boolean cadastrar_candidato(Map<String, String> ci){
        Candidato candidato = null;

        try{
            List<Competencia> competencias = ci["competencias"]
                .tokenize()
                .collect{new Competencia(it.trim())}

            Endereco endereco = new Endereco(
                CEP: ci.CEP,
                pais: null,
                estado: ci.estado
            )

            candidato = new Candidato(
                CPF: ci.CPF,
                idade: ci.idade.toInteger(),
                competencias: competencias,
                nome: ci.nome,
                sobrenome: ci.sobrenome,
                data_nascimento: ci.nascimento,
                email: ci.email,
                descricao: ci.descricao,
                senha: ci.senha,
                endereco: endereco
            )
        }
        catch (Exception e) {
            e.printStackTrace(); return false
        }

        return bd.create.cadastrar_candidato(candidato)
    }

    //ei = empresa_info
    boolean cadastrar_empresa(Map<String, String> ei){
        Empresa empresa = null;

        try{
            List<Competencia> competencias = ei["competencias"]
                .tokenize()
                .collect{new Competencia(it.trim())}

            Endereco endereco = new Endereco(
                CEP: ei.CEP,
                pais: ei.pais,
                estado: ei.estado
            )

            empresa = new Empresa(
                CNPJ: ei.CNPJ,
                competencias_desejadas: competencias,
                nome: ei.nome,
                email: ei.email,
                descricao: ei.descricao,
                senha: ei.senha,
                endereco: endereco
            )

        }
        catch (Exception e) {
            e.printStackTrace(); return false
        }

        return bd.create.cadastrar_empresa(empresa);
    }
}
