-- esse arquivo cria e coloca dados no banco.
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


--POPULACAO:

insert into pais (nome) values ('China');
insert into pais (nome) values ('Brasil');
insert into pais (nome) values ('Uzbequsitão');

insert into estado (nome) values ('Braslandia');
insert into estado (nome) values ('Brasilia');
insert into estado (nome) values ('Brasilendia');

insert into endereco (CEP, pais_id, estado_id) values ('00000000', 1, 2);
insert into endereco (CEP, pais_id, estado_id) values ('11111111', 2, 3);
insert into endereco (CEP, pais_id, estado_id) values ('22222222', 3, 3);

insert into competencia (tecnologia) values ('python');
insert into competencia (tecnologia) values ('cython');
insert into competencia (tecnologia) values ('dython');
insert into competencia (tecnologia) values ('zython');
insert into competencia (tecnologia) values ('java');
insert into competencia (tecnologia) values ('groovy');
insert into competencia (tecnologia) values ('git');
insert into competencia (tecnologia) values ('postgres');
insert into competencia (tecnologia) values ('spock');

insert into candidato (nome, sobrenome, e_mail, CPF, descricao, data_nascimento, senha, endereco_id) values (
	'pedro',
	'pinchas',
	'pedropinchas@mail.mail.com',
	'33322211199',
	'sou um cara muito maneiro que faz coisas teconlogicas.',
	'1930-01-31',
	'SENHA!',
	'1'
);
insert into candidato (nome, sobrenome, e_mail, CPF, descricao, data_nascimento, senha, endereco_id) values (
	'carlos',
	'cantos',
	'carloscantos@mail.mail.com',
	'33322211133',
	'sou um cara muito mais maneiro que faz coisas mais teconlogicas.',
	'1930-02-01',
	'SEGURA!',
	'2'
);
insert into candidato (nome, sobrenome, e_mail, CPF, descricao, data_nascimento, senha, endereco_id) values (
	'sun',
	'tzu',
	'carloscantos@war.com',
	'10000000000',
	'不战而屈人之兵',
	'0001-01-01',
	'tzusun',
	'1'
);
insert into candidato (nome, sobrenome, e_mail, CPF, descricao, data_nascimento, senha, endereco_id) values (
	'pedra',
	'pinchas',
	'pedrapinchas@mail.mail.com',
	'33322211200',
	'Minha maior fraqueza é o papel, mas sou boa contra tesouras.',
	'1967-01-31',
	'SENHA!',
	'3'
);
insert into candidato (nome, sobrenome, e_mail, CPF, descricao, data_nascimento, senha, endereco_id) values (
	'carta',
	'cantos',
	'cartacantos@mail.mail.com',
	'33322211134',
	'eu entrego coisas rapidamente',
	'1901-03-14',
	'SEGURA!',
	'3'
);

insert into empresa (nome, e_mail, CNPJ, descricao, senha, endereco_id) values (
	'empresa tecnologica',
	'emptec@tecemp.emp',
	'00000000000000',
	'maior tec empresa empresa tec do tecnologic world mundo tecnologico (mundo tec)',
	'ftyiugkhlkj tec senha',
	'1'
); 
insert into empresa (nome, e_mail, CNPJ, descricao, senha, endereco_id) values (
	'empresa nao tecnologica',
	'empntec@ntecemp.emp',
	'00000000000067',
	'maior tec nao empresa empresa tec do tecnologic world nao mundo tecnologico (nao mundo tec)',
	'ftyiugkhlkj nao tec senha',
	'3'
); 

insert into empresa (nome, e_mail, CNPJ, descricao, senha, endereco_id) values (
	'FGGGASDEF',
	'fgggasdef@fgggasdef.fgggasdef',
	'00000030000000',
	'quem descobrir op signifciado de fgggasdef ganha uma vaga vitalicia de emprego',
	'fgggasdef',
	'2'
); 


insert into empresa (nome, e_mail, CNPJ, descricao, senha, endereco_id) values (
	'gugle',
	'gugle.com@gucle.gom',
	'00011030000000',
	'gugle com é a mairor empresa de tecnologica web do planeta google maps translate',
	'guoogle',
	'1'
); 


insert into empresa (nome, e_mail, CNPJ, descricao, senha, endereco_id) values (
	'linketinder',
	'linketinder@link.tinder',
	'10000000000000',
	'A melhor solução para recrutamento às cegas.',
	'lientkedetinre',
	'2'
); 

insert into candidato_competencias (candidato_id, competencia_id) values (1, 3);
insert into candidato_competencias (candidato_id, competencia_id) values (1, 8);
insert into candidato_competencias (candidato_id, competencia_id) values (1, 5);
insert into candidato_competencias (candidato_id, competencia_id) values (2, 5);
insert into candidato_competencias (candidato_id, competencia_id) values (3, 4);
insert into candidato_competencias (candidato_id, competencia_id) values (3, 1);
insert into candidato_competencias (candidato_id, competencia_id) values (4, 4);
insert into candidato_competencias (candidato_id, competencia_id) values (4, 1);
insert into candidato_competencias (candidato_id, competencia_id) values (4, 2);
insert into candidato_competencias (candidato_id, competencia_id) values (5, 9);


insert into vaga (nome, descricao, endereco_id, empresa_id) values (
	'melhor vaga tecweb',
	'descritiva',
	'1',
	'1'
);

insert into vaga (nome, descricao, endereco_id, empresa_id) values (
	'linketinder dev',
	'deve dev o development do linketinder development dev team',
	'2',
	'5'
);

insert into vaga_competencias (vaga_id, competencia_id) values (2, 9);
insert into vaga_competencias (vaga_id, competencia_id) values (2, 8);
insert into vaga_competencias (vaga_id, competencia_id) values (2, 6);
insert into vaga_competencias (vaga_id, competencia_id) values (2, 7);
insert into vaga_competencias (vaga_id, competencia_id) values (1, 1);
insert into vaga_competencias (vaga_id, competencia_id) values (1, 2);
insert into vaga_competencias (vaga_id, competencia_id) values (1, 3);
insert into vaga_competencias (vaga_id, competencia_id) values (1, 4);




