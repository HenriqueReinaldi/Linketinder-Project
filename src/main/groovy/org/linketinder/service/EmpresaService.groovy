package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.Banco
import org.linketinder.DAO.EmpresaDAO
import org.linketinder.DAO.EnderecoDAO
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa

@TupleConstructor
class EmpresaService {
    Banco bd

    EmpresaDAO dao
    EnderecoDAO endereco_dao

    List<Empresa> get_lista() {
        try {
            List<Empresa> empresas = dao.get_lista_empresa()

            empresas.each {Empresa empresa ->
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
            bd.create.cadastrar_empresa_if_not_exists(m)
        }
        catch (Exception ignored) {
        }
    }

    Empresa get_by_CNPJ(String CNPJ) {
        try {
            int emp_id = bd.read.get_empresa_id_by_CNPJ(CNPJ)
            if (emp_id == -1) return null
            bd.read.get_empresa_by_id(emp_id)
        }
        catch (Exception ignored) {
            return null
        }
    }

    void deletar(int id) {
        try {
            bd.delete.delete_empresa_by_id(id)
        }
        catch (Exception ignored) {
        }
    }

    void curtir(Curtida c) {
        try {
            bd.update.empresa_curtir(c)
        }
        catch (Exception ignored) {
        }
    }

    void update(Empresa m) {
        try {
            bd.update.update_empresa(m)
        }
        catch (Exception ignored) {
        }
    }

    EmpresaService(Banco bd) {
        this.bd = bd
        this.dao = new EmpresaDAO(bd)
        this.endereco_dao = new EnderecoDAO(bd)
    }
}
