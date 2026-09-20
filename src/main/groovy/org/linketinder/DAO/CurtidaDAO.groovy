package org.linketinder.DAO

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Vaga

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class CurtidaDAO {
    static Banco banco

    static void cadastrar_curtida(Curtida c) throws SQLException{
        if (c == null) return

        String busca = """
            insert into curtida (candidato_id, vaga_id) 
            values (?, ?) on conflict (candidato_id, vaga_id) do nothing
        """

        banco.xecute_busca(busca) {PreparedStatement pst ->
            pst.setInt(1, c.candidato.id)
            pst.setInt(2, c.vaga.id)
        }

    }

    static List<Curtida> get_lista_curtida() throws SQLException{
        /*
            TODO:
            completar candidato e vaga em service.
         */

        List<Curtida> curtidas = banco.get_lista_tabela("select * from curtida", {}) { ResultSet res ->
            return new Curtida(
                    candidato: new Candidato(id: res.getInt("candidato_id")),
                    vaga: new Vaga(id: res.getInt("vaga_id")),
                    empresa_curtiu: res.getBoolean("empresa_curtiu")
            )
        }
        return curtidas
    }

    static boolean empresa_curtir(Curtida c) throws SQLException{
        String busca = "update curtida set empresa_curtiu = true where candidato_id = ? and vaga_id = ?"

        return banco.execute_update_busca(busca) { PreparedStatement pst ->
            pst.setInt(1, c.candidato.id)
            pst.setInt(2, c.vaga.id)
        }
    }
}
