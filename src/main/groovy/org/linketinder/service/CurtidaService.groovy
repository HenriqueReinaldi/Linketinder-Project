package org.linketinder.service

import groovy.transform.TupleConstructor
import org.linketinder.DAO.Banco
import org.linketinder.DAO.CandidatoDAO
import org.linketinder.DAO.CurtidaDAO
import org.linketinder.DAO.VagaDAO
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa

@TupleConstructor
class CurtidaService {
    Banco bd
    CurtidaDAO dao
    CandidatoDAO candidato_dao
    VagaDAO vaga_dao

    List<Curtida> get_lista() {
        try {
            List<Curtida> curtidas = dao.get_lista_curtida()

            curtidas.each { Curtida curtida ->
                curtida.candidato = candidato_dao.get_candidato_by_id(curtida.candidato.id)
                curtida.vaga = vaga_dao.get_vaga_by_id(curtida.vaga.id)
            }

            return curtidas
        }
        catch (Exception e) {
            e.printStackTrace()
            return []
        }
    }

    void curtir_como_candidato(Curtida c) {
        try {
            dao.cadastrar_curtida(c)
        }
        catch (Exception e) {
            e.printStackTrace()
        }
    }

    void curtir_como_empresa(Curtida c) {
        try {
            dao.empresa_curtir(c)
        }
        catch (Exception ignored) {
        }
    }

}
