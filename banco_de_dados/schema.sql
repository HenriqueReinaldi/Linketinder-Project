create table pais(
    id serial primary key,
    nome character varying(50) unique not null
);

create table estado(
   id serial primary key,
   nome character varying(100) unique not null
);

create table endereco(
	id serial primary key,
	CEP character varying(8) not null,
	pais_id int references pais(id) not null,
    estado_id int references estado(id) not null
);


create table candidato(
	id serial primary key,
	nome character varying(50) not null,
	sobrenome character varying(50) not null,
	e_mail character varying(100) not null,
	CPF character varying(11) unique not null,
	
	descricao TEXT not null,
	data_nascimento date not null,
	
	senha TEXT CHECK (length(senha) >= 6) not null,
	
	endereco_id int references endereco(id) not null
);

create table competencia(
	id serial primary key,
	tecnologia character varying(100) unique not null
);

create table candidato_competencias(
	candidato_id int not null references candidato(id) on delete cascade,
	competencia_id int not null references competencia(id) on delete cascade,

	primary key(candidato_id, competencia_id)
);

create table empresa(
	id serial primary key,
	nome character varying(100) not null,
	e_mail character varying(100) not null,
	CNPJ character varying(14) unique not null,
	
	descricao TEXT not null,
	senha TEXT CHECK (length(senha) >= 6) not null,
	
	endereco_id int references endereco(id) not null
);

create table vaga(
	id serial primary key,
	nome character varying(100) not null,
	descricao TEXT not null,
	
	endereco_id int references endereco(id) not null,
	empresa_id int references empresa(id) not null
);

create table vaga_competencias(
	vaga_id int not null references vaga(id) on delete cascade,
	competencia_id int not null references competencia(id) on delete cascade,
	
	primary key(vaga_id, competencia_id)
);
