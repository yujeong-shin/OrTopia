FROM openjdk:11 as stage1

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# git action에서 실행시 권한 관련 에러가 생기는 것으로 추측
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM openjdk:11

WORKDIR /app

COPY --from=stage1 /app/build/libs/*.jar /app/app.jar

VOLUME /tmp

ENTRYPOINT ["java","-jar","app.jar"]