package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.service.EmpresaService

@TupleConstructor
class EmpresaController {
    AssembleModel assemble_model
    EmpresaService empresa_service

    List<Empresa> get_lista_empresa(){
        return empresa_service.get_lista()
    }

    void cadastrar_empresa(ModelData modelo){
        Empresa m = assemble_model.assemble_empresa(modelo.data)
        empresa_service.cadastrar(m)
    }

    void deletar_empresa(int id){
        empresa_service.deletar(id)
    }

    void update_empresa(ModelData modelo){
        Empresa m = assemble_model.assemble_empresa(modelo.data)
        empresa_service.update(m)
    }

    void empresa_curtir(ModelData modelo){
        Curtida c = assemble_model.assemble_curtida(modelo.data)
        empresa_service.curtir(c)
    }
}
