INSERT INTO estado_solicitud (nombre_estado_solicitud) VALUES 
('Pendiente'),
('Por pagar'),
('Rechazada'),
('Por calificar'),
('Cerrada')
ON DUPLICATE KEY UPDATE nombre_estado_solicitud = VALUES(nombre_estado_solicitud);