FROM openliberty/open-liberty:javaee7

RUN ln -s /opt/ol/wlp/usr/servers /servers

RUN wget https://repo1.maven.org/maven2/net/wasdev/wlp/tracer/liberty-opentracing-zipkintracer/1.0/liberty-opentracing-zipkintracer-1.0-sample.zip -O /tmp/tracer.zip && \
    unzip /tmp/tracer.zip -d /opt/ol/wlp/usr && \
    rm /tmp/tracer.zip

ENTRYPOINT ["/opt/ol/wlp/bin/server", "run"]
CMD ["defaultServer"]


