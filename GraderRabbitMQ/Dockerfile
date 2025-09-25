FROM rabbitmq:3.13-management

# Enable management plugin
RUN rabbitmq-plugins enable rabbitmq_management

# Create directories for data persistence
RUN mkdir -p /var/lib/rabbitmq && \
    chown -R rabbitmq:rabbitmq /var/lib/rabbitmq

EXPOSE 5672 15672

USER rabbitmq