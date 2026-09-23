package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga
import org.linketinder.service.EmpresaService
import org.linketinder.service.VagaService

@TupleConstructor
class VagaController {
    VagaService vaga_service
    EmpresaService empresa_service

    List<Vaga> get_lista_vaga() {
        return vaga_service.get_lista()
    }

    void cadastrar_vaga(ModelData modelo) {
        Vaga v = assemble_vaga(modelo.data)
        vaga_service.cadastrar(v)
    }

    void deletar_vaga(int id) {
        vaga_service.deletar(id)
    }

    void update_vaga(ModelData modelo) {
        Vaga v = assemble_vaga(modelo.data)
        vaga_service.update(v)
    }

    Vaga assemble_vaga(Map<String, String> vaga_info) {
        try {
            List<Competencia> competencias = vaga_info["competencias_desejadas"]
                    .tokenize()
                    .collect { new Competencia(tecnologia: it.trim()) }

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
        catch (Exception ignored) {
            return null
        }
    }
}
