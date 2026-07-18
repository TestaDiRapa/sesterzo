FROM gradle:9.2.1-jdk25-noble AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# Needed to build even if not used by the backend
RUN printf 'api.url=http://127.0.0.1:8080/api\napi.cache.ttl=15\nspace.limit=5\n' > local.properties
RUN gradle :server:shadowJar

FROM gcr.io/distroless/java25-debian13:nonroot

WORKDIR /app
COPY --from=build /home/gradle/src/server/build/libs/server-all.jar /app/sesterzo.jar
EXPOSE 8080

CMD ["/app/sesterzo.jar"]