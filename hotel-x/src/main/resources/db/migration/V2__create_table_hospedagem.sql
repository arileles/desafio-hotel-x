CREATE TABLE hospedagem (
    id SERIAL PRIMARY KEY,
    dataEntrada TIMESTAMP NOT NULL,
    dataSaida TIMESTAMP,
    adicionalVeiculo boolean,
    valorTotal NUMERIC NOT NULL,
    observacoes VARCHAR(255),
    id_hospede SERIAL NOT NULL,
    foreign key (id_hospede) references hospede(id)
);