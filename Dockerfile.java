FROM openjdk:21-oracle
RUN microdnf install findutils

COPY . /home
WORKDIR /home/java
#RUN curl -L -o dd-java-agent.jar 'https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=com.datadoghq&a=dd-java-agent&v=1.21.0'
#RUN curl -L -o dd-java-agent.jar 'https://repo1.maven.org/maven2/com/datadoghq/dd-java-agent/1.21.0/dd-java-agent-1.21.0.jar'
RUN curl -L -o dd-java-agent.jar 'https://github.com/DataDog/dd-trace-java/releases/download/v1.22.0/dd-java-agent.jar'
RUN ./gradlew --no-daemon installDist
ENV DD_PROFILING_TAGS="env:$DD_ENV,service:$DD_SERVICE,version:$DD_VERSION" 
CMD JAVA_OPTS="-Ddd.agent.host=$DD_AGENT_HOST -Ddd.profiling.enabled=true -Ddd.profiling.allocation.enabled=true -Ddd.profiling.ddprof.enabled=true -Ddd.profiling.ddprof.cpu.enabled=true -Ddd.profiling.ddprof.wall.enabled=true -Ddd.profiling.ddprof.cstack=dwarf -Ddd.profiling.ddprof.liveheap.enabled=true -Ddd.profiling.ddprof.alloc.enabled=true -Ddd.profiling.directallocation.enabled=true -javaagent:dd-java-agent.jar" ./build/install/movies-api-java/bin/movies-api-java