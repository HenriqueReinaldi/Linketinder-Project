package org.linketinder.DAO

import groovy.transform.TupleConstructor
import org.linketinder.model.objetos.Endereco

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

@TupleConstructor
class EnderecoDAO {
    Banco banco

    int create_if_not_exists_pais(String nome) throws SQLException {
        String busca = """
            insert into pais (nome) values (?)
            on conflict (nome) do update set nome = pais.nome 
            returning id
            """
        return banco.return_id_from_busca(busca) { PreparedStatement pst ->
            pst.setString(1, nome)
        }
    }

    int create_if_not_exists_estado(String nome) throws SQLException {
        String busca = """
            insert into estado (nome) values (?)
            on conflict (nome) do update set nome = estado.nome 
            returning id
            """
        return banco.return_id_from_busca(busca) { PreparedStatement pst ->
            pst.setString(1, nome)
        }
    }

    int cadastrar_endereco_se_nao_existe(Endereco e) throws SQLException {
        if (e == null) return -1

        if (e.pais == null) e.pais = "default"
        if (e.estado == null) e.estado = "default"

        int endereco_id = get_endereco_id(e)
        if (endereco_id != -1) return endereco_id

        int pais_id = create_if_not_exists_pais(e.pais)
        int estado_id = create_if_not_exists_estado(e.estado)

        String busca = "insert into endereco (CEP, pais_id, estado_id) values (?, ?, ?) returning id"
        return banco.return_id_from_busca(busca) { PreparedStatement pst ->
            pst.setString(1, e.CEP)
            pst.setInt(2, pais_id)
            pst.setInt(3, estado_id)
        }
    }

    int get_endereco_id(Endereco e) throws SQLException {
        String busca = """
            select 
                e.id as id,
                p.nome as pais_nome,
                es.nome as estado_nome,
                CEP
            from endereco as e
            join pais p on e.pais_id = p.id 
            join estado es on e.estado_id = es.id
            where CEP = ?
            and p.nome = ?
            and es.nome = ?
        """

        List<Integer> endereco = banco.get_lista_tabela(busca, {
            PreparedStatement pst ->
                pst.setString(1, e.CEP)
                pst.setString(2, e.pais)
                pst.setString(3, e.estado)
        }) { ResultSet res -> return res.getInt("id") }

        if (!endereco) return -1
        return endereco[0]
    }

    Endereco get_endereco_by_id(int id) throws SQLException {
        String busca = """
            select 
                e.id as endereco_id,
                p.nome as pais_nome,
                es.nome as estado_nome,
                CEP
            from endereco as e
            join pais p on e.pais_id = p.id 
            join estado es on e.estado_id = es.id
            where e.id = ?
        """

        List<Endereco> enderecos = banco.get_lista_tabela(busca, {
            PreparedStatement pst -> pst.setInt(1, id)
        }) { ResultSet res ->
            return new Endereco(
                    id: res.getInt("endereco_id"),
                    pais: res.getString("pais_nome"),
                    estado: res.getString("estado_nome"),
                    CEP: res.getString("CEP"),
            )
        }

        if (!enderecos) return null
        return enderecos[0]
    }
}
