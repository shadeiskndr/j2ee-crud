# Stage 1: Build the WAR file using Maven
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
# Copy the entire src directory
COPY src ./src
# Run Maven package goal with verbose output to see what's happening
RUN mvn package -X
# List the contents of the target directory to debug
RUN ls -la target/

# Stage 2: Create the final image with Tomcat and the WAR
FROM tomcat:11.0-jdk21
# Copy the WAR file from the build stage - updated to match the new finalName
COPY --from=build /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war
