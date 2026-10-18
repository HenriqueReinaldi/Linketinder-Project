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
        int nicho_id = create_if_not_exists_nicho(v.nicho)
        if (nicho_id < 0) return -1

        String busca = """
            insert into vaga (nome, descricao, endereco_id, empresa_id, nicho_id)
            values (?, ?, ?, ?, ?) returning id
        """
        Closure busca_args = { PreparedStatement pst ->
            pst.setString(1, v.nome)
            pst.setString(2, v.descricao)
            pst.setInt(3, v.endereco.id)
            pst.setInt(4, v.empresa.id)
            pst.setInt(5, nicho_id)
        }

        return banco.executar(busca, busca_args) { ResultSet res ->
            if (!res.next()) return -1
            return res.getInt("id")
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

    List<Vaga> get_lista_vaga() throws SQLException {
        String busca = """
            select 
                v.id AS vaga_id,
                v.nome AS vaga_nome,
                v.descricao AS vaga_descricao,
                v.endereco_id AS vaga_endereco_id,
                e.id AS empresa_id,
                n.nome AS vaga_nicho
            from vaga as v
            join empresa as e on e.id = v.empresa_id
            join nicho as n on n.id = v.nicho_id
        """
        return banco.executar(busca, {}) { ResultSet res ->
            List<Vaga> vagas = []

            while (res.next()) {
                vagas << new Vaga(
                        id: res.getInt("vaga_id"),
                        nome: res.getString("vaga_nome"),
                        descricao: res.getString("vaga_descricao"),
                        nicho: res.getString("vaga_nicho"),
                        endereco: new Endereco(id: res.getInt("vaga_endereco_id")),
                        empresa: new Empresa(id: res.getInt("empresa_id")),
                        competencias_desejadas: null
                )
            }

            return vagas
        }
    }

    Vaga get_vaga_by_id(int id) throws SQLException {
        String busca = """
            select v.*, n.nome as nicho_nome
            from vaga v join nicho n on n.id = v.nicho_id
            where v.id = ?
        """
        Closure busca_args = { PreparedStatement pst -> pst.setInt(1, id) }

        return banco.executar(busca, busca_args) { ResultSet res ->
            if (!res.next()) return null

            return new Vaga(
                    id: res.getInt("id"),
                    nome: res.getString("nome"),
                    descricao: res.getString("descricao"),
                    nicho: res.getString("nicho_nome"),
                    endereco: new Endereco(id: res.getInt("endereco_id")),
                    empresa: new Empresa(id: res.getInt("empresa_id")),
                    competencias_desejadas: null
            )
        }
    }

    List<Vaga> get_lista_vaga_recrutador(int recrutador_id) throws SQLException {
        String busca = """
            select v.id as vaga_id, v.nome as vaga_nome, v.descricao as vaga_descricao,
                   v.endereco_id as vaga_endereco_id, v.empresa_id as empresa_id,
                   nv.nome as vaga_nicho
            from vaga v
            join nicho nv on nv.id = v.nicho_id
            join recrutador r on r.empresa_id = v.empresa_id
            join nicho nr on nr.id = r.nicho_id
            where r.id = ? and (nr.nome = 'geral' or nr.nome = nv.nome)
        """
        return banco.executar(busca, { PreparedStatement pst -> pst.setInt(1, recrutador_id) }) { ResultSet res ->
            List<Vaga> vagas = []
            while (res.next()) {
                vagas << new Vaga(
                        id: res.getInt("vaga_id"),
                        nome: res.getString("vaga_nome"),
                        descricao: res.getString("vaga_descricao"),
                        nicho: res.getString("vaga_nicho"),
                        endereco: new Endereco(id: res.getInt("vaga_endereco_id")),
                        empresa: new Empresa(id: res.getInt("empresa_id")),
                        competencias_desejadas: null
                )
            }
            return vagas
        }
    }

    boolean update_vaga(Vaga v) throws SQLException {
        if (v == null) return false
        String busca = """
            update vaga set
                nome = ?, descricao = ?, endereco_id = ?, empresa_id = ?,
                nicho_id = (select id from nicho where nome = ?)
            where id = ?
        """
        Closure busca_args = { PreparedStatement pst ->
            pst.setString(1, v.nome)
            pst.setString(2, v.descricao)
            pst.setInt(3, v.endereco.id)
            pst.setInt(4, v.empresa.id)
            pst.setString(5, v.nicho)
            pst.setInt(6, v.id)
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
