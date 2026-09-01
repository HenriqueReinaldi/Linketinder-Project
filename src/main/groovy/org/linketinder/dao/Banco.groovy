package org.linketinder.dao

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

import static java.sql.DriverManager.getConnection as getConnection

class Banco {
    static String nome_banco;
    static Connection conn;
    Create create = new Create();

    static void conectar(){
        Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "postgres");
            props.setProperty("ssl", "false");
            String URL_SERV = "jdbc:postgresql://localhost:5432/${nome_banco}";
            //extremamente seguro.
        try{
            conn = getConnection(URL_SERV, props);
        }catch (Exception e){
            e.printStackTrace();
            System.exit(-42);
        }
    }
    static void desconectar(){
        if (conn != null){
            try{
                conn.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    Banco(String nome_banco){
        this.nome_banco = nome_banco;
        conectar()

        create.conn = conn;

        teste()
    }

    static void teste(){
        String busca = "select * from candidato";

        try{
            PreparedStatement candidatos = conn.prepareStatement(
                    busca,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            ResultSet res = candidatos.executeQuery();

            while (res.next()) {
                System.out.println(res.getString("id"));
            }

            res.close();
            candidatos.close();
        }
        catch (Exception ignored) {}
    }
}

