-- buscas:

with sigma as (
    select d.nome, c.competencia_id from candidato_competencias as c join candidato as d on c.candidato_id = d.id
) select s.nome, c.tecnologia from sigma as s join competencia as c on s.competencia_id = c.id;


--equivalente melhor:
    select d.nome, c.tecnologia 
    from candidato as d
    join candidato_competencias as cc on d.id = cc.candidato_id
    join competencia as c on cc.competencia_id = c.id;

-------------------------------------------
select e.nome as empresa, v.nome, c.tecnologia
from vaga as v
join empresa as e on v.empresa_id = e.id 
join vaga_competencias as vc on vc.vaga_id = v.id
join competencia as c on c.id = vc.competencia_id;


-------------------------------------------
select c.nome, e.cep, p.nome as pais
from candidato as c 
join endereco as e on c.endereco_id = e.id
join pais as p on p.id = e.pais_id;


-------------------------------------------
select c.nome, e.cep, p.nome as pais, es.nome as estado
from candidato as c
join endereco as e on c.endereco_id = e.id
join pais as p on p.id = e.pais_id
join estado as es on es.id = e.estado_id;