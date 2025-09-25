FROM postgres:15

# Create data directory and set permissions
RUN mkdir -p /var/lib/postgresql/data && \
    chown -R postgres:postgres /var/lib/postgresql/data

# Set the default database directory
ENV PGDATA=/var/lib/postgresql/data

# Copy initialization scripts
COPY init.sql /docker-entrypoint-initdb.d/

EXPOSE 5432

USER postgres