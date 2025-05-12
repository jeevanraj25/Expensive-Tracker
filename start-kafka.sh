#!/bin/bash

# Format the storage directory if needed
if [ ! -f "/tmp/kraft-combined-logs/.kafka-formatted" ]; then
    echo "Formatting Kafka storage directory..."
    kafka-storage.sh format -t MkU3OEVBNTcwNTJENDM2M -c /opt/kafka/config/kraft/server.properties
    touch /tmp/kraft-combined-logs/.kafka-formatted
fi

# Start Kafka server
exec kafka-server-start.sh /opt/kafka/config/kraft/server.properties