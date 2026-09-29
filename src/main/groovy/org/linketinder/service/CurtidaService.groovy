package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.CandidatoDAO
import org.linketinder.DAO.CurtidaDAO
import org.linketinder.DAO.VagaDAO
import org.linketinder.model.objetos.Curtida

@TupleConstructor
class CurtidaService extends Service {
    CurtidaDAO dao
    CandidatoDAO candidato_dao
    VagaDAO vaga_dao

    List<Curtida> get_lista() {
        executar_seguramente([]) {
            List<Curtida> curtidas = dao.get_lista_curtida()

            curtidas.each { Curtida curtida ->
                curtida.candidato = candidato_dao.get_candidato_by_id(curtida.candidato.id)
                curtida.vaga = vaga_dao.get_vaga_by_id(curtida.vaga.id)
            }

            return curtidas
        }
    }

    boolean curtir_como_candidato(Curtida c) {
        executar_seguramente(false) {
            dao.cadastrar_curtida(c)
            return true
        }
    }

    boolean curtir_como_empresa(Curtida c) {
        executar_seguramente(false) {
            dao.empresa_curtir(c)
            return true
        }
    }

}
