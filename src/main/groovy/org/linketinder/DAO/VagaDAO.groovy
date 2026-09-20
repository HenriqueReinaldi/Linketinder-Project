package org.linketinder.DAO

import org.linketinder.DAO.old.Create
import org.linketinder.DAO.old.Delete
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class VagaDAO {
    static Banco banco

    static int cadastrar_vaga_if_not_exists(Vaga v) throws SQLException{
        /*
            TODO:
            CRIACAO DAS TABELAS VAGA_COMPETENCIA EM SERVICE
            CRIACAO DO ENDERE4CO EM SERVICE
         */
        if (v == null) return -1


        String busca = """
            insert into vaga (nome, descricao, endereco_id, empresa_id) 
            values (?, ?, ?, ?) returning id
        """
        int id_novo = banco.return_id_from_busca(busca) {PreparedStatement pst ->
            pst.setString(1, v.nome)
            pst.setString(2, v.descricao)
            pst.setInt(3, v.endereco.id)
            pst.setInt(4, v.empresa.id)
        }
        return id_novo
    }


    static List<Vaga> get_lista_vaga() throws SQLException{
        /*
        todo:
        mandar service completar endereco empresa e competencias desejadas
         */

        List<Vaga> vagas = banco.get_lista_tabela("""
            select 
                v.id AS vaga_id,
                v.nome AS vaga_nome,
                v.descricao AS vaga_descricao,
                v.endereco_id AS vaga_endereco_id,
                e.id AS empresa_id
            from vaga as v join empresa as e on e.id = v.empresa_id """, {}
        ) { ResultSet res ->
            int id = res.getInt("vaga_id")
            return new Vaga(
                    id: id,
                    nome: res.getString("vaga_nome"),
                    descricao: res.getString("vaga_descricao"),
                    endereco: new Endereco(id: res.getInt("vaga_endereco_id")),
                    empresa: new Empresa(id: res.getInt("empresa_id")),
                    competencias_desejadas: null
            )
        }
        return vagas;
    }
    static Vaga get_vaga_by_id(int id) throws SQLException{
        /*
        todo:
        mandar service completar endereco empresa e competencias desejadas
         */

        String busca = "select * from vaga where id = ?"

        List<Vaga> vagas = banco.get_lista_tabela(busca, {
            PreparedStatement pst -> pst.setInt(1, id)
        }) { ResultSet res ->
            int vid = res.getInt("id")
            return new Vaga(
                    id: vid,
                    nome: res.getString("nome"),
                    descricao: res.getString("descricao"),
                    endereco: new Endereco(id: res.getInt("vaga_endereco_id")),
                    empresa: new Empresa(id: res.getInt("empresa_id")),
                    competencias_desejadas: null
            )
        }

        if (!vagas) return null
        return vagas[0]
    }


    static boolean update_vaga(Vaga v) throws SQLException{
        /*
            TODO:
            CRIACAO DAS TABELAS VAGA_COMPETENCIA EM SERVICE
            CRIACAO DO ENDERE4CO EM SERVICE
         */

        if (v == null) return false
        String busca = """
            update vaga set
                nome = ?, descricao = ?, endereco_id = ?, empresa_id = ?
            where id = ?
        """

        boolean troca_aconteceu = banco.execute_update_busca(busca) { PreparedStatement pst ->
            pst.setString(1, v.nome)
            pst.setString(2, v.descricao)
            pst.setInt(3, v.endereco.id)
            pst.setInt(4, v.empresa.id)
            pst.setInt(5, v.id)
        }

        return troca_aconteceu
    }


    static boolean delete_vaga_by_id(int id) throws SQLException{
        String busca = """
            delete from vaga where id = ?
        """
        return banco.execute_busca_delete(busca, {PreparedStatement pst -> pst.setInt(1, id)})
    }
}
