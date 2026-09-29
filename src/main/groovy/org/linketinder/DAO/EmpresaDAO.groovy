package org.linketinder.DAO

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

@TupleConstructor
class EmpresaDAO {
    Banco banco

    int cadastrar_empresa_se_nao_existe(Empresa m) throws SQLException {
        if (m == null) return -1;
        if (get_empresa_id_by_CNPJ(m.CNPJ) >= 0) return -1

        String busca = """
            insert into empresa (nome, e_mail, CNPJ, descricao, senha, endereco_id) 
            values (?, ?, ?, ?, ?, ?) returning id 
        """
        Closure busca_args = { PreparedStatement pst ->
            pst.setString(1, m.nome)
            pst.setString(2, m.email)
            pst.setString(3, m.CNPJ)
            pst.setString(4, m.descricao)
            pst.setString(5, m.senha)
            pst.setInt(6, m.endereco.id)
        }

        return banco.executar(busca, busca_args) { ResultSet res ->
            if (res.next()) return res.getInt("id")
            return -1
        }
    }

    boolean delete_empresa_by_id(int id) throws SQLException {
        String busca = """
            delete from empresa where id = ?
        """
        Closure busca_args = { PreparedStatement pst -> pst.setInt(1, id) }

        return banco.executar_detectar_updates(busca, busca_args)
    }

    List<Empresa> get_lista_empresa() throws SQLException {
        return banco.executar("select * from empresa", {}) { ResultSet res ->
            List<Empresa> empresas = []

            while (res.next()) {
                empresas << new Empresa(
                        id: res.getInt("id"),
                        CNPJ: res.getString("CNPJ"),
                        nome: res.getString("nome"),
                        email: res.getString("e_mail"),
                        descricao: res.getString("descricao"),
                        senha: res.getString("senha"),
                        endereco: new Endereco(id: res.getInt("endereco_id"))
                )
            }

            return empresas
        }
    }

    Empresa get_empresa_by_id(int id) throws SQLException {
        String busca = "select * from empresa where id = ?"
        Closure busca_args = {
            PreparedStatement pst -> pst.setInt(1, id)
        }

        return banco.executar(busca, busca_args) { ResultSet res ->
            if (!res.next()) return null
            return new Empresa(
                    id: res.getInt("id"),
                    CNPJ: res.getString("CNPJ"),
                    nome: res.getString("nome"),
                    email: res.getString("e_mail"),
                    descricao: res.getString("descricao"),
                    senha: res.getString("senha"),
                    endereco: new Endereco(id: res.getInt("endereco_id"))
            )
        }
    }

    int get_empresa_id_by_CNPJ(String CNPJ) throws SQLException {
        String busca = "select id from empresa where CNPJ = ?"
        Closure busca_args = {
            PreparedStatement pst -> pst.setString(1, CNPJ)
        }

        return banco.executar(busca, busca_args) { ResultSet res ->
            if (!res.next()) return -1
            return res.getInt("id")
        }
    }

    boolean update_empresa(Empresa m) throws SQLException {
        if (m == null) return false
        String busca = """
            update empresa set
                nome = ?, e_mail = ?, CNPJ = ?, descricao = ?, senha = ?, endereco_id = ?
            where id = ?
        """
        Closure busca_args = { PreparedStatement pst ->
            pst.setString(1, m.nome)
            pst.setString(2, m.email)
            pst.setString(3, m.CNPJ)
            pst.setString(4, m.descricao)
            pst.setString(5, m.senha)
            pst.setInt(6, m.endereco.id)
            pst.setInt(7, m.id)//67
        }

        return banco.executar_detectar_updates(busca, busca_args)
    }
}
