FROM gradle:jdk11 as build

RUN mkdir "twenty-four-kay"

WORKDIR "/twenty-four-kay"

ADD gradlew .
ADD build.gradle.kts .
ADD settings.gradle.kts .
ADD gradle.properties .
RUN gradle clean build --no-daemon /dev/null 2>&1 || true

COPY src ./src
RUN gradle clean build --no-daemon
RUN unzip build/distributions/*.zip -d /twenty-four-kay/app

FROM openjdk:11-jre-slim
WORKDIR /twenty-four-kay/app
COPY --from=build /twenty-four-kay/app /twenty-four-kay/app
ENTRYPOINT ["./twenty-four-kay-1.0-SNAPSHOT/bin/twenty-four-kay"]