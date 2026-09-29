package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.CompetenciaDAO
import org.linketinder.model.objetos.Competencia

@TupleConstructor
class CompetenciaService extends Service {
    CompetenciaDAO dao

    List<Competencia> get_lista() {
        executar_seguramente([]) {
            return dao.get_lista_competencia()
        }
    }

    boolean deletar(int id) {
        executar_seguramente(false) {
            dao.delete_competencia_by_id(id)
            return true
        }
    }

    boolean update(Competencia c) {
        executar_seguramente(false) {
            dao.update_competencia(c)
            return true
        }
    }

}
