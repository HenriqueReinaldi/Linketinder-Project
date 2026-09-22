package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.service.EmpresaService

@TupleConstructor
class EmpresaController {
    EmpresaService empresa_service

    List<Empresa> get_lista_empresa() {
        return empresa_service.get_lista()
    }

    void cadastrar_empresa(ModelData modelo) {
        Empresa m = assemble_empresa(modelo.data)
        empresa_service.cadastrar(m)
    }

    void deletar_empresa(int id) {
        empresa_service.deletar(id)
    }

    void update_empresa(ModelData modelo) {
        Empresa m = assemble_empresa(modelo.data)
        empresa_service.update(m)
    }


    Empresa assemble_empresa(Map<String, String> empresa_info) {
        try {
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
        } catch (Exception ignored) {
            return null
        }
    }
}
