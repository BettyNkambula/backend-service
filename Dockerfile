FROM openjdk:8-jdk-alpine as build
WORKDIR /workspace/app

ARG DEPENDENCY=target/dependency

ADD target target
RUN mkdir -p ${DEPENDENCY} && (cd ${DEPENDENCY}; jar -xf ../*.jar)

FROM openjdk:8-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring
RUN mkdir -p /logs \
 && chown -R spring:spring /logs \
 && chmod -R a+rw /logs

EXPOSE 8080

#VOLUME /logs
#VOLUME /tmp

ARG DEPENDENCY=/workspace/app/target/dependency
ARG JAVA_OPTS=""

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

USER spring
#ENTRYPOINT ["sh", "-c", "java -noverify -XX:TieredStopAtLevel=1 -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap ${JAVA_OPTIONS} org.springframework.boot.loader.JarLauncher"]
ENTRYPOINT ["sh", "-c", "java \
-cp app:app/lib/* \
-noverify \
-XX:TieredStopAtLevel=1 \
-XX:+UnlockExperimentalVMOptions \
-XX:+UseCGroupMemoryLimitForHeap \
-Dspring.backgroundpreinitializer.ignore=true \
-Dspring.jmx.enabled=false \
${JAVA_OPTS} \
za/co/vending/machine/backendservice/BackendServiceApplication.java \
${0} \
${@}"]
