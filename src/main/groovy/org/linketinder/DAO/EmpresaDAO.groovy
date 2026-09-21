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
        return banco.return_id_from_busca(busca) { PreparedStatement pst ->
            pst.setString(1, m.nome)
            pst.setString(2, m.email)
            pst.setString(3, m.CNPJ)
            pst.setString(4, m.descricao)
            pst.setString(5, m.senha)
            pst.setInt(6, m.endereco.id)
        }
    }

    boolean delete_empresa_by_id(int id) throws SQLException {
        String busca = """
            delete from empresa where id = ?
        """
        return banco.execute_busca_detect_updates(busca, { PreparedStatement pst -> pst.setInt(1, id) })
    }

    List<Empresa> get_lista_empresa() throws SQLException {
        List<Empresa> empresas = banco.get_lista_tabela("select * from empresa", {}) { ResultSet res ->
            int id = res.getInt("id")
            return new Empresa(
                    id: id,
                    CNPJ: res.getString("CNPJ"),
                    nome: res.getString("nome"),
                    email: res.getString("e_mail"),
                    descricao: res.getString("descricao"),
                    senha: res.getString("senha"),
                    endereco: new Endereco(id: res.getInt("endereco_id"))
            )
        }
        return empresas;
    }

    Empresa get_empresa_by_id(int id) throws SQLException {
        String busca = "select * from empresa where id = ?"

        List<Empresa> empresas = banco.get_lista_tabela(busca, {
            PreparedStatement pst -> pst.setInt(1, id)
        }) { ResultSet res ->
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

        if (!empresas) return null
        return empresas[0]
    }

    int get_empresa_id_by_CNPJ(String CNPJ) throws SQLException {
        String busca = "select id from empresa where CNPJ = ?"

        List<Integer> id = banco.get_lista_tabela(busca, {
            PreparedStatement pst -> pst.setString(1, CNPJ)
        }) {
            ResultSet res -> return res.getInt("id")
        }

        if (!id) return -1
        return id[0]
    }

    boolean update_empresa(Empresa m) throws SQLException {
        if (m == null) return false
        String busca = """
            update empresa set
                nome = ?, e_mail = ?, CNPJ = ?, descricao = ?, senha = ?, endereco_id = ?
            where id = ?
        """

        return banco.execute_busca_detect_updates(busca) { PreparedStatement pst ->
            pst.setString(1, m.nome)
            pst.setString(2, m.email)
            pst.setString(3, m.CNPJ)
            pst.setString(4, m.descricao)
            pst.setString(5, m.senha)
            pst.setInt(6, m.endereco.id)
            pst.setInt(7, m.id)//67
        }
    }
}
