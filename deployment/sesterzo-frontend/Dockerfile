FROM gradle:9.2.1-jdk25-noble AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# Provide the build-time properties normally read from local.properties.
RUN printf 'api.url=https://sesterzo.eu/api\napi.cache.ttl=15\nspace.limit=5\n' > local.properties
RUN gradle kotlinUpgradeYarnLock
RUN gradle :composeApp:jsBrowserDistribution

FROM nginx:alpine
COPY deployment/sesterzo-frontend/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /home/gradle/src/composeApp/build/dist/js/productionExecutable /usr/share/nginx/html
# Precompress the static assets so nginx can serve them via gzip_static at no CPU cost.
RUN find /usr/share/nginx/html -type f \
        \( -name '*.js' -o -name '*.mjs' -o -name '*.wasm' -o -name '*.css' \
           -o -name '*.html' -o -name '*.ttf' -o -name '*.json' -o -name '*.cvr' \) \
        -exec gzip -9 -k {} +
EXPOSE 80