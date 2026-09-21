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
        return banco.return_id_from_busca(busca) { PreparedStatement pst ->
            pst.setString(1, tecnologia)
        }
    }

    void cadastrar_competencias_entidade(String entidade, int id_entidade, int id_competencia) throws SQLException {
        String busca = """
            insert into ${entidade}_competencias (${entidade}_id, competencia_id) values (?, ?)
            on conflict (${entidade}_id, competencia_id) do nothing
        """

        banco.execute_busca(busca) { PreparedStatement pst ->
            pst.setInt(1, id_entidade)
            pst.setInt(2, id_competencia)
        }
    }

    List<Competencia> get_lista_competencia() throws SQLException {
        List<Competencia> competencias = banco.get_lista_tabela("select * from competencia", {}) { ResultSet res ->
            return new Competencia(
                    id: res.getInt("id"),
                    tecnologia: res.getString("tecnologia"),
            )
        }
        return competencias
    }

    List<Competencia> get_lista_competencias_of_entidade(String entidade, int entidade_id) throws SQLException {
        List<Competencia> competencias = banco.get_lista_tabela("select * from ${entidade}_competencias where ${entidade}_id = ?",
                { PreparedStatement pst -> pst.setInt(1, entidade_id) })
                { ResultSet res ->
                    int id = res.getInt("competencia_id")
                    return new Competencia(
                            id: id,
                            tecnologia: banco.get_coluna_from_entrada_id(id, "competencia", "tecnologia")
                    )
                }
        return competencias
    }

    boolean update_competencia(Competencia c) throws SQLException {
        if (c == null) return false
        String busca = "update competencia set tecnologia = ? where id = ?"

        return banco.execute_busca_detect_updates(busca) { PreparedStatement pst ->
            pst.setString(1, c.tecnologia)
            pst.setInt(2, c.id)
        }
    }

    boolean delete_competencia_by_id(int id) throws SQLException {
        String busca = """
            delete from competencia where id = ?
        """
        return banco.execute_busca_detect_updates(busca, { PreparedStatement pst -> pst.setInt(1, id) })
    }

    boolean delete_entidade_competencias_by_entidadeid(String entidade, int id) throws SQLException {
        String busca = """
            delete from ${entidade}_competencias where ${entidade}_id = ?
        """
        return banco.execute_busca_detect_updates(busca, { PreparedStatement pst -> pst.setInt(1, id) })
    }
}
