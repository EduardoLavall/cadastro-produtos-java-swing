-- Laboratório de Desenvolvimento de Software - CRUD Produtos/Categorias
-- Execute no MySQL Workbench, mysql CLI ou phpMyAdmin (compatível MySQL 8+).
CREATE DATABASE IF NOT EXISTS loja_swing CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE loja_swing;

CREATE TABLE IF NOT EXISTS Categorias (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(80) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_categorias_nome (nome)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Produtos (
    id INT NOT NULL AUTO_INCREMENT,
    Nome VARCHAR(100) NOT NULL,
    Preco DECIMAL(10,2) NOT NULL,
    Quantidade INT NOT NULL,
    categoria_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_produtos_categorias FOREIGN KEY (categoria_id)
      REFERENCES Categorias(id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT chk_produtos_preco CHECK (Preco >= 0),
    CONSTRAINT chk_produtos_quantidade CHECK (Quantidade >= 0),
    KEY ix_produtos_nome (Nome),
    KEY ix_produtos_categoria (categoria_id)
) ENGINE=InnoDB;
