package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.Banco
import org.linketinder.DAO.CompetenciaDAO
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia

@TupleConstructor
class CompetenciaService extends Service {
    CompetenciaDAO dao

    List<Competencia> get_lista() {
        executar_seguramente([]) {
            return dao.get_lista_competencia()
        }
    }

    void deletar(int id) {
        executar_seguramente() {
            dao.delete_competencia_by_id(id)
        }
    }

    void update(Competencia c) {
        executar_seguramente() {
            dao.update_competencia(c)
        }
    }

}
