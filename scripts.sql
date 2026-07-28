INSERT INTO estados_usuario (nombre) 
VALUES ('ACTIVO'),('BLOQUEADO'),('INACTIVO'),('SUSPENDIDO');

 INSERT INTO estados_ejemplar (nombre) 
 VALUES ('BAJA'),('DAÑADO'),('DISPONIBLE'),('PERDIDO'),('PRESTADO'),('RESERVADO');

INSERT INTO estados_multa (nombre) 
VALUES ('CONDONADA'),('PAGADA'),('PENDIENTE');

INSERT INTO estados_prestamo (nombre) 
VALUES ('ACTIVO'),('DEVUELTO'), ('RENOVADO'), ('VENCIDO');

INSERT INTO estados_reserva (nombre) 
VALUES ('DISPONIBLE'),('ENTREGADA'),('PENDIENTE'), ('CANCELADA'), ('EXPIRADO');

INSERT INTO roles (nombre) 
VALUES ('ADMINISTRADOR'), ('BIBLIOTECARIO'), ('USUARIO');

INSERT INTO tipos_telefono (nombre)
VALUES ('EMPRESA'), ('PERSONAL'), ('CASA'), ('REFERENCIA');

INSERT INTO municipios (nombre)
VALUES ('Villahermosa'),('Tenosique'),('Balancan'),('Paraiso'),('Teapa'),('Jonuta');

/* USUARIO ADMIN */
INSERT INTO usuarios 
(nombre, apellido_paterno, apellido_materno, fecha_nacimiento, genero, fk_estado, fk_rol)
VALUES ('José Alfredo','Lopez','De La Cruz', '2006-01-04','Hombre',1,1);

INSERT INTO credenciales
(contrasena, correo, fk_usuario)
VALUES ('Alfredo12345','admin@me.com', 1);