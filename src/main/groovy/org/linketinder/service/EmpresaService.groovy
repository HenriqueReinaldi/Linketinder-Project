package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.Banco
import org.linketinder.DAO.CurtidaDAO
import org.linketinder.DAO.EmpresaDAO
import org.linketinder.DAO.EnderecoDAO
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco

@TupleConstructor
class EmpresaService {
    EmpresaDAO dao
    EnderecoDAO endereco_dao

    List<Empresa> get_lista() {
        try {
            List<Empresa> empresas = dao.get_lista_empresa()

            empresas.each { Empresa empresa ->
                empresa.endereco = endereco_dao.get_endereco_by_id(empresa.endereco.id)
            }

            return empresas
        }
        catch (Exception ignored) {
            return []
        }
    }

    void cadastrar(Empresa m) {
        try {
            int endereco_id = endereco_dao.cadastrar_endereco_se_nao_existe(m.endereco)
            if (endereco_id < 0) return
            m.endereco.id = endereco_id

            dao.cadastrar_empresa_se_nao_existe(m)
        }
        catch (Exception ignored) {
        }
    }

    void deletar(int id) {
        try {
            dao.delete_empresa_by_id(id)
        }
        catch (Exception ignored) {
        }
    }

    void update(Empresa m) {
        try {
            int endereco_id = endereco_dao.cadastrar_endereco_se_nao_existe(m.endereco)
            if (endereco_id < 0) return
            m.endereco.id = endereco_id

            dao.update_empresa(m)
        }
        catch (Exception ignored) {
        }
    }

    Empresa get_by_CNPJ(String CNPJ) {
        try {
            int emp_id = dao.get_empresa_id_by_CNPJ(CNPJ)
            if (emp_id == -1) return null
            return dao.get_empresa_by_id(emp_id)
        }
        catch (Exception ignored) {
            return null
        }
    }
}
