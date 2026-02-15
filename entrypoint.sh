#!/bin/sh
echo "Starting Spring Boot on port 8080 with memory optimization..."
# Tambahkan -Xss256k untuk menghemat stack memory per thread
exec java -Xmx160m -Xms160m -Xss256k -XX:+UseSerialGC \
     -Dserver.address=0.0.0.0 \
     -Dserver.port=8080 \
     -Dspring.main.lazy-initialization=true \
     -jar /app/app.jar