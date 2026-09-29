package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.EmpresaDAO
import org.linketinder.DAO.EnderecoDAO
import org.linketinder.model.objetos.Empresa

@TupleConstructor
class EmpresaService extends Service {
    EmpresaDAO dao
    EnderecoDAO endereco_dao

    List<Empresa> get_lista() {
        executar_seguramente([]) {
            List<Empresa> empresas = dao.get_lista_empresa()

            empresas.each { Empresa empresa ->
                empresa.endereco = endereco_dao.get_endereco_by_id(empresa.endereco.id)
            }

            return empresas
        }
    }

    boolean cadastrar(Empresa m) {
        executar_seguramente(false) {
            int endereco_id = endereco_dao.cadastrar_endereco_se_nao_existe(m.endereco)
            if (endereco_id < 0) return
            m.endereco.id = endereco_id

            dao.cadastrar_empresa_se_nao_existe(m)
            return true
        }
    }

    boolean deletar(int id) {
        executar_seguramente(false) {
            dao.delete_empresa_by_id(id)
            return true
        }
    }

    boolean update(Empresa m) {
        executar_seguramente(false) {
            int endereco_id = endereco_dao.cadastrar_endereco_se_nao_existe(m.endereco)
            if (endereco_id < 0) return
            m.endereco.id = endereco_id

            dao.update_empresa(m)
            return true
        }
    }

    Empresa get_by_CNPJ(String CNPJ) {
        executar_seguramente(null) {
            int emp_id = dao.get_empresa_id_by_CNPJ(CNPJ)
            if (emp_id == -1) return null

            return dao.get_empresa_by_id(emp_id)
        }
    }
}
