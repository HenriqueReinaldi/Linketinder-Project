package org.linketinder.DAO

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Vaga

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

@TupleConstructor
class CurtidaDAO {
    Banco banco

    void cadastrar_curtida(Curtida c) throws SQLException {
        if (c == null) return

        String busca = """
            insert into curtida (candidato_id, vaga_id) 
            values (?, ?) on conflict (candidato_id, vaga_id) do nothing
        """
        Closure busca_args = { PreparedStatement pst ->
            pst.setInt(1, c.candidato.id)
            pst.setInt(2, c.vaga.id)
        }

        banco.executar(busca, busca_args, {})
    }

    List<Curtida> get_lista_curtida() throws SQLException {
        return banco.executar("select * from curtida", {}) { ResultSet res ->
            List<Curtida> curtidas = []

            while (res.next()) {
                curtidas << new Curtida(
                        candidato: new Candidato(id: res.getInt("candidato_id")),
                        vaga: new Vaga(id: res.getInt("vaga_id")),
                        empresa_curtiu: res.getBoolean("empresa_curtiu")
                )
            }

            return curtidas
        }
    }

    boolean empresa_curtir(Curtida c) throws SQLException {
        String busca = "update curtida set empresa_curtiu = true where candidato_id = ? and vaga_id = ?"
        Closure busca_args = { PreparedStatement pst ->
            pst.setInt(1, c.candidato.id)
            pst.setInt(2, c.vaga.id)
        }
        return banco.executar_detectar_updates(busca, busca_args)
    }
}
