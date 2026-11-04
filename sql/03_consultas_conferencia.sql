USE loja_swing;
SHOW TABLES;
DESCRIBE Categorias;
DESCRIBE Produtos;
SELECT p.id, p.Nome, p.Preco, p.Quantidade, c.nome AS categoria
  FROM Produtos p INNER JOIN Categorias c ON c.id=p.categoria_id
  ORDER BY p.id;
SELECT * FROM Categorias ORDER BY nome;
