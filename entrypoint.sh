#!/bin/sh
echo "Starting Spring Boot..."
# Kita kurangi Xms agar startup tidak langsung memakan banyak RAM
exec java -Xmx170m -Xms64m -Xss256k -XX:+UseSerialGC \
     -Dspring.main.lazy-initialization=true \
     -Dserver.address=0.0.0.0 \
     -Dserver.port=8080 \
     -jar /app/app.jar