FROM redis:7

# Create data directory
RUN mkdir -p /data && chown redis:redis /data

RUN echo "appendonly yes" >> /usr/local/etc/redis/redis.conf && \
    echo "appendfsync everysec" >> /usr/local/etc/redis/redis.conf

EXPOSE 6379

USER redis

CMD ["redis-server", "/usr/local/etc/redis/redis.conf"]