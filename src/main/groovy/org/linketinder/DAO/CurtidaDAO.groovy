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

    List<Curtida> get_lista_curtida_recrutador(int recrutador_id) throws SQLException {
        String busca = """
            select c.candidato_id, c.vaga_id, c.empresa_curtiu
            from curtida c
            join vaga v on v.id = c.vaga_id
            join recrutador r on r.empresa_id = v.empresa_id
            join nicho nr on nr.id = r.nicho_id
            join nicho nv on nv.id = v.nicho_id
            where r.id = ? and (nr.nome = 'geral' or nr.nome = nv.nome)
        """

        return banco.executar(busca, { PreparedStatement pst -> pst.setInt(1, recrutador_id) }) { ResultSet res ->
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

    boolean recrutador_curtir(Curtida c, int recrutador_id) throws SQLException {
        if (c == null || c.candidato == null || c.vaga == null) return false
        String busca = """
            update curtida c
            set empresa_curtiu = true
            where c.candidato_id = ? and c.vaga_id = ?
              and exists (
                  select 1
                  from recrutador r
                  join vaga v on v.empresa_id = r.empresa_id and v.id = c.vaga_id
                  join nicho nr on nr.id = r.nicho_id
                  join nicho nv on nv.id = v.nicho_id
                  where r.id = ? and (nr.nome = 'geral' or nr.nome = nv.nome)
              )
        """
        Closure busca_args = { PreparedStatement pst ->
            pst.setInt(1, c.candidato.id)
            pst.setInt(2, c.vaga.id)
            pst.setInt(3, recrutador_id)
        }
        return banco.executar_detectar_updates(busca, busca_args)
    }
}
