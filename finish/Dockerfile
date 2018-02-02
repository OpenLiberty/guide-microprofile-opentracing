# Start with OpenLiberty as the parent image.
FROM openliberty/open-liberty:javaee7

# Symlink for easier mounts.
RUN ln -s /opt/ol/wlp/usr/servers /servers

# Download and install the opentracing zipkin user feature.
RUN wget https://repo1.maven.org/maven2/net/wasdev/wlp/tracer/liberty-opentracing-zipkintracer/1.0/liberty-opentracing-zipkintracer-1.0-sample.zip -O /tmp/tracer.zip && \
    unzip /tmp/tracer.zip -d /opt/ol/wlp/usr && \
    rm /tmp/tracer.zip

# Start the defaultServer (by default).
ENTRYPOINT ["/opt/ol/wlp/bin/server", "run"]
CMD ["defaultServer"]
