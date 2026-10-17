package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Recrutador

@TupleConstructor
class RecrutadorService extends Service {
    List<Recrutador> get_lista() {
        executar_seguramente([]) {
            return []
        }
    }

    boolean cadastrar(Recrutador recrutador) {
        executar_seguramente(false) {
            return true
        }
    }

    boolean deletar(int id) {
        executar_seguramente(false) {
            return true
        }
    }

    boolean update(Recrutador recrutador) {
        executar_seguramente(false) {
            return true
        }
    }
}
