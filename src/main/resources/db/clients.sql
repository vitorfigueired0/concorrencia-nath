INSERT INTO clients (name, role, address, city, postal_code, phone)
 SELECT * FROM (
 	SELECT 'User1', 'Assistant', '4973 Maple Ave', 'New York', '38276', '135-101-8137' UNION ALL
  SELECT 'User2', 'Manager', '5260 Main St', 'Philadelphia', '54742', '950-836-9985' UNION ALL
  SELECT 'User3', 'Clerk', '3627 Cedar Ln', 'Chicago', '39795', '950-836-9985' UNION ALL
  SELECT 'User4', 'Assistant', '3963 Oakwood Dr', 'San Antonio', '88688', '413-436-1417' UNION ALL
  SELECT 'User5', 'Intern', '3266 Broadway', 'Philadelphia', '88688', '651-242-7643' UNION ALL
  SELECT 'User6', 'Developer', '8522 Sunset Rd', 'San Diego', '98064', '135-101-8137' UNION ALL
  SELECT 'User7', 'Lead', '3761 Elm St', 'San Diego', '88688', '268-434-4120' UNION ALL
  SELECT 'User8', 'Clerk', '9103 Highland Ave', 'Los Angeles', '56183', '135-101-8137' UNION ALL
  SELECT 'User9', 'Analyst', '193 Broadway', 'Phoenix', '59946', '950-836-9985' UNION ALL
  SELECT 'User10', 'Clerk', '9387 Elm St', 'Phoenix', '54742', '265-253-9509' UNION ALL
  SELECT 'User11', 'Manager', '123 Elm St', 'Dallas', '90210', '123-456-7890' UNION ALL
  SELECT 'User12', 'Analyst', '456 Pine St', 'Miami', '33101', '987-654-3210' UNION ALL
  SELECT 'User13', 'Intern', '789 Oak St', 'Seattle', '98101', '555-123-4567' UNION ALL
  SELECT 'User14', 'Assistant', '101 Maple St', 'Boston', '02101', '222-333-4444' UNION ALL
  SELECT 'User15', 'Clerk', '202 Cedar St', 'Denver', '80201', '999-888-7777' UNION ALL
  SELECT 'User16', 'Developer', '303 Birch St', 'Portland', '97201', '444-555-6666' UNION ALL
  SELECT 'User17', 'Lead', '404 Ash St', 'Atlanta', '30301', '777-666-5555' UNION ALL
  SELECT 'User18', 'Manager', '505 Cherry St', 'Houston', '77001', '888-777-6666' UNION ALL
  SELECT 'User19', 'Intern', '606 Spruce St', 'Orlando', '32801', '333-222-1111' UNION ALL
  SELECT 'User20', 'Assistant', '707 Walnut St', 'Las Vegas', '89101', '111-222-3333' UNION ALL
  SELECT 'User21', 'Clerk', '808 Chestnut St', 'Austin', '78701', '888-999-0000' UNION ALL
  SELECT 'User22', 'Developer', '909 Sycamore St', 'Columbus', '43085', '555-444-3333' UNION ALL
  SELECT 'User23', 'Lead', '111 Poplar St', 'Indianapolis', '46201', '666-777-8888' UNION ALL
  SELECT 'User24', 'Manager', '222 Locust St', 'Charlotte', '28201', '999-000-1111' UNION ALL
  SELECT 'User25', 'Intern', '333 Willow St', 'San Francisco', '94101', '222-111-0000' UNION ALL
  SELECT 'User26', 'Assistant', '444 Magnolia St', 'San Jose', '95101', '000-111-2222' UNION ALL
  SELECT 'User27', 'Clerk', '555 Palm St', 'Salt Lake City', '84101', '123-321-4567' UNION ALL
  SELECT 'User28', 'Developer', '666 Hickory St', 'Tampa', '33601', '654-987-3210' UNION ALL
  SELECT 'User29', 'Lead', '777 Cypress St', 'Nashville', '37201', '789-654-1234' UNION ALL
  SELECT 'User30', 'Manager', '888 Fir St', 'Minneapolis', '55401', '456-789-0123' UNION ALL
  SELECT 'User31', 'Intern', '999 Beech St', 'St. Louis', '63101', '321-654-9870' UNION ALL
  SELECT 'User32', 'Assistant', '121 Maple Ave', 'Cleveland', '44101', '555-666-7777' UNION ALL
  SELECT 'User33', 'Clerk', '232 Oak Ave', 'Kansas City', '64101', '888-999-1111' UNION ALL
  SELECT 'User34', 'Developer', '343 Pine Ave', 'Pittsburgh', '15201', '000-123-4567' UNION ALL
  SELECT 'User35', 'Lead', '454 Cedar Ave', 'Cincinnati', '45201', '987-012-3456' UNION ALL
  SELECT 'User36', 'Manager', '565 Birch Ave', 'Raleigh', '27601', '789-123-4560' UNION ALL
  SELECT 'User37', 'Intern', '676 Elm Ave', 'Richmond', '23201', '456-789-1234' UNION ALL
  SELECT 'User38', 'Assistant', '787 Ash Ave', 'Louisville', '40201', '321-654-0987' UNION ALL
  SELECT 'User39', 'Clerk', '898 Spruce Ave', 'Oklahoma City', '73101', '654-321-1234' UNION ALL
  SELECT 'User40', 'Developer', '909 Walnut Ave', 'Milwaukee', '53201', '432-543-6543' UNION ALL
  SELECT 'User41', 'Lead', '101 Cherry Ave', 'New Orleans', '70112', '321-987-6540' UNION ALL
  SELECT 'User42', 'Manager', '202 Poplar Ave', 'Memphis', '38101', '210-123-4567' UNION ALL
  SELECT 'User43', 'Intern', '303 Locust Ave', 'Tucson', '85701', '111-555-2222' UNION ALL
  SELECT 'User44', 'Assistant', '404 Willow Ave', 'El Paso', '79901', '333-444-5555' UNION ALL
  SELECT 'User45', 'Clerk', '505 Magnolia Ave', 'Albuquerque', '87101', '777-888-9999' UNION ALL
  SELECT 'User46', 'Developer', '606 Palm Ave', 'Birmingham', '35201', '123-555-6789' UNION ALL
  SELECT 'User47', 'Lead', '707 Hickory Ave', 'Jacksonville', '32201', '987-666-7777' UNION ALL
  SELECT 'User48', 'Manager', '808 Cypress Ave', 'Fresno', '93701', '000-444-3333' UNION ALL
  SELECT 'User49', 'Intern', '909 Fir Ave', 'Sacramento', '95801', '888-111-5555' UNION ALL
  SELECT 'User50', 'Assistant', '111 Beech Ave', 'Long Beach', '90801', '444-222-3333') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM clients);
