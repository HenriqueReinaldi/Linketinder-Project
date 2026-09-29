package org.linketinder.DAO

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

@TupleConstructor
class VagaDAO {
    Banco banco

    int cadastrar_vaga(Vaga v) throws SQLException {
        if (v == null) return -1

        String busca = """
            insert into vaga (nome, descricao, endereco_id, empresa_id) 
            values (?, ?, ?, ?) returning id
        """
        Closure busca_args = { PreparedStatement pst ->
            pst.setString(1, v.nome)
            pst.setString(2, v.descricao)
            pst.setInt(3, v.endereco.id)
            pst.setInt(4, v.empresa.id)
        }

        return banco.executar(busca, busca_args) { ResultSet res ->
            if (!res.next()) return -1
            return res.getInt("id")
        }
    }

    List<Vaga> get_lista_vaga() throws SQLException {
        String busca = """
            select 
                v.id AS vaga_id,
                v.nome AS vaga_nome,
                v.descricao AS vaga_descricao,
                v.endereco_id AS vaga_endereco_id,
                e.id AS empresa_id
            from vaga as v join empresa as e on e.id = v.empresa_id 
        """
        return banco.executar(busca, {}) { ResultSet res ->
            List<Vaga> vagas = []

            while (res.next()) {
                vagas << new Vaga(
                        id: res.getInt("vaga_id"),
                        nome: res.getString("vaga_nome"),
                        descricao: res.getString("vaga_descricao"),
                        endereco: new Endereco(id: res.getInt("vaga_endereco_id")),
                        empresa: new Empresa(id: res.getInt("empresa_id")),
                        competencias_desejadas: null
                )
            }

            return vagas
        }
    }

    Vaga get_vaga_by_id(int id) throws SQLException {
        String busca = "select * from vaga where id = ?"
        Closure busca_args = { PreparedStatement pst -> pst.setInt(1, id) }

        return banco.executar(busca, busca_args) { ResultSet res ->
            if (!res.next()) return null

            return new Vaga(
                    id: res.getInt("id"),
                    nome: res.getString("nome"),
                    descricao: res.getString("descricao"),
                    endereco: new Endereco(id: res.getInt("endereco_id")),
                    empresa: new Empresa(id: res.getInt("empresa_id")),
                    competencias_desejadas: null
            )
        }
    }

    boolean update_vaga(Vaga v) throws SQLException {
        if (v == null) return false
        String busca = """
            update vaga set
                nome = ?, descricao = ?, endereco_id = ?, empresa_id = ?
            where id = ?
        """
        Closure busca_args = { PreparedStatement pst ->
            pst.setString(1, v.nome)
            pst.setString(2, v.descricao)
            pst.setInt(3, v.endereco.id)
            pst.setInt(4, v.empresa.id)
            pst.setInt(5, v.id)
        }

        return banco.executar_detectar_updates(busca, busca_args)
    }

    boolean delete_vaga_by_id(int id) throws SQLException {
        String busca = """
            delete from vaga where id = ?
        """
        Closure busca_args = { PreparedStatement pst -> pst.setInt(1, id) }

        return banco.executar_detectar_updates(busca, busca_args)
    }
}
