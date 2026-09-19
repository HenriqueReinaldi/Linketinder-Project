package org.linketinder.DAO

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Curtida
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Read {
    static Connection conn

    private static String get_coluna_from_entrada_id(int id, String tabela, String coluna) throws SQLException{
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

    static List<Candidato> get_lista_candidato() throws SQLException{
        List<Candidato> candidatos = get_lista_tabela("select * from candidato", {}){ ResultSet res ->
            String data_nascimento = res.getString("data_nascimento")
            LocalDate data_nascimento_t = LocalDate.parse(data_nascimento)
            LocalDate hoje = LocalDate.now()
            int idade = ChronoUnit.YEARS.between(data_nascimento_t, hoje) as int

            int id = res.getInt("id")
            return new Candidato(
                id: id,
                CPF: res.getString("CPF"),
                idade: idade,
                competencias: get_lista_competencias_of_entidade("candidato", id),
                nome: res.getString("nome"),
                sobrenome: res.getString("sobrenome"),
                data_nascimento: data_nascimento,
                email: res.getString("e_mail"),
                descricao: res.getString("descricao"),
                senha: res.getString("senha"),
                endereco: get_endereco_by_id(res.getInt("endereco_id"))
            )
        }

        return candidatos;
    }
    static List<Empresa> get_lista_empresa() throws SQLException{
        List<Empresa> empresas = get_lista_tabela("select * from empresa", {}){ ResultSet res ->
            int id = res.getInt("id")
            return new Empresa(
                id: id,
                CNPJ: res.getString("CNPJ"),
                nome: res.getString("nome"),
                email: res.getString("e_mail"),
                descricao: res.getString("descricao"),
                senha: res.getString("senha"),
                endereco: get_endereco_by_id(res.getInt("endereco_id"))
            )
        }
        return empresas;
    }

    static List<Vaga> get_lista_vaga() throws SQLException{
        List<Vaga> vagas = get_lista_tabela("""
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
                endereco: get_endereco_by_id(res.getInt("vaga_endereco_id")),
                empresa: get_empresa_by_id(res.getInt("empresa_id")),
                competencias_desejadas: get_lista_competencias_of_entidade("vaga", id)
            )
        }
        return vagas;
    }
    static List<Competencia> get_lista_competencia() throws SQLException{
        List<Competencia> competencias = get_lista_tabela("select * from competencia", {}) { ResultSet res ->
            return new Competencia(
                id: res.getInt("id"),
                tecnologia: res.getString("tecnologia"),
            )
        }
        return competencias
    }
    static List<Curtida> get_lista_curtida() throws SQLException{
        List<Curtida> curtidas = get_lista_tabela("select * from curtida", {}) { ResultSet res ->
            return new Curtida(
                candidato: get_candidato_by_id(res.getInt("candidato_id")),
                vaga: get_vaga_by_id(res.getInt("vaga_id")),
                empresa_curtiu: res.getBoolean("empresa_curtiu")
            )
        }
        return curtidas
    }
    static List<Competencia> get_lista_competencias_of_entidade(String entidade, int entidade_id)throws SQLException{
        List<Competencia> competencias = get_lista_tabela("select * from ${entidade}_competencias where ${entidade}_id = ?",
                {PreparedStatement pst -> pst.setInt(1, entidade_id) })
        { ResultSet res ->
            int id = res.getInt("competencia_id")
            return new Competencia(
                id: id,
                tecnologia: get_coluna_from_entrada_id(id, "competencia", "tecnologia")
            )
        }
        return competencias
    }

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
    static int get_endereco_id(Endereco e) throws SQLException{
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

    static Empresa get_empresa_by_id(int id) throws SQLException{
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
                endereco: get_endereco_by_id(res.getInt("endereco_id"))
            )
        }

        if (!empresas) return null
        return empresas[0]
    }
    static Candidato get_candidato_by_id(int id) throws SQLException{
        String busca = "select * from candidato where id = ?"

        List<Candidato> candidatos = get_lista_tabela(busca, {
            PreparedStatement pst -> pst.setInt(1, id)
        }) { ResultSet res ->
            String data_nascimento = res.getString("data_nascimento")
            LocalDate data_nascimento_t = LocalDate.parse(data_nascimento)
            LocalDate hoje = LocalDate.now()
            int idade = ChronoUnit.YEARS.between(data_nascimento_t, hoje) as int

            int cid = res.getInt("id")
            return new Candidato(
                    id: cid,
                    CPF: res.getString("CPF"),
                    idade: idade,
                    competencias: get_lista_competencias_of_entidade("candidato", cid),
                    nome: res.getString("nome"),
                    sobrenome: res.getString("sobrenome"),
                    data_nascimento: data_nascimento,
                    email: res.getString("e_mail"),
                    descricao: res.getString("descricao"),
                    senha: res.getString("senha"),
                    endereco: get_endereco_by_id(res.getInt("endereco_id"))
            )
        }

        if (!candidatos) return null
        return candidatos[0]
    }
    static Vaga get_vaga_by_id(int id) throws SQLException{
        String busca = "select * from vaga where id = ?"

        List<Vaga> vagas = get_lista_tabela(busca, {
            PreparedStatement pst -> pst.setInt(1, id)
        }) { ResultSet res ->
            int vid = res.getInt("id")
            return new Vaga(
                id: vid,
                nome: res.getString("nome"),
                descricao: res.getString("descricao"),
                endereco: get_endereco_by_id(res.getInt("endereco_id")),
                empresa: get_empresa_by_id(res.getInt("empresa_id")),
                competencias_desejadas: get_lista_competencias_of_entidade("vaga", vid)
            )
        }

        if (!vagas) return null
        return vagas[0]
    }
    static Endereco get_endereco_by_id(int id) throws SQLException{
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

        List<Endereco> enderecos = get_lista_tabela(busca, {
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