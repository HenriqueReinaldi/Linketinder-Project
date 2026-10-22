# Migração de nicho e recrutador

Este diretório contém a migração SQL para a feature de nicho e recrutador. A instrução abaixo foi escrita para revisão e execução manual pelo usuário, conforme o processo do projeto.

## 1) Aplicar a migração em um banco já existente

```bash
psql -d linketinder -f banco_de_dados/migrations/001_add_nicho_e_recrutador.sql
```

## 2) Preparar uma base de teste nova a partir do snapshot do projeto

```bash
createdb linketinder_scratch
psql -d linketinder_scratch -f banco_de_dados/schema.sql
psql -d linketinder_scratch -f banco_de_dados/entrega.sql
```

## 3) Inspecionar a migração e validar o resultado

```bash
psql -d linketinder -c "SELECT id, nome FROM nicho ORDER BY id;"
psql -d linketinder -c "SELECT COUNT(*) AS vagas_sem_nicho FROM vaga WHERE nicho_id IS NULL;"
psql -d linketinder -c "SELECT COUNT(*) AS vagas_geral FROM vaga WHERE nicho_id = (SELECT id FROM nicho WHERE nome = 'geral');"
psql -d linketinder -c "SELECT v.id, v.nome, v.empresa_id, v.nicho_id, n.nome AS nicho FROM vaga v JOIN nicho n ON n.id = v.nicho_id ORDER BY v.id;"
psql -d linketinder -c "SELECT conrelid::regclass AS tabela, conname AS restricao FROM pg_constraint WHERE conname = 'vaga_nicho_id_fkey';"
```

## 4) Validar que a restrição rejeita dados inválidos

```bash
psql -d linketinder -c "INSERT INTO vaga (nome, descricao, endereco_id, empresa_id, nicho_id) VALUES ('vaga invalida', 'desc', 1, 1, NULL);"
# deve falhar por NOT NULL

psql -d linketinder -c "INSERT INTO vaga (nome, descricao, endereco_id, empresa_id, nicho_id) VALUES ('vaga invalida', 'desc', 1, 1, 9999);"
# deve falhar por FK

psql -d linketinder -c "INSERT INTO nicho (nome) VALUES ('geral');"
# deve falhar por UNIQUE
```

## 5) Verificar que as vagas antigas permaneceram intactas

```bash
psql -d linketinder -c "SELECT COUNT(*) AS total_vagas FROM vaga;"
psql -d linketinder -c "SELECT id, nome, descricao, endereco_id, empresa_id FROM vaga ORDER BY id;"
```

> Os comandos acima devem ser executados manualmente pelo usuário em um banco de testes ou em um backup antes de prosseguir para as fases seguintes da implementação.
