package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.Banco
import org.linketinder.DAO.CompetenciaDAO
import org.linketinder.DAO.EmpresaDAO
import org.linketinder.DAO.EnderecoDAO
import org.linketinder.DAO.VagaDAO
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Vaga

@TupleConstructor
class VagaService {
    Banco bd

    VagaDAO dao
    EnderecoDAO endereco_dao
    CompetenciaDAO competencia_dao
    EmpresaDAO empresa_dao

    List<Vaga> get_lista() {
        try {
            List<Vaga> vagas = dao.get_lista_vaga()

            vagas.each { Vaga vaga ->
                vaga.endereco = endereco_dao.get_endereco_by_id(vaga.endereco.id)
                vaga.competencias_desejadas = competencia_dao.get_lista_competencias_of_entidade("vaga", vaga.id)
                vaga.empresa = empresa_dao.get_empresa_by_id(vaga.empresa.id)
            }

            return vagas
        }
        catch (Exception ignored) {
            return null
        }
    }

    void cadastrar(Vaga v) {
        try {
            int endereco_id = endereco_dao.cadastrar_endereco_se_nao_existe(v.endereco)
            if (endereco_id < 0) return
            v.endereco.id = endereco_id

            vaga_id = dao.cadastrar_vaga(v)
            if (vaga_id < 0) return

            List<Integer> competencias_id = []
            for (Competencia comp : v.competencias_desejadas) {
                competencias_id << competencia_dao.create_if_not_exists_competencia(comp.tecnologia)
            }
            for (int competencia_id : competencias_id) {
                competencia_dao.cadastrar_competencias_entidade("vaga", vaga_id, competencia_id)
            }
        }
        catch (Exception e) {
            e.printStackTrace()
        }
    }

    void deletar(int id) {
        try {
            dao.delete_vaga_by_id(id)
        }
        catch (Exception ignored) {
        }
    }

    void update(Vaga v) {
        try {
            int endereco_id = endereco_dao.cadastrar_endereco_se_nao_existe(v.endereco)
            if (endereco_id < 0) return
            v.endereco.id = endereco_id

            dao.update_vaga(v)

            competencia_dao.delete_entidade_competencias_by_entidadeid("vaga", v.id)
            List<Integer> competencias_id = []
            for (Competencia comp : v.competencias_desejadas) {
                competencias_id << competencia_dao.create_if_not_exists_competencia(comp.tecnologia)
            }
            for (int competencia_id : competencias_id) {
                competencia_dao.cadastrar_competencias_entidade("vaga", v.id, competencia_id)
            }
        }
        catch (Exception ignored) {
        }
    }

    Vaga get_by_id(String id) {
        try {
            return dao.get_vaga_by_id(Integer.parseInt(id))
        } catch (Exception ignored) {
            return null
        }
    }

    VagaService(Banco bd) {
        this.bd = bd
        this.dao = new VagaDAO(bd)
        this.endereco_dao = new EnderecoDAO(bd)
        this.competencia_dao = new CompetenciaDAO(bd)
        this.empresa_dao = new EmpresaDAO(bd)
    }
}
