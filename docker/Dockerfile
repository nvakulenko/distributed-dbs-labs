FROM postgres:13

# Custom initialization scripts
COPY 1-schema.sql /docker-entrypoint-initdb.d/1-schema.sql

RUN chmod +x /docker-entrypoint-initdb.d/1-schema.sql