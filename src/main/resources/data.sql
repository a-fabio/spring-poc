-- Inserir clientes de exemplo
INSERT INTO clientes (nome, email, data_criacao) VALUES 
('João Silva', 'joao.silva@email.com', CURRENT_TIMESTAMP),
('Maria Santos', 'maria.santos@email.com', CURRENT_TIMESTAMP),
('Pedro Oliveira', 'pedro.oliveira@email.com', CURRENT_TIMESTAMP);

-- Inserir pedidos de exemplo
INSERT INTO pedidos (descricao, valor, data_pedido, status, cliente_id) VALUES 
('Pedido de teste 1', 150.50, CURRENT_TIMESTAMP, 'PENDENTE', 1),
('Pedido de teste 2', 299.99, CURRENT_TIMESTAMP, 'PROCESSANDO', 1),
('Pedido de teste 3', 89.90, CURRENT_TIMESTAMP, 'CONCLUIDO', 2),
('Pedido de teste 4', 45.00, CURRENT_TIMESTAMP, 'PENDENTE', 3),
('Pedido de teste 5', 199.99, CURRENT_TIMESTAMP, 'CANCELADO', 2);