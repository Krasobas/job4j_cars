insert into brand (name, country_id)
values
    -- German Brands
    ('Volkswagen', (SELECT id FROM country WHERE name = 'Germany')),
    ('BMW', (SELECT id FROM country WHERE name = 'Germany')),
    ('Mercedes-Benz', (SELECT id FROM country WHERE name = 'Germany')),
    ('Audi', (SELECT id FROM country WHERE name = 'Germany')),
    ('Porsche', (SELECT id FROM country WHERE name = 'Germany')),
    ('Opel', (SELECT id FROM country WHERE name = 'Germany')),

    -- Japanese Brands
    ('Toyota', (SELECT id FROM country WHERE name = 'Japan')),
    ('Honda', (SELECT id FROM country WHERE name = 'Japan')),
    ('Nissan', (SELECT id FROM country WHERE name = 'Japan')),
    ('Mazda', (SELECT id FROM country WHERE name = 'Japan')),
    ('Subaru', (SELECT id FROM country WHERE name = 'Japan')),
    ('Mitsubishi', (SELECT id FROM country WHERE name = 'Japan')),
    ('Lexus', (SELECT id FROM country WHERE name = 'Japan')),
    ('Infiniti', (SELECT id FROM country WHERE name = 'Japan')),
    ('Suzuki', (SELECT id FROM country WHERE name = 'Japan')),

    -- American Brands
    ('Ford', (SELECT id FROM country WHERE name = 'United States')),
    ('Chevrolet', (SELECT id FROM country WHERE name = 'United States')),
    ('Tesla', (SELECT id FROM country WHERE name = 'United States')),
    ('Jeep', (SELECT id FROM country WHERE name = 'United States')),
    ('Dodge', (SELECT id FROM country WHERE name = 'United States')),
    ('Cadillac', (SELECT id FROM country WHERE name = 'United States')),
    ('GMC', (SELECT id FROM country WHERE name = 'United States')),

    -- Korean Brands
    ('Hyundai', (SELECT id FROM country WHERE name = 'South Korea')),
    ('Kia', (SELECT id FROM country WHERE name = 'South Korea')),
    ('Genesis', (SELECT id FROM country WHERE name = 'South Korea')),

    -- French Brands
    ('Peugeot', (SELECT id FROM country WHERE name = 'France')),
    ('Renault', (SELECT id FROM country WHERE name = 'France')),
    ('Citroën', (SELECT id FROM country WHERE name = 'France')),
    ('Bugatti', (SELECT id FROM country WHERE name = 'France')),

    -- Italian Brands
    ('Ferrari', (SELECT id FROM country WHERE name = 'Italy')),
    ('Fiat', (SELECT id FROM country WHERE name = 'Italy')),
    ('Lamborghini', (SELECT id FROM country WHERE name = 'Italy')),
    ('Alfa Romeo', (SELECT id FROM country WHERE name = 'Italy')),
    ('Maserati', (SELECT id FROM country WHERE name = 'Italy')),
    ('Pagani', (SELECT id FROM country WHERE name = 'Italy')),

    -- British Brands
    ('Jaguar', (SELECT id FROM country WHERE name = 'United Kingdom')),
    ('Land Rover', (SELECT id FROM country WHERE name = 'United Kingdom')),
    ('Bentley', (SELECT id FROM country WHERE name = 'United Kingdom')),
    ('Rolls-Royce', (SELECT id FROM country WHERE name = 'United Kingdom')),
    ('Mini', (SELECT id FROM country WHERE name = 'United Kingdom')),
    ('Aston Martin', (SELECT id FROM country WHERE name = 'United Kingdom')),
    ('McLaren', (SELECT id FROM country WHERE name = 'United Kingdom')),

    -- Swedish Brands
    ('Volvo', (SELECT id FROM country WHERE name = 'Sweden')),
    ('Koenigsegg', (SELECT id FROM country WHERE name = 'Sweden')),

    -- Other European
    ('Škoda', (SELECT id FROM country WHERE name = 'Czech Republic')),
    ('Lada', (SELECT id FROM country WHERE name = 'Russia')),
    ('SEAT', (SELECT id FROM country WHERE name = 'Spain')),

    -- Asian Brands
    ('Geely', (SELECT id FROM country WHERE name = 'China')),
    ('BYD', (SELECT id FROM country WHERE name = 'China')),
    ('Great Wall', (SELECT id FROM country WHERE name = 'China')),
    ('Tata', (SELECT id FROM country WHERE name = 'India')),
    ('Mahindra', (SELECT id FROM country WHERE name = 'India'))
;