let database = db.getSiblingDB('shapp_db');

database.createCollection('Store');
database.createCollection('City');
database.createCollection('Category');
database.createCollection('Address');
database.createCollection('Product');
