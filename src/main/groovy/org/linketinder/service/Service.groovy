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
    
    //ci = candidato_info
    boolean cadastrar_candidato(Map<String, String> ci){
        Candidato candidato = null;

        try{
            List<Competencia> competencias = ci["competencias"]
                .tokenize()
                .collect{new Competencia(tecnologia: it.trim())}

            Endereco endereco = new Endereco(
                CEP: ci.CEP,
                pais: null,
                estado: ci.estado,
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
                endereco: endereco,
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
            Endereco endereco = new Endereco(
                CEP: ei.CEP,
                pais: ei.pais,
                estado: ei.estado
            )

            empresa = new Empresa(
                CNPJ: ei.CNPJ,
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

    //vi = vagfa_info
    boolean cadastrar_vaga(Map<String, String> vi){
        Vaga vaga = null;

        try{
            List<Competencia> competencias = vi["competencias_desejadas"]
                .tokenize()
                .collect{new Competencia(tecnologia: it.trim())}

            Endereco endereco = new Endereco(
                CEP: vi.CEP,
                pais: vi.pais,
                estado: vi.estado,
            )

            int emp_id = bd.read.get_empresa_id_by_CNPJ(vi.empresa_CNPJ)
            if (emp_id == -1) return false

            Empresa empresa = bd.read.get_empresa_by_id(emp_id.toString())

            vaga = new Vaga(
                competencias_desejadas: competencias,
                nome: vi.nome,
                descricao: vi.descricao,
                endereco: endereco,
                empresa: empresa
            )
        }
        catch (Exception e) {
            e.printStackTrace(); return false
        }

        return bd.create.cadastrar_vaga(vaga)
    }



    boolean deletar_candidato(){}
}
