#!/bin/sh
echo "Starting Spring Boot on port 8080..."
exec java -Xmx160m -Xms160m -XX:+UseSerialGC -XX:MaxRAM=256m \
     -Dserver.address=0.0.0.0 \
     -Dserver.port=8080 \
     -jar /app/app.jar