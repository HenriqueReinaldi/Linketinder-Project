package org.linketinder.DAO

import groovy.transform.TupleConstructor

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Endereco

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@TupleConstructor
class CandidatoDAO {
    Banco banco

    int cadastrar_candidato_se_nao_existe(Candidato c) throws SQLException {
        if (c == null) return -1
        if (get_candidato_id_by_CPF(c.CPF) >= 0) return -1

        String busca = """
            insert into candidato (nome, sobrenome, e_mail, CPF, descricao, data_nascimento, senha, endereco_id) 
            values (?, ?, ?, ?, ?, ?, ?, ?) returning id;
        """
        Closure busca_args = { PreparedStatement pst ->
            pst.setString(1, c.nome)
            pst.setString(2, c.sobrenome)
            pst.setString(3, c.email)
            pst.setString(4, c.CPF)
            pst.setString(5, c.descricao)
            pst.setDate(6, java.sql.Date.valueOf(c.data_nascimento))
            pst.setString(7, c.senha)
            pst.setInt(8, c.endereco.id)
        }

        return banco.executar(busca, busca_args) { ResultSet res ->
            if (res.next()) return res.getInt("id")
            return -1
        }
    }

    boolean delete_candidato_by_id(int id) throws SQLException {
        String busca = """
            delete from candidato where id = ?
        """
        Closure busca_args = { PreparedStatement pst -> pst.setInt(1, id) }

        return banco.executar_detectar_updates(busca, busca_args)
    }

    List<Candidato> get_lista_candidato() throws SQLException {
        return banco.executar("select * from candidato", {}) { ResultSet res ->
            List<Candidato> candidatos = []

            while (res.next()) {
                String data_nasc = res.getString("data_nascimento")
                candidatos << new Candidato(
                        id: res.getInt("id"),
                        CPF: res.getString("CPF"),
                        nome: res.getString("nome"),
                        sobrenome: res.getString("sobrenome"),
                        email: res.getString("e_mail"),
                        descricao: res.getString("descricao"),
                        senha: res.getString("senha"),
                        data_nascimento: data_nasc,
                        idade: ChronoUnit.YEARS.between(LocalDate.parse(data_nasc), LocalDate.now()) as int,
                        competencias: [],
                        endereco: new Endereco(id: res.getInt("endereco_id"))
                )
            }

            return candidatos
        }
    }

    Candidato get_candidato_by_id(int id) throws SQLException {
        String busca = "select * from candidato where id = ?"
        Closure busca_args = { PreparedStatement pst -> pst.setInt(1, id) }

        return banco.executar(busca, busca_args) { ResultSet res ->
            if (!res.next()) return null

            String data_nascimento = res.getString("data_nascimento")

            return new Candidato(
                    id: res.getInt("id"),
                    CPF: res.getString("CPF"),
                    idade: ChronoUnit.YEARS.between(LocalDate.parse(data_nascimento), LocalDate.now()) as int,
                    nome: res.getString("nome"),
                    sobrenome: res.getString("sobrenome"),
                    data_nascimento: data_nascimento,
                    email: res.getString("e_mail"),
                    descricao: res.getString("descricao"),
                    senha: res.getString("senha")
            )
        }
    }

    int get_candidato_id_by_CPF(String CPF) throws SQLException {
        String busca = "select id from candidato where CPF = ?"
        Closure busca_args = {
            PreparedStatement pst -> pst.setString(1, CPF)
        }

        return banco.executar(busca, busca_args) { ResultSet res ->
            if (!res.next()) return -1
            return res.getInt("id")
        }
    }

    boolean update_candidato(Candidato c) throws SQLException {
        if (c == null) return false

        String busca = """
            update candidato set
                nome = ?, sobrenome = ?, e_mail = ?, CPF = ?, descricao = ?,
                data_nascimento = ?, senha = ?, endereco_id = ?
            where id = ?
        """
        Closure busca_args = { PreparedStatement pst ->
            pst.setString(1, c.nome)
            pst.setString(2, c.sobrenome)
            pst.setString(3, c.email)
            pst.setString(4, c.CPF)
            pst.setString(5, c.descricao)
            pst.setDate(6, java.sql.Date.valueOf(c.data_nascimento))
            pst.setString(7, c.senha)
            pst.setInt(8, c.endereco.id)
            pst.setInt(9, c.id)
        }

        return banco.executar_detectar_updates(busca, busca_args)
    }
}
