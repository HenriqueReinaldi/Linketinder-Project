package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Endereco
import org.linketinder.service.CandidatoService

@TupleConstructor
class CandidatoController {
    CandidatoService candidato_service

    List<Candidato> get_lista_candidato() {
        return candidato_service.get_lista()
    }

    void cadastrar_candidato(ModelData modelo) {
        Candidato c = assemble_candidato(modelo.data)
        candidato_service.cadastrar(c)
    }

    void deletar_candidato(int id) {
        candidato_service.deletar(id)
    }

    void update_candidato(ModelData modelo) {
        Candidato c = assemble_candidato(modelo.data)
        candidato_service.update(c)
    }




    Candidato assemble_candidato(Map<String, String> candidato_info){
        try {
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
        } catch (Exception ignored){
            return null
        }
    }
}
