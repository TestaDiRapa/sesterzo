FROM gradle:9.2.1-jdk25-noble AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# Provide the build-time properties normally read from local.properties.
RUN printf 'api.url=http://127.0.0.1:8080/api\napi.cache.ttl=15\nspace.limit=5\n' > local.properties
RUN gradle :composeApp:jsBrowserDistribution

FROM nginx:alpine
COPY deployment/sesterzo-frontend/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /home/gradle/src/composeApp/build/dist/js/productionExecutable /usr/share/nginx/html
EXPOSE 80