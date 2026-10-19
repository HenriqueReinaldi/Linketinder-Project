package org.linketinder.controller

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Recrutador
import org.linketinder.model.objetos.Vaga
import org.linketinder.service.CurtidaService
import org.linketinder.service.EmpresaService
import org.linketinder.service.RecrutadorService
import org.linketinder.service.VagaService

@TupleConstructor
class RecrutadorController {
    RecrutadorService recrutador_service
    EmpresaService empresa_service
    VagaService vaga_service
    CurtidaService curtida_service

    List<Recrutador> get_lista_recrutador() {
        return recrutador_service.get_lista()
    }

    boolean cadastrar_recrutador(ModelData modelo) {
        Recrutador recrutador = assemble_recrutador(modelo.data)
        if (recrutador == null) return false

        return recrutador_service.cadastrar(recrutador)
    }

    boolean deletar_recrutador(int id) {
        return recrutador_service.deletar(id)
    }

    boolean update_recrutador(ModelData modelo) {
        Recrutador recrutador = assemble_recrutador(modelo.data)
        if (recrutador == null) return false

        return recrutador_service.update(recrutador)
    }

    List<Vaga> get_lista_vaga_recrutador(int recrutador_id) {
        return vaga_service.get_lista_vaga_recrutador(recrutador_id)
    }

    List<Curtida> get_lista_curtida_recrutador(int recrutador_id) {
        if (curtida_service == null) return []
        return curtida_service.get_lista_curtida_recrutador(recrutador_id)
    }

    Recrutador assemble_recrutador(Map<String, String> recrutador_info) {
        try {
            if (recrutador_info == null) return null

            String empresa_cnpj = recrutador_info.empresa_CNPJ
            Empresa empresa = empresa_service.get_by_CNPJ(empresa_cnpj)
            if (empresa == null) return null

            return new Recrutador(
                    CPF: recrutador_info.CPF,
                    nome: recrutador_info.nome,
                    descricao: recrutador_info.descricao,
                    email: recrutador_info.email,
                    senha: recrutador_info.senha,
                    data_nascimento: recrutador_info.nascimento ?: recrutador_info.data_nascimento,
                    empresa: empresa,
                    nicho: recrutador_info.nicho,
                    endereco: null,
                    id: recrutador_info.id ? recrutador_info.id.toInteger() : -1
            )
        }
        catch (Exception ignored) {
            return null
        }
    }

}
