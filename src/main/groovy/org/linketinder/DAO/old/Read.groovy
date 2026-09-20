package org.linketinder.DAO.old


import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class Read {
    static Connection conn





    static int get_candidato_id_by_CPF(String CPF) throws SQLException{
        String busca = "select id from candidato where CPF = ?"

        List<Integer> id = get_lista_tabela(busca, {
            PreparedStatement pst -> pst.setString(1, CPF)
        }) {
            ResultSet res -> return res.getInt("id")
        }

        if (!id) return -1
        return id[0]
    }


}