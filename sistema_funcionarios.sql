CREATE SCHEMA `sistema_funcionarios` ;

USE sistema_funcionarios;

CREATE TABLE `cargos` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(50) NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `funcionarios` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(50) NULL,
  `sobrenome` VARCHAR(50) NULL,
  `data_nascimento` DATETIME NULL,
  `email` VARCHAR(100) NULL,
  `cargo` INT NULL,
  `salario` DOUBLE NULL,
  PRIMARY KEY (`id`));


INSERT INTO `cargos` (`nome`) VALUES ('Gerente de Projetos');
INSERT INTO `cargos` (`nome`) VALUES ('Designer Gráfico');
INSERT INTO `cargos` (`nome`) VALUES ('Atendente Sênior');

INSERT INTO `funcionarios` (`nome`, `sobrenome`, `data_nascimento`, `email`, `cargo`, `salario`) VALUES ('Geraldo', 'Nascimento', '1997-10-08', 'teste@gmail.com', '2', '4500.15');
INSERT INTO `funcionarios` (`nome`, `sobrenome`, `data_nascimento`, `email`, `cargo`, `salario`) VALUES ('Rosy', 'Lima', '1970-08-08', 'rosi@gmail.com', '3', '2503.34');
INSERT INTO `funcionarios` (`nome`, `sobrenome`, `data_nascimento`, `email`, `cargo`, `salario`) VALUES ('José', 'Marconi', '1997-10-07', 'jose@gmail.com', '1', '15000');
INSERT INTO `funcionarios` (`nome`, `sobrenome`, `data_nascimento`, `email`, `cargo`, `salario`) VALUES ('Vital', 'Mota', '1968-10-19', 'vital@gmail.com', '3', '2700.52');

