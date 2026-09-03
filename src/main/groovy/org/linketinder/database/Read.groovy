package org.linketinder.database

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Read {
    static Connection conn

    private static String get_string_tabela_coluna_by_id(String id, String tabela, String coluna){
        String busca = "select * from ${tabela} where id = ?"
        String resultado = ""
        try{
            PreparedStatement pst = conn.prepareStatement( busca, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY )
            pst.setInt(1, Integer.parseInt(id))
            ResultSet res = pst.executeQuery()

            res.beforeFirst()
            if (res.next()) resultado = res.getString(coluna)

            res.close()
            pst.close()
        }
        catch (Exception e) { e.printStackTrace() }
        return resultado
    }
//    private static Map<String, String> get_strings_tabela_colunas_by_id(String id, String tabela, List<String> colunas){
//        String busca = "select * from ${tabela} where id = ?"
//        Map<String, String> resultado = [:]
//
//        try{
//            PreparedStatement pst = conn.prepareStatement( busca, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY )
//            pst.setInt(1, Integer.parseInt(id))
//            ResultSet res = pst.executeQuery()
//
//            res.beforeFirst()
//            if (res.next()) {
//                for (String col : colunas){
//                    resultado[col] = res.getString(col)
//                }
//            }
//
//            res.close()
//            pst.close()
//        }
//        catch (Exception e) { e.printStackTrace() }
//        return resultado
//    }

    static Endereco get_endereco_by_id(String id){
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
        Endereco endereco = null;

        try{
            PreparedStatement pst = conn.prepareStatement( busca, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY )
            pst.setInt(1, Integer.parseInt(id))
            ResultSet res = pst.executeQuery()

            res.beforeFirst()
            if (res.next()) {
                endereco = new Endereco(
                    id: res.getInt("endereco_id"),
                    pais: res.getString("pais_nome"),
                    estado: res.getString("estado_nome"),
                    CEP: res.getString("CEP"),
                )
            }

            res.close()
            pst.close()
        }
        catch (Exception e) { e.printStackTrace() }

        return endereco;
    }
    static List<Competencia> get_competencias(String entidade, int entidade_id){
        List<Competencia> competencias = []
        String busca = "select * from ${entidade}_competencias where ${entidade}_id = ?"
        try{
            PreparedStatement pst = conn.prepareStatement( busca, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY )
            pst.setInt(1, entidade_id)
            ResultSet res = pst.executeQuery()

            res.beforeFirst()
            while (res.next()) {
                int id = res.getInt("competencia_id")

                Competencia comp = new Competencia(
                    id: id,
                    tecnologia: get_string_tabela_coluna_by_id(id.toString(), "competencia", "tecnologia")
                )
                competencias << comp
            }

            res.close()
            pst.close()
        }
        catch (Exception e) { e.printStackTrace() }

        return competencias
    }

    //closeure que deve retornar objeto Generico pra colocar na lista, ResultSet como parametro
    //closure de busca_args para preparar a busca corretamente
    static <Generico> List<Generico> get_lista_tabela(String busca, Closure busca_args, Closure<Generico> construtor){
        List<Generico> genericos = [];
        try{
            PreparedStatement pst = conn.prepareStatement(
                    busca,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )
            busca_args(pst)
            ResultSet res = pst.executeQuery();
            res.beforeFirst()
            while (res.next()) {
                Generico g = construtor(res)
                genericos << g;
            }
            res.close();
            pst.close();
        }
        catch (Exception e) { e.printStackTrace()}
        return genericos;
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
                String data_nascimento = res.getString("data_nascimento")
                LocalDate data_nascimento_t = LocalDate.parse(data_nascimento)
                LocalDate hoje = LocalDate.now()
                int idade = ChronoUnit.YEARS.between(data_nascimento_t, hoje) as int

                int id = res.getInt("id")
                Candidato c = new Candidato(
                    id: id,
                    CPF: res.getString("CPF"),
                    idade: idade,
                    competencias: get_competencias("candidato", id),
                    nome: res.getString("nome"),
                    sobrenome: res.getString("sobrenome"),
                    data_nascimento: data_nascimento,
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
    static List<Empresa> get_lista_empresas(){
        List<Empresa> empresas = [];
        String busca = "select * from empresa";

        try{
            PreparedStatement pst = conn.prepareStatement(
                    busca,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )
            ResultSet res = pst.executeQuery();

            res.beforeFirst()
            while (res.next()) {
                int id = res.getInt("id")
                Empresa e = new Empresa(
                        id: id,
                        CNPJ: res.getString("CNPJ"),
                        nome: res.getString("nome"),
                        email: res.getString("e_mail"),
                        descricao: res.getString("descricao"),
                        senha: res.getString("senha"),
                        endereco: get_endereco_by_id(res.getString("endereco_id"))
                )
                empresas << e;
            }

            res.close();
            pst.close();
        }
        catch (Exception e) { e.printStackTrace()}

        return empresas;
    }
    static List<Vaga> get_lista_vagas(){
        List<Vaga> vagas = [];
        String busca = """
            select 
                v.id AS vaga_id,
                v.nome AS vaga_nome,
                v.descricao AS vaga_descricao,
                v.endereco_id AS vaga_endereco_id,
                e.id AS empresa_id
            from vaga as v join empresa as e on e.id = v.empresa_id
        """

        try{
            PreparedStatement pst = conn.prepareStatement(
                    busca,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )
            ResultSet res = pst.executeQuery();

            res.beforeFirst()
            while (res.next()) {
                int id = res.getInt("vaga_id")

                Vaga v = new Vaga(
                    id: id,
                    nome: res.getString("vaga_nome"),
                    descricao: res.getString("vaga_descricao"),
                    endereco: get_endereco_by_id(res.getString("vaga_endereco_id")),
                    empresa: get_empresa_by_id(res.getString("empresa_id")),
                    competencias_desejadas: get_competencias("vaga", id)
                )
                vagas << v;
            }

            res.close();
            pst.close();
        }
        catch (Exception e) { e.printStackTrace()}

        return vagas;
    }
    static List<Competencia> get_lista_competencias(){
        List<Competencia> competencias = get_lista_tabela("select * from competencia", {}) { ResultSet res ->
            return new Competencia(
                id: res.getInt("id"),
                tecnologia: res.getString("tecnologia"),
            )
        }
        return competencias
    }

    static int get_candidato_id_by_CPF(String CPF){
        String busca = "select id from candidato where CPF = ?"

        List<Integer> id = get_lista_tabela(busca, {
            PreparedStatement pst -> pst.setString(1, CPF)
        }) {
            ResultSet res -> return res.getInt("id")
        }

        if (!id) return -1
        return id[0]
    }
    static int get_empresa_id_by_CNPJ(String CNPJ){
        String busca = "select id from empresa where CNPJ = ?"

        List<Integer> id = get_lista_tabela(busca, {
            PreparedStatement pst -> pst.setString(1, CNPJ)
        }) {
            ResultSet res -> return res.getInt("id")
        }

        if (!id) return -1
        return id[0]
    }


    static int get_endereco_id(Endereco e){
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

        List<Integer> endereco = get_lista_tabela(busca, {
            PreparedStatement pst ->
                pst.setString(1, e.CEP)
                pst.setString(2, e.pais)
                pst.setString(3, e.estado)
        }) { ResultSet res -> return res.getInt("id")}

        if (!endereco) return -1
        return endereco[0]
    }
    static Empresa get_empresa_by_id(String id){
        String busca = "select * from empresa where id = ?"

        List<Empresa> empresas = get_lista_tabela(busca, {
            PreparedStatement pst -> pst.setInt(1, Integer.parseInt(id))
        }) { ResultSet res ->
            return new Empresa(
                id: res.getInt("id"),
                CNPJ: res.getString("CNPJ"),
                nome: res.getString("nome"),
                email: res.getString("e_mail"),
                descricao: res.getString("descricao"),
                senha: res.getString("senha"),
                endereco: get_endereco_by_id(res.getString("endereco_id"))
            )
        }

        if (!empresas) return null
        return empresas[0]
    }
}