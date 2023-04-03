FROM amazoncorretto:17 as build
WORKDIR /app

COPY ../build.gradle ../settings.gradle ../gradlew ../lombok.config ./
COPY ../gradle ./gradle
RUN ./gradlew --no-daemon downloadDependencies
COPY src src

RUN ./gradlew --no-daemon build

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/build/libs/learn-words-using-phrases-0.0.1-SNAPSHOT.jar ./app.jar

ENTRYPOINT java -jar -Dspring.profiles.active=$APPLICATION_PROFILE \
                      app.jar