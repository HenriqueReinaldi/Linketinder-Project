package org.linketinder.DAO

import org.linketinder.DAO.old.Create
import org.linketinder.DAO.old.Read
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class EmpresaDAO {
    static Connection conn

    static int return_id_from_busca(String busca, Closure busca_args) throws SQLException{
        //retorna o campo "id" resultante da busca SQL
        String id = ""
        PreparedStatement pst = conn.prepareStatement( busca, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY )
        busca_args(pst)
        ResultSet res = pst.executeQuery()

        res.beforeFirst()
        if (res.next()) id = res.getString("id")

        res.close()
        pst.close()

        try {
            return Integer.parseInt(id)
        } catch (Exception ignored){
            throw new SQLException("retornado id não numérico")
        }
    }
    static void execute_busca(String busca, Closure busca_args) throws SQLException{
        PreparedStatement pst = conn.prepareStatement( busca )
        busca_args(pst)
        pst.execute()
        pst.close()
    }
    static boolean execute_busca_delete(String busca, Closure busca_args) throws SQLException{
        PreparedStatement pst = conn.prepareStatement( busca )
        busca_args(pst)
        int delecoes = pst.executeUpdate()
        pst.close()

        if (delecoes > 0) return true
        return false
    }
    static String get_coluna_from_entrada_id(int id, String tabela, String coluna) throws SQLException{
        String resultado = ""
        String busca = "select * from ${tabela} where id = ?"

        PreparedStatement pst = conn.prepareStatement( busca, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY )
        pst.setInt(1, id)
        ResultSet res = pst.executeQuery()

        res.beforeFirst()
        if (res.next()) resultado = res.getString(coluna)

        res.close()
        pst.close()
        return resultado
    }
    static <Generico> List<Generico> get_lista_tabela(String busca, Closure busca_args, Closure<Generico> construtor) throws SQLException{
        List<Generico> genericos = []

        PreparedStatement pst = conn.prepareStatement(
                busca,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        )
        busca_args(pst)
        ResultSet res = pst.executeQuery()
        res.beforeFirst()
        while (res.next()) {
            Generico g = construtor(res)
            genericos << g
        }
        res.close()
        pst.close()

        return genericos
    }
    static boolean execute_update_busca(String busca, Closure busca_args) throws SQLException{
        PreparedStatement pst = conn.prepareStatement( busca )
        busca_args(pst)
        int alteracoes = pst.executeUpdate()
        pst.close()

        if (alteracoes > 0) return true
        return false
    }


    static int cadastrar_empresa_if_not_exists(Empresa m) throws SQLException{
        /*
            TODO:
            VERIFICAR SE empresa JÁ EXISTE EM SERVICE (CNPJ)
            CRIACAO DO ENDERE4CO EM SERVICE
         */

        if (m == null) return -1;

        String busca = """
            insert into empresa (nome, e_mail, CNPJ, descricao, senha, endereco_id) 
            values (?, ?, ?, ?, ?, ?) returning id 
        """
        return return_id_from_busca(busca) {PreparedStatement pst ->
            pst.setString(1, m.nome)
            pst.setString(2, m.email)
            pst.setString(3, m.CNPJ)
            pst.setString(4, m.descricao)
            pst.setString(5, m.senha)
            pst.setInt(6, m.endereco.id)
        }
    }

    static boolean delete_empresa_by_id(int id) throws SQLException{
        String busca = """
            delete from empresa where id = ?
        """
        return execute_busca_delete(busca, {PreparedStatement pst -> pst.setInt(1, id)})
    }

    static List<Empresa> get_lista_empresa() throws SQLException{
        /*
        TOdo: service completar enderec o
        * */

        List<Empresa> empresas = get_lista_tabela("select * from empresa", {}){ ResultSet res ->
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
    static Empresa get_empresa_by_id(int id) throws SQLException{
        /*
        TOdo: service completar endereco
        * */
        String busca = "select * from empresa where id = ?"

        List<Empresa> empresas = get_lista_tabela(busca, {
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
    static int get_empresa_id_by_CNPJ(String CNPJ) throws SQLException{
        String busca = "select id from empresa where CNPJ = ?"

        List<Integer> id = get_lista_tabela(busca, {
            PreparedStatement pst -> pst.setString(1, CNPJ)
        }) {
            ResultSet res -> return res.getInt("id")
        }

        if (!id) return -1
        return id[0]
    }

    static boolean update_empresa(Empresa m) throws SQLException{
        /*
            TODO:
            VERIFICAR SE empresa JÁ EXISTE EM SERVICE (CNPJ)
            CRIACAO DO ENDERE4CO EM SERVICE
         */


        if (m == null) return false
        String busca = """
            update empresa set
                nome = ?, e_mail = ?, CNPJ = ?, descricao = ?, senha = ?, endereco_id = ?
            where id = ?
        """

        return execute_update_busca(busca) { PreparedStatement pst ->
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
