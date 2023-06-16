FROM gradle:jdk11 as build

RUN mkdir "twenty-four-kay"

WORKDIR "/twenty-four-kay"

ADD gradlew .
ADD build.gradle.kts .
ADD settings.gradle.kts .
ADD gradle.properties .
RUN gradle clean build --no-daemon >/dev/null 2>&1 || true

COPY src ./src

# Tailwind CSS standalone binary to compile the CSS
COPY tailwind.config.js ./tailwind.config.js
RUN curl -sLO https://github.com/tailwindlabs/tailwindcss/releases/download/v3.3.2/tailwindcss-linux-x64
RUN chmod +x tailwindcss-linux-x64
RUN mv tailwindcss-linux-x64 tailwindcss
RUN ./tailwindcss -i ./src/main/css/input.css -o ./src/main/resources/public/output.css --minify

RUN gradle clean build --no-daemon
RUN unzip build/distributions/*.zip -d /twenty-four-kay/app

FROM openjdk:11-jre-slim
WORKDIR /twenty-four-kay/app
COPY --from=build /twenty-four-kay/app /twenty-four-kay/app
ENTRYPOINT ["./twenty-four-kay-1.0-SNAPSHOT/bin/twenty-four-kay"]