package org.linketinder.database

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Endereco

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class Read {
    static Connection conn

    static Endereco get_endereco_by_id(String id){
        Endereco endereco = null;
        String busca = "select * from endereco where id = ?";

        try{
            PreparedStatement pst = conn.prepareStatement(
                busca,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
            pst.setString(1, id)
            ResultSet res = pst.executeQuery();

            res.beforeFirst()
            if (res.next()) {
               println(res.getString("pais_id"))
            }

            res.close();
            pst.close();
        }
        catch (Exception e) { e.printStackTrace() }

        return endereco;
    }

    static List<Candidato> get_lista_candidatos(){
        List<Candidato> candidatos = [];
        String busca = "select * from candidato";

        try{
            PreparedStatement pst = conn.prepareStatement(
                busca,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            )
            ResultSet res = pst.executeQuery();

            res.beforeFirst()
            while (res.next()) {
                Candidato c = new Candidato(
                    CPF: res.getString("CPF"),
                    idade: 18,
                    competencias: null,
                    nome: res.getString("nome"),
                    sobrenome: res.getString("sobrenome"),
                    data_nascimento: res.getString("data_nascimento"),
                    email: res.getString("e_mail"),
                    descricao: res.getString("descricao"),
                    senha: res.getString("senha"),
                    endereco: get_endereco_by_id(res.getString("endereco_id"))
                )
                candidatos << c;
            }

            res.close();
            pst.close();
        }
        catch (Exception e) { e.printStackTrace()}

        return candidatos;
    }

}
