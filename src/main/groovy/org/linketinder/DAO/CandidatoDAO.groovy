package org.linketinder.DAO

import org.linketinder.DAO.old.Create
import org.linketinder.DAO.old.Delete
import org.linketinder.DAO.old.Read
import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class CandidatoDAO {
    static Banco banco

    static int cadastrar_candidato_if_not_exists(Candidato c) throws SQLException{
        /*
            TODO:
            VERIFICAR SE CANDIDATO JÁ EXISTE EM SERVICE (CPF)
            CRIACAO DAS TABELAS CANDIDATO_COMPETENCIA EM SERVICE
            CRIACAO DO ENDERE4CO EM SERVICE
         */

        if (c == null) return -1

        String busca = """
            insert into candidato (nome, sobrenome, e_mail, CPF, descricao, data_nascimento, senha, endereco_id) 
            values (?, ?, ?, ?, ?, ?, ?, ?) returning id;
        """
        int id_novo = banco.return_id_from_busca(busca) { PreparedStatement pst ->
            pst.setString(1, c.nome)
            pst.setString(2, c.sobrenome)
            pst.setString(3, c.email)
            pst.setString(4, c.CPF)
            pst.setString(5, c.descricao)
            pst.setDate(6, java.sql.Date.valueOf(c.data_nascimento))
            pst.setString(7, c.senha)
            pst.setInt(8, c.endereco.id)
        }
        return id_novo
    }
    static boolean delete_candidato_by_id(int id) throws SQLException{
        String busca = """
            delete from candidato where id = ?
        """
        return banco.execute_busca_delete(busca, {PreparedStatement pst -> pst.setInt(1, id)})
    }

    static List<Candidato> get_lista_candidato() throws SQLException{
        List<Candidato> candidatos = banco.get_lista_tabela("select * from candidato", {}){ ResultSet res ->
            String data_nascimento = res.getString("data_nascimento")
            LocalDate data_nascimento_t = LocalDate.parse(data_nascimento)
            LocalDate hoje = LocalDate.now()
            int idade = ChronoUnit.YEARS.between(data_nascimento_t, hoje) as int

            int id = res.getInt("id")
            return new Candidato(
                    id: id,
                    CPF: res.getString("CPF"),
                    idade: idade,
                    competencias: null,
                    nome: res.getString("nome"),
                    sobrenome: res.getString("sobrenome"),
                    data_nascimento: data_nascimento,
                    email: res.getString("e_mail"),
                    descricao: res.getString("descricao"),
                    senha: res.getString("senha"),
                    endereco: null
            )

            /*
            TODO:
                delegar competencias e endereco para service
             */
        }

        return candidatos;
    }
    static Candidato get_candidato_by_id(int id) throws SQLException{
        String busca = "select * from candidato where id = ?"

        List<Candidato> candidatos = banco.get_lista_tabela(busca, {
            PreparedStatement pst -> pst.setInt(1, id)
        }) { ResultSet res ->
            String data_nascimento = res.getString("data_nascimento")
            LocalDate data_nascimento_t = LocalDate.parse(data_nascimento)
            LocalDate hoje = LocalDate.now()
            int idade = ChronoUnit.YEARS.between(data_nascimento_t, hoje) as int

            int cid = res.getInt("id")
            return new Candidato(
                    id: cid,
                    CPF: res.getString("CPF"),
                    idade: idade,
                    competencias: null,
                    nome: res.getString("nome"),
                    sobrenome: res.getString("sobrenome"),
                    data_nascimento: data_nascimento,
                    email: res.getString("e_mail"),
                    descricao: res.getString("descricao"),
                    senha: res.getString("senha"),
                    endereco: null,
            )

            /*
            TODO:
                delegar competencias e endereco para service
             */
        }

        if (!candidatos) return null
        return candidatos[0]
    }
    static int get_candidato_id_by_CPF(String CPF) throws SQLException{
        String busca = "select id from candidato where CPF = ?"

        List<Integer> id = banco.get_lista_tabela(busca, {
            PreparedStatement pst -> pst.setString(1, CPF)
        }) {
            ResultSet res -> return res.getInt("id")
        }

        if (!id) return -1
        return id[0]
    }


    static boolean update_candidato(Candidato c) throws SQLException{
        /*
            TODO:
            VERIFICAR SE CANDIDATO JÁ EXISTE EM SERVICE (CPF)
            RE-CRIACAO(deletar, criar) DAS TABELAS CANDIDATO_COMPETENCIA EM SERVICE
            CRIACAO DO ENDERE4CO EM SERVICE
         */

        if (c == null) return false
        String busca = """
            update candidato set
                nome = ?, sobrenome = ?, e_mail = ?, CPF = ?, descricao = ?,
                data_nascimento = ?, senha = ?, endereco_id = ?
            where id = ?
        """

        boolean troca_aconteceu = banco.execute_update_busca(busca) { PreparedStatement pst ->
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

        return troca_aconteceu
    }
}
