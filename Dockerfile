FROM gradle:8.10-jdk21

WORKDIR /app

COPY /app .

RUN gradle installDist

CMD ./app/build/install/app/bin/app