package org.linketinder.DAO

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Competencia

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

@TupleConstructor
class CompetenciaDAO {
    Banco banco

    int create_if_not_exists_competencia(String tecnologia) throws SQLException {
        String busca = """
            insert into competencia (tecnologia) values (?)
            on conflict (tecnologia) do update set tecnologia = competencia.tecnologia 
            returning id
            """
        Closure busca_args = { PreparedStatement pst -> pst.setString(1, tecnologia)
        }

        return banco.executar(busca, busca_args) { ResultSet res ->
            if (!res.next()) return -1
            return res.getInt("id")
        }
    }

    void cadastrar_competencias_entidade(String entidade, int id_entidade, int id_competencia) throws SQLException {
        String busca = """
            insert into ${entidade}_competencias (${entidade}_id, competencia_id) values (?, ?)
            on conflict (${entidade}_id, competencia_id) do nothing
        """
        Closure busca_args = { PreparedStatement pst ->
            pst.setInt(1, id_entidade)
            pst.setInt(2, id_competencia)
        }

        banco.executar(busca, busca_args) {}
    }

    List<Competencia> get_lista_competencia() throws SQLException {
        return banco.executar("select * from competencia", {}) { ResultSet res ->
            List<Competencia> competencias = []

            while (res.next()) {
                competencias << new Competencia(
                        id: res.getInt("id"),
                        tecnologia: res.getString("tecnologia"),
                )
            }

            return competencias
        }
    }

    List<Competencia> get_lista_competencias_of_entidade(String entidade, int entidade_id) throws SQLException {
        String busca = """
            select * from
            ${entidade}_competencias as cc JOIN competencia as c on cc.competencia_id = c.id
            where cc.${entidade}_id = ?
        """
        Closure busca_args = { PreparedStatement pst -> pst.setInt(1, entidade_id) }

        return banco.executar(busca, busca_args) { ResultSet res ->
            List<Competencia> competencias = []

            while (res.next()) {
                competencias << new Competencia(
                        id: res.getInt("competencia_id"),
                        tecnologia: res.getString("tecnologia")
                )
            }

            return competencias
        }

    }

    boolean update_competencia(Competencia c) throws SQLException {
        if (c == null) return false
        String busca = "update competencia set tecnologia = ? where id = ?"
        Closure busca_args = { PreparedStatement pst ->
            pst.setString(1, c.tecnologia)
            pst.setInt(2, c.id)
        }

        return banco.executar_detectar_updates(busca, busca_args)
    }

    boolean delete_competencia_by_id(int id) throws SQLException {
        String busca = """
            delete from competencia where id = ?
        """
        Closure busca_args = { PreparedStatement pst -> pst.setInt(1, id) }

        return banco.executar_detectar_updates(busca, busca_args)
    }

    boolean delete_entidade_competencias_by_entidadeid(String entidade, int id) throws SQLException {
        String busca = """
            delete from ${entidade}_competencias where ${entidade}_id = ?
        """
        Closure busca_args = { PreparedStatement pst -> pst.setInt(1, id) }

        return banco.executar_detectar_updates(busca, busca_args)
    }
}
