FROM openjdk:16
WORKDIR /app
COPY src ./src/
COPY lib ./lib/
RUN javac -cp ".:lib/*:src" -d out "src/compileBoot.java"