-- ============================================
-- Archivo: basededatos.sql
-- Descripción: Esquema de base de datos para User, Service y Order
-- Codificación completa UTF-8 (ñ, tildes, emojis, etc.)
-- Compatible con MySQL / MariaDB
-- ============================================

-- Crear base de datos con soporte completo para caracteres españoles
CREATE DATABASE IF NOT EXISTS eventik
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_spanish_ci;

USE eventik;

-- ============================================
-- Tabla: User
-- ============================================
CREATE TABLE users (
  id CHAR(36) PRIMARY KEY,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  phone VARCHAR(50) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  address VARCHAR(255),
  role ENUM('admin','worker','customer') NOT NULL DEFAULT 'customer',
  image LONGTEXT
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_spanish_ci;

-- ============================================
-- Tabla: Service
-- ============================================
CREATE TABLE services (
  id CHAR(36) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  duration INT NOT NULL,
  maxParticipants INT NOT NULL,
  isActive BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_spanish_ci;

-- ============================================
-- Tabla: Order
-- ============================================
CREATE TABLE orders (
  id CHAR(36) PRIMARY KEY,
  customerId CHAR(36) NOT NULL,
  serviceId CHAR(36) NOT NULL,
  appointment DATETIME NOT NULL,
  status ENUM('pending','confirmed','completed','canceled') NOT NULL DEFAULT 'pending',
  notes TEXT,
  priceFinal DECIMAL(10,2),
  participants INT,
  location VARCHAR(255),
  
  CONSTRAINT fk_order_customer
    FOREIGN KEY (customerId) REFERENCES users(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

  CONSTRAINT fk_order_service
    FOREIGN KEY (serviceId) REFERENCES services(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_spanish_ci;

-- ============================================
-- Índices recomendados
-- ============================================
CREATE INDEX idx_order_customer ON orders (customerId);
CREATE INDEX idx_order_service ON orders (serviceId);
CREATE INDEX idx_order_status ON orders (status);

-- ============================================
-- Datos de ejemplo con ñ y tildes
-- ============================================
-- ============================================
-- Datos de ejemplo: 15 usuarios
-- ============================================
INSERT INTO users (id, username, password, phone, email, address, role, image)
VALUES
  (UUID(), 'jose', 'josepass', '555-1001', 'jose.pena@example.com', 'Calle Niño Perdido Nº 1', 'customer', ''),
  (UUID(), 'maria', 'mariapass', '555-1002', 'maria.nunez@example.com', 'Avenida Constitución 45', 'worker', ''),
  (UUID(), 'ana', 'anapass', '555-1003', 'ana.gomez@example.com', 'Calle Mayor 12', 'customer', ''),
  (UUID(), 'luis', 'luispass', '555-1004', 'luis.rodriguez@example.com', 'Plaza España 5', 'customer', ''),
  (UUID(), 'carlos', 'carlospass', '555-1005', 'carlos.martin@example.com', 'Calle Luna 7', 'customer', ''),
  (UUID(), 'elena', 'elenapass', '555-1006', 'elena.perez@example.com', 'Calle Sol 3', 'worker', ''),
  (UUID(), 'jorge', 'jorgepass', '555-1007', 'jorge.sanchez@example.com', 'Avenida del Mar 8', 'customer', ''),
  (UUID(), 'laura', 'laurapass', '555-1008', 'laura.morales@example.com', 'Calle Olmo 9', 'customer', ''),
  (UUID(), 'miguel', 'miguelpass', '555-1009', 'miguel.vazquez@example.com', 'Plaza del Rey 2', 'customer', ''),
  (UUID(), 'sofia', 'sofiapass', '555-1010', 'sofia.ramirez@example.com', 'Avenida Real 4', 'customer', ''),
  (UUID(), 'david', 'davidpass', '555-1011', 'david.lopez@example.com', 'Calle Alta 6', 'worker', ''),
  (UUID(), 'paula', 'paulapass', '555-1012', 'paula.mendez@example.com', 'Plaza Nueva 11', 'customer', ''),
  (UUID(), 'fernando', 'fernandopass', '555-1013', 'fernando.garcia@example.com', 'Calle Baja 10', 'customer', ''),
  (UUID(), 'isabel', 'isabelpass', '555-1014', 'isabel.soto@example.com', 'Avenida Central 1', 'customer', ''),
  (UUID(), 'ricardo', 'ricardopass', '555-1015', 'ricardo.castillo@example.com', 'Calle Norte 12', 'customer', '');

-- ============================================
-- Datos de ejemplo: 5 servicios
-- ============================================
INSERT INTO services (id, name, description, price, duration, maxParticipants, isActive)
VALUES
  (UUID(), 'Catering corporativo', 'Servicio de catering completo para eventos empresariales, incluye buffet y bebidas.', 1500.00, 240, 60, TRUE),
  (UUID(), 'Organizacion de bodas', 'Planificacion y coordinacion integral de bodas, desde la ceremonia hasta la recepcion.', 5000.00, 480, 80, TRUE),
  (UUID(), 'Servicio de banquete', 'Preparacion y montaje de banquetes para celebraciones grandes.', 2500.00, 300, 250, TRUE),
  (UUID(), 'Produccion de eventos', 'Gestion tecnica y logistica para eventos: sonido, iluminacion y escenario.', 3200.00, 360, 80, TRUE),
  (UUID(), 'Coordinacion de conferencias', 'Organizacion de conferencias y meetings, incluye agenda, ponentes y soporte tecnico.', 1800.00, 180, 70, TRUE);


-- ============================================
-- Datos de ejemplo: 10 órdenes
-- ============================================
-- Nota: reemplaza los UUID() por los IDs reales de usuarios y servicios si quieres mantener relaciones fijas
INSERT INTO orders (id, customerId, serviceId, appointment, status, notes, priceFinal, participants, location)
VALUES
  (UUID(), (SELECT id FROM users WHERE username='jose'), 
      (SELECT id FROM services WHERE name='Catering corporativo'),
      '2025-11-19 10:00:00', 'pending', 'Primera vez', 1500.00, 30, 'Calle Niño Perdido Nº 1'),

  (UUID(), (SELECT id FROM users WHERE username='ana'),
      (SELECT id FROM services WHERE name='Organizacion de bodas'),
      '2025-11-19 11:00:00', 'confirmed', '', 5000.00, 2, 'Calle Mayor 12'),

  (UUID(), (SELECT id FROM users WHERE username='luis'),
      (SELECT id FROM services WHERE name='Servicio de banquete'),
      '2025-11-19 12:00:00', 'completed', 'Llegó tarde', 2500.00, 80, 'Plaza España 5'),

  (UUID(), (SELECT id FROM users WHERE username='carlos'),
      (SELECT id FROM services WHERE name='Produccion de eventos'),
      '2025-11-20 09:30:00', 'canceled', 'Canceló', 3200.00, 50, 'Calle Luna 7'),

  (UUID(), (SELECT id FROM users WHERE username='jorge'),
      (SELECT id FROM services WHERE name='Coordinacion de conferencias'),
      '2025-11-20 10:00:00', 'pending', '', 1800.00, 40, 'Avenida del Mar 8'),

  (UUID(), (SELECT id FROM users WHERE username='laura'),
      (SELECT id FROM services WHERE name='Catering corporativo'),
      '2025-11-20 11:30:00', 'confirmed', 'Pide menú vegetariano', 1500.00, 20, 'Calle Olmo 9'),

  (UUID(), (SELECT id FROM users WHERE username='miguel'),
      (SELECT id FROM services WHERE name='Organizacion de bodas'),
      '2025-11-21 14:00:00', 'pending', '', 5000.00, 2, 'Plaza del Rey 2'),

  (UUID(), (SELECT id FROM users WHERE username='sofia'),
      (SELECT id FROM services WHERE name='Servicio de banquete'),
      '2025-11-21 15:00:00', 'completed', '', 2500.00, 80, 'Avenida Real 4'),

  (UUID(), (SELECT id FROM users WHERE username='paula'),
      (SELECT id FROM services WHERE name='Produccion de eventos'),
      '2025-11-22 10:30:00', 'confirmed', '', 3200.00, 60, 'Plaza Nueva 11'),

  (UUID(), (SELECT id FROM users WHERE username='fernando'),
      (SELECT id FROM services WHERE name='Coordinacion de conferencias'),
      '2025-11-22 11:00:00', 'pending', '', 1800.00, 30, 'Calle Baja 10');
