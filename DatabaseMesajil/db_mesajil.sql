-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema db_mesajil
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `db_mesajil` ;

-- -----------------------------------------------------
-- Schema db_mesajil
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `db_mesajil` DEFAULT CHARACTER SET utf8mb3 ;
USE `db_mesajil` ;

-- -----------------------------------------------------
-- Table `db_mesajil`.`rol`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_mesajil`.`rol` ;

CREATE TABLE IF NOT EXISTS `db_mesajil`.`rol` (
  `idRol` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  `descripcion` VARCHAR(150) NULL DEFAULT NULL,
  PRIMARY KEY (`idRol`),
  UNIQUE INDEX `nombre_UNIQUE` (`nombre` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `db_mesajil`.`usuario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_mesajil`.`usuario` ;

CREATE TABLE IF NOT EXISTS `db_mesajil`.`usuario` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `idRol` INT NOT NULL,
  `nombres` VARCHAR(100) NOT NULL,
  `apellidos` VARCHAR(100) NOT NULL,
  `correo` VARCHAR(100) NOT NULL,
  `contrasena` VARCHAR(255) NOT NULL,
  `telefono` VARCHAR(20) NULL DEFAULT NULL,
  `direccion` VARCHAR(200) NULL DEFAULT NULL,
  `fechaRegistro` DATETIME NOT NULL,
  `estado` TINYINT NOT NULL DEFAULT '1',
  PRIMARY KEY (`idUsuario`),
  UNIQUE INDEX `correo_UNIQUE` (`correo` ASC) VISIBLE,
  INDEX `fk_usuario_rol_idx` (`idRol` ASC) VISIBLE,
  CONSTRAINT `fk_usuario_rol`
    FOREIGN KEY (`idRol`)
    REFERENCES `db_mesajil`.`rol` (`idRol`))
ENGINE = InnoDB
AUTO_INCREMENT = 13
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `db_mesajil`.`carrito`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_mesajil`.`carrito` ;

CREATE TABLE IF NOT EXISTS `db_mesajil`.`carrito` (
  `idCarrito` INT NOT NULL AUTO_INCREMENT,
  `idUsuario` INT NOT NULL,
  `fechaCreacion` DATETIME NOT NULL,
  `estado` TINYINT NOT NULL DEFAULT '1',
  PRIMARY KEY (`idCarrito`),
  INDEX `fk_producto_rol_idx` (`idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_carrito_usuario`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `db_mesajil`.`usuario` (`idUsuario`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `db_mesajil`.`categoria`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_mesajil`.`categoria` ;

CREATE TABLE IF NOT EXISTS `db_mesajil`.`categoria` (
  `idCategoria` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(80) NOT NULL,
  `descripcion` VARCHAR(200) NULL DEFAULT NULL,
  `estado` TINYINT NOT NULL DEFAULT '1',
  `fechaRegistro` DATETIME NOT NULL,
  PRIMARY KEY (`idCategoria`),
  UNIQUE INDEX `nombre_UNIQUE` (`nombre` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `db_mesajil`.`producto`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_mesajil`.`producto` ;

CREATE TABLE IF NOT EXISTS `db_mesajil`.`producto` (
  `idProducto` INT NOT NULL AUTO_INCREMENT,
  `idCategoria` INT NOT NULL,
  `nombre` VARCHAR(120) NOT NULL,
  `descripcion` TEXT NULL DEFAULT NULL,
  `marca` VARCHAR(80) NOT NULL,
  `modelo` VARCHAR(80) NOT NULL,
  `precio` DECIMAL(10,2) NOT NULL,
  `estado` TINYINT NOT NULL DEFAULT '1',
  `fechaRegistro` DATETIME NOT NULL,
  PRIMARY KEY (`idProducto`),
  INDEX `fk_categoria_rol_idx` (`idCategoria` ASC) VISIBLE,
  CONSTRAINT `fk_producto_categoria`
    FOREIGN KEY (`idCategoria`)
    REFERENCES `db_mesajil`.`categoria` (`idCategoria`))
ENGINE = InnoDB
AUTO_INCREMENT = 38
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `db_mesajil`.`detallecarrito`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_mesajil`.`detallecarrito` ;

CREATE TABLE IF NOT EXISTS `db_mesajil`.`detallecarrito` (
  `idDetalleCarrito` INT NOT NULL AUTO_INCREMENT,
  `idCarrito` INT NOT NULL,
  `idProducto` INT NOT NULL,
  `cantidad` INT NOT NULL,
  `precioUnitario` DECIMAL(10,2) NOT NULL,
  `subtotal` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`idDetalleCarrito`),
  INDEX `fk_carrito_rol_idx` (`idCarrito` ASC) VISIBLE,
  INDEX `fk_detallecarrito_producto_idx` (`idProducto` ASC) VISIBLE,
  CONSTRAINT `fk_detallecarrito_carrito`
    FOREIGN KEY (`idCarrito`)
    REFERENCES `db_mesajil`.`carrito` (`idCarrito`),
  CONSTRAINT `fk_detallecarrito_producto`
    FOREIGN KEY (`idProducto`)
    REFERENCES `db_mesajil`.`producto` (`idProducto`))
ENGINE = InnoDB
AUTO_INCREMENT = 47
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `db_mesajil`.`pedido`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_mesajil`.`pedido` ;

CREATE TABLE IF NOT EXISTS `db_mesajil`.`pedido` (
  `idPedido` INT NOT NULL AUTO_INCREMENT,
  `idUsuario` INT NOT NULL,
  `fechaPedido` DATETIME NOT NULL,
  `total` DECIMAL(10,2) NOT NULL,
  `estadoPedido` VARCHAR(30) NOT NULL,
  `tipoEntrega` VARCHAR(20) NOT NULL DEFAULT 'Recojo',
  `direccionEntrega` VARCHAR(200) NULL DEFAULT NULL,
  `tiendaRecojo` VARCHAR(100) NULL DEFAULT NULL,
  `costoEnvio` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  `estadoPago` VARCHAR(30) NOT NULL DEFAULT 'Pendiente',
  `IdOrdenMercadoPago` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`idPedido`),
  INDEX `fk_usuario_rol_idx` (`idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_pedido_usuario`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `db_mesajil`.`usuario` (`idUsuario`))
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `db_mesajil`.`detallepedido`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_mesajil`.`detallepedido` ;

CREATE TABLE IF NOT EXISTS `db_mesajil`.`detallepedido` (
  `idDetallePedido` INT NOT NULL AUTO_INCREMENT,
  `idPedido` INT NOT NULL,
  `idProducto` INT NOT NULL,
  `cantidad` INT NOT NULL,
  `precioUnitario` DECIMAL(10,2) NOT NULL,
  `subtotal` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`idDetallePedido`),
  INDEX `fk_pedido_rol_idx` (`idPedido` ASC) VISIBLE,
  INDEX `fk_producto_rol_idx` (`idProducto` ASC) VISIBLE,
  CONSTRAINT `fk_detallepedido_pedido`
    FOREIGN KEY (`idPedido`)
    REFERENCES `db_mesajil`.`pedido` (`idPedido`),
  CONSTRAINT `fk_detallepedido_producto`
    FOREIGN KEY (`idProducto`)
    REFERENCES `db_mesajil`.`producto` (`idProducto`))
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `db_mesajil`.`imagenesproducto`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_mesajil`.`imagenesproducto` ;

CREATE TABLE IF NOT EXISTS `db_mesajil`.`imagenesproducto` (
  `idImagen` INT NOT NULL AUTO_INCREMENT,
  `idProducto` INT NOT NULL,
  `urlImagen` VARCHAR(255) NOT NULL,
  `principal` TINYINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`idImagen`),
  INDEX `fk_producto_rol_idx` (`idProducto` ASC) VISIBLE,
  CONSTRAINT `fk_imagen_producto`
    FOREIGN KEY (`idProducto`)
    REFERENCES `db_mesajil`.`producto` (`idProducto`))
ENGINE = InnoDB
AUTO_INCREMENT = 29
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `db_mesajil`.`inventario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_mesajil`.`inventario` ;

CREATE TABLE IF NOT EXISTS `db_mesajil`.`inventario` (
  `idInventario` INT NOT NULL AUTO_INCREMENT,
  `idProducto` INT NOT NULL,
  `stockActual` INT NOT NULL DEFAULT '0',
  `stockMinimo` INT NOT NULL DEFAULT '0',
  `ultimaActualizacion` DATETIME NOT NULL,
  PRIMARY KEY (`idInventario`),
  UNIQUE INDEX `idProducto_UNIQUE` (`idProducto` ASC) VISIBLE,
  INDEX `fk_producto_rol_idx` (`idProducto` ASC) VISIBLE,
  CONSTRAINT `fk_inventario_producto`
    FOREIGN KEY (`idProducto`)
    REFERENCES `db_mesajil`.`producto` (`idProducto`))
ENGINE = InnoDB
AUTO_INCREMENT = 20
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
