FROM gradle:9.2.1-jdk25-noble AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle :server:shadowJar

FROM gcr.io/distroless/java25-debian13:nonroot

WORKDIR /app
COPY --from=build /home/gradle/src/server/build/libs/server-all.jar /app/sesterzo.jar
EXPOSE 8080

CMD ["/app/sesterzo.jar"]