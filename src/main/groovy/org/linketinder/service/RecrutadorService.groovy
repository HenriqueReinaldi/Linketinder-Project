package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.EmpresaDAO
import org.linketinder.DAO.RecrutadorDAO
import org.linketinder.model.objetos.Recrutador

@TupleConstructor
class RecrutadorService extends Service {
    RecrutadorDAO dao
    EmpresaDAO empresa_dao

    List<Recrutador> get_lista() {
        executar_seguramente([]) {
            List<Recrutador> recrutadores = dao.get_lista_recrutador()
            recrutadores.each { Recrutador recrutador ->
                recrutador.empresa = empresa_dao.get_empresa_by_id(recrutador.empresa.id)
            }
            return recrutadores
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
