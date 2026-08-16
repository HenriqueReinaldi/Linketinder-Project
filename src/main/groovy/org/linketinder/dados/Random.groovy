package org.linketinder.dados

class Random {
    List<String> nomes = ["pedro", "heitor", "carlos", "roberto", "alberto", "carleto", "materno", "laberno", "maria", "eduarda", "amanda", "sofia"]
    List<String> sobrenomes = ["silva", "santos", "oliverira", "lima", "pinho", "pinto", "siksevenildo", "costa", "latrina", "nomeado", "rocha", "rascunho"]
    List<String> emp_nomes = ["ultra", "mega", "super", "desc", "org", "inc", "dom", "eleva", "trem", "asdf", "maia", "inca", "demmn", "fads", "empresa", "dot", ".", ['1'..'5']].flatten()
    List<String> competencias = ["Java", "Groovy", "Gradle", "Git", "Metodologias ágeis", "Testes", "JS", "TS", "PostgreSQL", "Regex", "SOLID", "REST", "Grails", "Angular", "kafka"]
    List<String> mails = ["yahoo", "google", "mail", "proton", "youtube", "email", "internet", "uau", "dotcom"]

    private static int random_int(int mn, int mx){
        new java.util.Random().nextInt(mx-mn) + mn;
    }

    String get_random_nome(){
        def nome =  nomes[random_int(0, nomes.size())];
        def sobrenome = sobrenomes[random_int(0, nomes.size())]

        "$nome $sobrenome"
    }

    String get_random_empresa_nome(){
        def a = emp_nomes[random_int(0, emp_nomes.size())]
        def b = emp_nomes[random_int(0, emp_nomes.size())]

        "$a$b"
    }

    List<String> get_random_competencias(){
        int index_inicio = random_int(0, competencias.size()-5);
        int index_fim = random_int(index_inicio, competencias.size());

        competencias.subList(index_inicio, index_fim)
    }

    List <String> get_fixed_competencias(int index_inicio, int index_fim){
        competencias.subList(index_inicio, index_fim)
    }

    String get_random_email(){
        def a =  nomes[random_int(0, nomes.size())];
        def b = mails[random_int(0, mails.size())]
        "$a@${b}.com.br.gov.etc.wow.fim"
    }

    static int get_random_idade(){
        random_int(16, 106)
    }

    static String get_random_cpf(){
        "000.000.000-00"
    }

    static String get_random_cnpj(){
        "00.000.000/0000-00"
    }

    static String get_random_cep(){
        "00000-000"
    }


}
