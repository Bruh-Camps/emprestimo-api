-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

INSERT INTO ProdutoEmprestimo(nome, taxaJurosAnual, prazoMaximoMeses) VALUES ('Empréstimo Pessoal CAIXA', 18.0, 24);
INSERT INTO ProdutoEmprestimo(nome, taxaJurosAnual, prazoMaximoMeses) VALUES ('Crédito Consignado', 12.5, 48);