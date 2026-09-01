package org.linketinder.dao;

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Empresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class Create {
    static Connection conn


    static boolean criar_tabela_candidato(Candidato c){
        if (c == null) return false;

        String query = """
            insert into candidato (nome, sobrenome, e_mail, CPF, descricao, data_nascimento, senha, endereco)
            values (?, ?, ?, ?, ?, ?, ?, ?) returning id
         """

        try{
            PreparedStatement pst = conn.prepareStatement(query)
            pst.setString(1, c.nome)
            pst.setString(2, c.sobrenome)
            pst.setString(3, c.email)
            pst.setString(4, c.CPF)
            pst.setString(5, c.descricao)
            pst.setString(6, c.data_nascimento)
            //criar um endereco

            ResultSet res = pst.executeQuery();

            while (res.next()) {
                System.out.println(res.getString("id"));
            }

            res.close();
            pst.close();
        }
        catch (Exception ignored) {return false}

        return true
    }


    static boolean cadastrar_candidato(Candidato c){
        criar_tabela_candidato(c)
    }

    static boolean cadastrar_empresa(Empresa m){
        if (m == null) return false;


        return true
    }
}
