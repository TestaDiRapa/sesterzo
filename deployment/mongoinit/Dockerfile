FROM mongo:8.2

WORKDIR /auxiliary
COPY ./init_mongo.sh /auxiliary/init_mongo.sh

CMD ["/bin/bash", "/auxiliary/init_mongo.sh"]