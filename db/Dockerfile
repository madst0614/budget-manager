FROM mariadb:latest

COPY init.sql /docker-entrypoint-initdb.d

ENV MARIADB_ROOT_PASSWORD=1234
ENV MARIADB_DATABASE=budget
ENV MARIADB_HOST=%

CMD ["--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci"]