#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER poc_user WITH PASSWORD 'qwerty';
    ALTER USER poc_user WITH SUPERUSER;
    CREATE DATABASE db_invoices;
    GRANT ALL PRIVILEGES ON DATABASE db_invoices TO poc_user;
EOSQL
