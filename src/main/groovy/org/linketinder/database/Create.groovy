package org.linketinder.database;

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Empresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class Create {
    static Connection conn
    private static int criar_tabela_return_id(PreparedStatement pst){
        ResultSet res = pst.executeQuery();
        int id = -1
        while (res.next()) {
            id = res.getInt("id");
        }
        res.close();
        return id
    }

    static boolean criar_tabela_pais(String pais){
        if (pais == null) return false;

        String query = """
            insert into pais (nome) values (?) returning id, nome
        """

        try{
            PreparedStatement pst = conn.prepareStatement(query)
            pst.setString(1, pais)

            int id = criar_tabela_return_id(pst)
            System.out.println(id);

            pst.close();
        }
        catch (Exception ignored) {return false}

        return true
    }

    static boolean criar_tabela_estado(String estado){
        return true
    }


    static boolean criar_tabela_candidato(Candidato c){
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
