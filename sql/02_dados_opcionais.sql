-- OPCIONAL: dados fictícios para apresentação (não executar se não quiser amostras)
USE loja_swing;
INSERT INTO Categorias(nome) VALUES ('Informática'), ('Papelaria'), ('Acessórios')
  ON DUPLICATE KEY UPDATE nome = VALUES(nome);
INSERT INTO Produtos(Nome, Preco, Quantidade, categoria_id)
SELECT 'Mouse USB', 49.90, 15, id FROM Categorias WHERE nome='Informática';
INSERT INTO Produtos(Nome, Preco, Quantidade, categoria_id)
SELECT 'Caderno', 21.50, 30, id FROM Categorias WHERE nome='Papelaria';
