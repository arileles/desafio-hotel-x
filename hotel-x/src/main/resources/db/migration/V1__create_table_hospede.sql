CREATE TABLE hospede (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(50) UNIQUE NOT NULL,
    telefone VARCHAR(50) NOT NULL,
    valor_total_gasto NUMERIC DEFAULT 0,
    valor_ultima_hospedagem NUMERIC DEFAULT 0
);