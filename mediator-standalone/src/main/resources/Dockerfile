FROM java:8

RUN mkdir -p /opt/mediator
COPY mediator-core.jar /opt/mediator/core.jar

EXPOSE "8080/tcp"

ENTRYPOINT ["/usr/bin/java", "-cp", "/opt/mediator/core.jar", "ru.linachan.mediator.MediatorCore"]