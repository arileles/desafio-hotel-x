<!DOCTYPE html>
<html lang="pt-br">
<body>
    <div class="container">
        <h1>Olá, bem-vindo ao Hotel X!</h1>
        <p>Este repositório contém a aplicação de backend e frontend do Hotel X, desenvolvida em Java com Spring Boot e Angular.</p>
        <h2>Pré-requisitos</h2>
        <p>Para executar o projeto, você precisará ter o seguinte instalado:</p>
        <ul>
            <li><strong>Java Development Kit (JDK) 17</strong>: A versão do Spring Boot usada no <code>pom.xml</code> (3.5.4) é compatível com o Java 17.</li>
            <li><strong>PostgreSQL</strong>: O banco de dados configurado no <code>application.properties</code> está definido para PostgreSQL, rodando na porta padrão (5432).</li>
            <li><strong>Node.js</strong>: Necessário para executar o ambiente do Angular.</li>
        </ul>
        <h2>Configuração do Banco de Dados</h2>
        <p>O projeto utiliza o Flyway para gerenciar as migrações do banco de dados.</p>
        <h2>Endpoints da API (Backend)</h2>
        <p>A API RESTful do backend oferece os seguintes endpoints:</p>
        <h3>Hóspedes (<code>/hospede</code>)</h3>
        <ul>
            <li><code>GET /hospede</code>: Listar todos os hóspedes.</li>
            <li><code>GET /hospede/cpf/{cpf}</code>: Buscar hóspede por CPF.</li>
           <li><code>GET /hospede/nome/{nome}</code>: Buscar hóspede por Nome.</li>
           <li><code>GET /hospede/telefone/{telefone}</code>: Buscar hóspede por Telefone.</li>
            <li><code>POST /hospede</code>: Criar um novo hóspede.</li>
            <li><code>PUT /hospede</code>: Atualizar um hóspede.</li>
            <li><code>DELETE /hospede/{id}</code>: Deletar um hóspede por ID.</li>
        </ul>
        <h3>Hospedagens (<code>/hospedagem</code>)</h3>
        <ul>
            <li><code>GET /hospedagem/ativas</code>: Consultar hóspedes que ainda estão no hotel.</li>
            <li><code>GET /hospedagem/inativas</code>: Consultar hóspedes que já deixaram o hotel.</li>
            <li><code>GET /hospedagem/{cpf}</code>: Buscar hospedagens por CPF do hóspede.</li>
           <li><code>GET  /hospedagem/</code>: Buscar todas as hospedagens.</li>
            <li><code>POST /hospedagem</code>: Realizar check-in (e opcionalmente check-out).</li>
            <li><code>PUT /hospedagem/{id}</code>: Atualizar uma hospedagem.</li>
            <li><code>DELETE /hospedagem/{id}</code>: Deletar uma hospedagem.</li>
        </ul>
</body>
</html>
