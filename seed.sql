-- Script de Datos Semilla para MiniCore Logística
-- Servirá para poblar la base de datos de forma masiva para pruebas de rangos cronológicos.

TRUNCATE TABLE envios RESTART IDENTITY CASCADE;
TRUNCATE TABLE repartidor RESTART IDENTITY CASCADE;
TRUNCATE TABLE zonas RESTART IDENTITY CASCADE;

-- 1. Registro de Zonas Geográficas y sus tarifas base por Kg
INSERT INTO zonas (id_zona, nombre_zona, tarifa_por_kg) VALUES 
(1, 'Norte', 1.50),
(2, 'Sur', 2.00),
(3, 'Centro', 1.25);

-- 2. Registro de Personal Repartidor
INSERT INTO repartidor (id_repartidor, nombre, email) VALUES 
(1, 'Andrés', 'andres.mullo@logistica.com'),
(2, 'Camila', 'camila.reparto@logistica.com'),
(3, 'Luis', 'luis.entrega@logistica.com');

-- 3. Histórico de Envíos Masivos (2025 - 2026)

-- === CASO DE PRUEBA OFICIAL: MAYO 2025 ===
-- Andrés: 5 envíos en Zona Norte que suman exactamente 32 kg (32 * 1.50 = $48.00)
INSERT INTO envios (id_repartidor, id_zona, peso_kg, fecha_envio) VALUES 
(1, 1, 5.00, '2025-05-02'),
(1, 1, 7.00, '2025-05-10'),
(1, 1, 10.00, '2025-05-15'),
(1, 1, 4.00, '2025-05-20'),
(1, 1, 6.00, '2025-05-28');

-- Camila: 3 envíos en Zona Sur que suman exactamente 18 kg (18 * 2.00 = $36.00)
INSERT INTO envios (id_repartidor, id_zona, peso_kg, fecha_envio) VALUES 
(2, 2, 5.00, '2025-05-05'),
(2, 2, 8.00, '2025-05-12'),
(2, 2, 5.00, '2025-05-22');

-- === DATOS ADICIONALES PARA MOSTRAR POTENCIA DE FILTRADO ===
-- Envíos de Abril 2025 (Fuera de rango)
INSERT INTO envios (id_repartidor, id_zona, peso_kg, fecha_envio) VALUES 
(1, 2, 12.50, '2025-04-15'), -- Andrés en el Sur
(2, 3, 14.00, '2025-04-18'), -- Camila en el Centro
(3, 1, 25.00, '2025-04-29'); -- Luis en el Norte

-- Envíos de Junio/Julio 2025
INSERT INTO envios (id_repartidor, id_zona, peso_kg, fecha_envio) VALUES 
(3, 3, 15.00, '2025-06-05'), -- El envío de Luis que lo excluye de Mayo
(1, 3, 8.00, '2025-06-12'),
(2, 1, 11.20, '2025-07-04');

-- Histórico Reciente (Enero a Junio 2026)
INSERT INTO envios (id_repartidor, id_zona, peso_kg, fecha_envio) VALUES 
(1, 1, 18.50, '2026-01-10'),
(2, 2, 22.00, '2026-02-14'),
(3, 3, 30.00, '2026-03-20'),
(1, 2, 14.10, '2026-04-05'),
(2, 1, 9.50, '2026-05-19'),
(3, 2, 16.00, '2026-06-01');
