package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.CompetenciaDAO
import org.linketinder.DAO.EmpresaDAO
import org.linketinder.DAO.EnderecoDAO
import org.linketinder.DAO.VagaDAO
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Vaga

@TupleConstructor
class VagaService extends Service {
    VagaDAO dao
    EnderecoDAO endereco_dao
    CompetenciaDAO competencia_dao
    EmpresaDAO empresa_dao

    List<Vaga> get_lista() {
        executar_seguramente([]) {
            List<Vaga> vagas = dao.get_lista_vaga()

            vagas.each { Vaga vaga ->
                vaga.endereco = endereco_dao.get_endereco_by_id(vaga.endereco.id)
                vaga.competencias_desejadas = competencia_dao.get_lista_competencias_of_entidade("vaga", vaga.id)
                vaga.empresa = empresa_dao.get_empresa_by_id(vaga.empresa.id)
            }

            return vagas
        }
    }

    void cadastrar(Vaga v) {
        executar_seguramente() {
            int endereco_id = endereco_dao.cadastrar_endereco_se_nao_existe(v.endereco)
            if (endereco_id < 0) return
            v.endereco.id = endereco_id

            int vaga_id = dao.cadastrar_vaga(v)
            if (vaga_id < 0) return

            List<Integer> competencias_id = []
            for (Competencia comp : v.competencias_desejadas) {
                competencias_id << competencia_dao.create_if_not_exists_competencia(comp.tecnologia)
            }
            for (int competencia_id : competencias_id) {
                competencia_dao.cadastrar_competencias_entidade("vaga", vaga_id, competencia_id)
            }
        }
    }

    void deletar(int id) {
        executar_seguramente() {
            dao.delete_vaga_by_id(id)
        }
    }

    void update(Vaga v) {
        executar_seguramente() {
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
    }

    Vaga get_by_id(String id) {
        executar_seguramente() {
            return dao.get_vaga_by_id(Integer.parseInt(id))
        }
    }

}
