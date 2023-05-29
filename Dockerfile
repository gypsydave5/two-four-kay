FROM gradle:jdk11

RUN mkdir "two-four-kay"

WORKDIR "/two-four-kay"

ADD gradlew .

ADD build.gradle.kts .

ADD settings.gradle.kts .

ADD gradle.properties .

RUN pwd

RUN gradle --no-daemon build

COPY src ./src

CMD ["gradle", "--no-daemon", "run"]