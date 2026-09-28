import { fetch_localstorage, lista_empresas, lista_vagas, update_localstorage } from "./persistencia/dados.js";
import { popular_vagas } from "./service/vagaservice.js";
import { popular_candidatos } from "./service/candidatoservice.js";
import { popular_empresas } from "./service/empresaservice.js";
import { lista_candidatos } from "./persistencia/dados.js";

function dados_init(): void{
    fetch_localstorage();
    
    if (lista_candidatos.length <= 0){
        popular_candidatos();
        popular_empresas();
        popular_vagas();
    }

    update_localstorage();

    console.log(lista_candidatos);
    console.log(lista_empresas);
    console.log(lista_vagas);
}

dados_init();
