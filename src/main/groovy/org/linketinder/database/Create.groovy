package org.linketinder.database;

import org.linketinder.model.objetos.Candidato
import org.linketinder.model.objetos.Competencia
import org.linketinder.model.objetos.Empresa
import org.linketinder.model.objetos.Endereco
import org.linketinder.model.objetos.Vaga;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet
import java.time.LocalDate;

class Create {
    static Connection conn

    //funcao "generica" que toma como parametros uma busca, e uma closure para inserir na busca os valores necessarios.
    //retorna o valor "id" da busca
    static int return_id_from_busca(String busca, Closure busca_args){
        String id = ""
        try{
            PreparedStatement pst = conn.prepareStatement( busca, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY )
            busca_args(pst)
            ResultSet res = pst.executeQuery()

            res.beforeFirst()
            if (res.next()) id = res.getString("id")

            res.close()
            pst.close()
        }
        catch (Exception e) { e.printStackTrace() }

        if (id) return Integer.parseInt(id)

        return -1
    }
    //so executa a busca, mais nada.
    static void execute_busca(String busca, Closure busca_args){
        try{
            PreparedStatement pst = conn.prepareStatement( busca )
            busca_args(pst)
            pst.execute()
            pst.close()
        }
        catch (Exception e) { e.printStackTrace() }
    }


    //retornam o ID da entrada. Se entrada ja existe, retornam o id da existente
    static int create_if_not_exists_pais(String nome){
        String busca = """
            insert into pais (nome) values (?)
            on conflict (nome) do update set nome = pais.nome 
            returning id
            """
        return return_id_from_busca(busca) { PreparedStatement pst ->
            pst.setString(1, nome)
        }
    }
    static int create_if_not_exists_estado(String nome){
        String busca = """
            insert into estado (nome) values (?)
            on conflict (nome) do update set nome = estado.nome 
            returning id
            """
        return return_id_from_busca(busca) { PreparedStatement pst ->
            pst.setString(1, nome)
        }
    }
    static int create_if_not_exists_competencia(String tecnologia){
        String busca = """
            insert into competencia (tecnologia) values (?)
            on conflict (tecnologia) do update set tecnologia = competencia.tecnologia 
            returning id
            """
        return return_id_from_busca(busca) { PreparedStatement pst ->
            pst.setString(1, tecnologia)
        }
    }

    static int cadastrar_endereco_if_not_exists(Endereco e){
        if (e == null) return -1

        if (e.pais == null) e.pais = "default"
        if (e.estado == null) e.estado = "default"

        int endereco_id = Read.get_endereco_id(e)
        if (endereco_id != -1) return endereco_id

        int pais_id = create_if_not_exists_pais(e.pais)
        int estado_id = create_if_not_exists_estado(e.estado)

        String busca = "insert into endereco (CEP, pais_id, estado_id) values (?, ?, ?) returning id"
        return return_id_from_busca(busca) { PreparedStatement pst ->
            pst.setString(1, e.CEP)
            pst.setInt(2, pais_id)
            pst.setInt(3, estado_id)
        }
    }

    static int cadastrar_candidato(Candidato c){
        if (c == null) return -1;
        int id = Read.get_candidato_id_by_CPF(c.CPF)
        if (id != -1) return id

        int endereco_id = cadastrar_endereco_if_not_exists(c.endereco)

        List<Integer> competencias_id  = []
        for (Competencia comp : c.competencias){
            competencias_id << create_if_not_exists_competencia(comp.tecnologia)
        }

        String busca = """
            insert into candidato (nome, sobrenome, e_mail, CPF, descricao, data_nascimento, senha, endereco_id) 
            values (?, ?, ?, ?, ?, ?, ?, ?) returning id;
        """

        int id_novo = return_id_from_busca(busca) {PreparedStatement pst ->
            pst.setString(1, c.nome)
            pst.setString(2, c.sobrenome)
            pst.setString(3, c.email)
            pst.setString(4, c.CPF)
            pst.setString(5, c.descricao)
            pst.setDate(6, java.sql.Date.valueOf(c.data_nascimento))
            pst.setString(7, c.senha)
            pst.setInt(8, endereco_id)
        }
        if (id_novo < 0) return id_novo

        for (int competencia_id : competencias_id){
            cadastrar_competencias_entidade("candidato", id_novo, competencia_id)
        }

        return id_novo
    }

    static int cadastrar_empresa(Empresa m){
        if (m == null) return -1;
        int id = Read.get_empresa_id_by_CNPJ(m.CNPJ)
        if (id != -1) return id

        int endereco_id = cadastrar_endereco_if_not_exists(m.endereco)

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
            pst.setInt(6, endereco_id)
        }
    }

    static int cadastrar_vaga(Vaga v){
        if (v == null) return -1

        int endereco_id = cadastrar_endereco_if_not_exists(v.endereco)

        List<Integer> competencias_id  = []
        for (Competencia comp : v.competencias_desejadas){
            competencias_id << create_if_not_exists_competencia(comp.tecnologia)
        }

        String busca = """
            insert into vaga (nome, descricao, endereco_id, empresa_id) 
            values (?, ?, ?, ?) returning id
        """
        int id_novo = return_id_from_busca(busca) {PreparedStatement pst ->
            pst.setString(1, v.nome)
            pst.setString(2, v.descricao)
            pst.setInt(3, endereco_id)
            pst.setInt(4, v.empresa.id)
        }
        if (id_novo < 0) return id_novo

        for (int competencia_id : competencias_id){
            cadastrar_competencias_entidade("vaga", id_novo, competencia_id)
        }

        return id_novo
    }

    static void cadastrar_competencias_entidade(String entidade, int id_entidade, int id_competencia){
        String busca = """
            insert into ${entidade}_competencias (${entidade}_id, competencia_id) values (?, ?)
            on conflict (${entidade}_id, competencia_id) do nothing
        """

        execute_busca(busca) {PreparedStatement pst ->
            pst.setInt(1, id_entidade)
            pst.setInt(2, id_competencia)
        }
    }
}
