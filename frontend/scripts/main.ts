import { fetch_localstorage, lista_empresas, lista_vagas, lista_recrutadores, lista_curtidas, update_localstorage } from "./persistencia/dados.js";
import { popular_vagas } from "./service/vagaservice.js";
import { popular_candidatos } from "./service/candidatoservice.js";
import { popular_empresas } from "./service/empresaservice.js";
import { popular_recrutadores } from "./service/recrutadoreservice.js";
import { popular_curtidas } from "./service/curtidaservice.js";
import { lista_candidatos } from "./persistencia/dados.js";

function dados_init(): void{
    fetch_localstorage();
    
    if (lista_candidatos.length <= 0) popular_candidatos();
    if (lista_empresas.length <= 0) popular_empresas();
    if (lista_vagas.length <= 0) popular_vagas();
    if (lista_recrutadores.length <= 0) popular_recrutadores();
    if (lista_curtidas.length <= 0) popular_curtidas();

    update_localstorage();

    console.log(lista_candidatos);
    console.log(lista_empresas);
    console.log(lista_vagas);
    console.log(lista_recrutadores);
    console.log(lista_curtidas);

}

dados_init();
