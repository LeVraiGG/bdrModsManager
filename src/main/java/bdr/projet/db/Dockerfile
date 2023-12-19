FROM bitnami/postgresql:latest

COPY ./all.sql ./all.sql

RUN psql "host=<localhost> port=5432 dbname=<$POSTGRESQL_DATABASE> user=<$POSTGRESQL_USERNAME> password=<$POSTGRESQL_PASSWORD>" -f ./all.sql
