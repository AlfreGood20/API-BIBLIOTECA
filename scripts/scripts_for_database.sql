INSERT INTO estados_usuario (nombre) 
VALUES ('ACTIVO'),('BLOQUEADO'),('INACTIVO'),('SUSPENDIDO');

INSERT INTO estados_ejemplar (nombre) 
VALUES ('BAJA'),('DAÑADO'),('DISPONIBLE'),('PERDIDO'),('PRESTADO'),('RESERVADO');

INSERT INTO estados_multa (nombre) 
VALUES ('CONDONADA'),('PAGADA'),('PENDIENTE');

INSERT INTO estados_prestamo (nombre) 
VALUES ('ACTIVO'),('DEVUELTO'), ('RENOVADO'), ('VENCIDO');

INSERT INTO estados_reserva (nombre) 
VALUES ('DISPONIBLE'),('ENTREGADA'),('PENDIENTE'), ('CANCELADA'), ('EXPIRADA');

INSERT INTO roles (nombre) 
VALUES ('ADMINISTRADOR'), ('BIBLIOTECARIO'), ('USUARIO');

INSERT INTO tipos_telefono (nombre)
VALUES ('EMPRESA'), ('PERSONAL'), ('CASA'), ('REFERENCIA');