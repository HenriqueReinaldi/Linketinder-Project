-- User-run migration: add nicho and recruiter support while preserving existing vacancies.
-- This script is intentionally idempotent for repeated inspection on a scratch database.

BEGIN;

CREATE TABLE IF NOT EXISTS nicho (
    id SERIAL PRIMARY KEY,
    nome CHARACTER VARYING(100) UNIQUE NOT NULL
);

INSERT INTO nicho (nome)
SELECT 'geral'
WHERE NOT EXISTS (
    SELECT 1 FROM nicho WHERE nome = 'geral'
);

ALTER TABLE vaga ADD COLUMN IF NOT EXISTS nicho_id INT;

UPDATE vaga
SET nicho_id = (
    SELECT id FROM nicho WHERE nome = 'geral'
)
WHERE nicho_id IS NULL;

ALTER TABLE vaga ALTER COLUMN nicho_id SET NOT NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'vaga_nicho_id_fkey'
    ) THEN
        ALTER TABLE vaga
            ADD CONSTRAINT vaga_nicho_id_fkey
            FOREIGN KEY (nicho_id) REFERENCES nicho(id);
    END IF;
END $$;

CREATE TABLE IF NOT EXISTS recrutador (
    id SERIAL PRIMARY KEY,
    nome CHARACTER VARYING(50) NOT NULL,
    descricao TEXT NOT NULL,
    e_mail CHARACTER VARYING(100) NOT NULL,
    CPF CHARACTER VARYING(11) UNIQUE NOT NULL,
    data_nascimento DATE NOT NULL,
    senha TEXT CHECK (length(senha) >= 6) NOT NULL,
    empresa_id INT NOT NULL REFERENCES empresa(id),
    nicho_id INT NOT NULL REFERENCES nicho(id)
);

COMMIT;
