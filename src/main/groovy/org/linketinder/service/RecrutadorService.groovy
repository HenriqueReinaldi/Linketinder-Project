package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.CurtidaDAO
import org.linketinder.DAO.RecrutadorDAO
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Recrutador

@TupleConstructor
class RecrutadorService extends Service {
    RecrutadorDAO dao
    VagaService vaga_service
    CurtidaDAO curtida_dao

    List<Recrutador> get_lista() {
        executar_seguramente([]) {
            return dao.get_lista_recrutador()
        }
    }

    boolean cadastrar(Recrutador recrutador) {
        executar_seguramente(false) {
            return dao.cadastrar_recrutador_se_nao_existe(recrutador) >= 0
        }
    }

    boolean deletar(int id) {
        executar_seguramente(false) {
            dao.delete_recrutador_by_id(id)
            return true
        }
    }

    boolean update(Recrutador recrutador) {
        executar_seguramente(false) {
            dao.update_recrutador(recrutador)
            return true
        }
    }
}
