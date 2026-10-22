package org.linketinder.DAO

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Recrutador

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

@TupleConstructor
class RecrutadorDAO {
    Banco banco

    List<Recrutador> get_lista_recrutador() throws SQLException {
        String busca = """
            select r.*, e.id as empresa_id, n.nome as nicho_nome
            from recrutador r
            join nicho n on n.id = r.nicho_id
            join empresa e on e.id = r.empresa_id
        """
        return banco.executar(busca, {}) { ResultSet res ->
            List<Recrutador> recrutadores = []
            while (res.next()) recrutadores << mapear_recrutador(res)
            return recrutadores
        }
    }

    int get_recrutador_id_by_CPF(String CPF) throws SQLException {
        String busca = "select id from recrutador where CPF = ?"
        return banco.executar(busca, { PreparedStatement pst -> pst.setString(1, CPF) }) { ResultSet res ->
            return res.next() ? res.getInt("id") : -1
        }
    }

    int get_nicho_id_by_nome(String nicho) throws SQLException {
        String busca = "select id from nicho where nome = ?"
        return banco.executar(busca, { PreparedStatement pst -> pst.setString(1, nicho) }) { ResultSet res ->
            return res.next() ? res.getInt("id") : -1
        }
    }

    int create_if_not_exists_nicho(String nome) throws SQLException {
        if (nome == null) return -1

        String busca = """
            insert into nicho (nome) values (?)
            on conflict (nome) do update set nome = nicho.nome
            returning id
        """
        Closure busca_args = { PreparedStatement pst -> pst.setString(1, nome) }

        return banco.executar(busca, busca_args) { ResultSet res ->
            if (!res.next()) return -1
            return res.getInt("id")
        }
    }

    int cadastrar_recrutador_se_nao_existe(Recrutador recrutador) throws SQLException {
        if (recrutador == null || recrutador.empresa == null || recrutador.nicho == null) return -1
        if (get_recrutador_id_by_CPF(recrutador.CPF) >= 0) return -1
        int nicho_id = create_if_not_exists_nicho(recrutador.nicho)
        if (nicho_id < 0) return -1

        String busca = """
            insert into recrutador
                (nome, descricao, e_mail, CPF, data_nascimento, senha, empresa_id, nicho_id)
            values (?, ?, ?, ?, ?, ?, ?, ?) returning id
        """
        Closure busca_args = { PreparedStatement pst ->
            pst.setString(1, recrutador.nome)
            pst.setString(2, recrutador.descricao)
            pst.setString(3, recrutador.email)
            pst.setString(4, recrutador.CPF)
            pst.setDate(5, java.sql.Date.valueOf(recrutador.data_nascimento))
            pst.setString(6, recrutador.senha)
            pst.setInt(7, recrutador.empresa.id)
            pst.setInt(8, nicho_id)
        }
        return banco.executar(busca, busca_args) { ResultSet res ->
            return res.next() ? res.getInt("id") : -1
        }
    }

    boolean delete_recrutador_by_id(int id) throws SQLException {
        return banco.executar_detectar_updates(
                "delete from recrutador where id = ?",
                { PreparedStatement pst -> pst.setInt(1, id) }
        )
    }

    boolean update_recrutador(Recrutador recrutador) throws SQLException {
        if (recrutador == null || recrutador.empresa == null || recrutador.nicho == null) return false
        int nicho_id = get_nicho_id_by_nome(recrutador.nicho)
        if (nicho_id < 0) return false

        String busca = """
            update recrutador set
                nome = ?, descricao = ?, e_mail = ?, CPF = ?, data_nascimento = ?,
                senha = ?, empresa_id = ?, nicho_id = ?
            where id = ?
        """
        Closure busca_args = { PreparedStatement pst ->
            pst.setString(1, recrutador.nome)
            pst.setString(2, recrutador.descricao)
            pst.setString(3, recrutador.email)
            pst.setString(4, recrutador.CPF)
            pst.setDate(5, java.sql.Date.valueOf(recrutador.data_nascimento))
            pst.setString(6, recrutador.senha)
            pst.setInt(7, recrutador.empresa.id)
            pst.setInt(8, nicho_id)
            pst.setInt(9, recrutador.id)
        }
        return banco.executar_detectar_updates(busca, busca_args)
    }

    private static Recrutador mapear_recrutador(ResultSet res) throws SQLException {
        return new Recrutador(
                id: res.getInt("id"),
                nome: res.getString("nome"),
                descricao: res.getString("descricao"),
                email: res.getString("e_mail"),
                CPF: res.getString("CPF"),
                data_nascimento: res.getString("data_nascimento"),
                senha: res.getString("senha"),
                empresa: new Empresa(id: res.getInt("empresa_id")),
                nicho: res.getString("nicho_nome")
        )
    }
}