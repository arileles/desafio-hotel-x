CREATE TABLE hospedagem (
    id SERIAL PRIMARY KEY,
    data_entrada TIMESTAMP,
    data_saida TIMESTAMP,
    adicional_veiculo boolean,
    valor_total NUMERIC,
    observacoes VARCHAR(255),
    id_hospede SERIAL NOT NULL,
    foreign key (id_hospede) references hospede(id)
);