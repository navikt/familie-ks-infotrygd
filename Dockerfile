FROM gcr.io/distroless/java21-debian12:nonroot

COPY --chown=nonroot:nonroot ./target/familie-ks-infotrygd-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

ENV APP_NAME=familie-ks-infotrygd
ENV TZ="Europe/Oslo"
# TLS Config works around an issue in OpenJDK... See: https://github.com/kubernetes-client/java/issues/854
ENTRYPOINT [ "java", "-Djdk.tls.client.protocols=TLSv1.2", "-jar", "/app/app.jar" ]

