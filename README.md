# Jonas Andersen Admin

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jonasandersen-no_jonasandersen-admin&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jonasandersen-no_jonasandersen-admin) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=jonasandersen-no_jonasandersen-admin&metric=bugs)](https://sonarcloud.io/summary/new_code?id=jonasandersen-no_jonasandersen-admin) [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=jonasandersen-no_jonasandersen-admin&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=jonasandersen-no_jonasandersen-admin) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=jonasandersen-no_jonasandersen-admin&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=jonasandersen-no_jonasandersen-admin)

## Htmx tutorial

https://maciejwalkowiak.com/blog/spring-boot-thymeleaf-tailwindcss

## Local testing

```bash
mvn clean package

java -jar -Dspring.profiles.active=dev admin-app/target/admin-app-
```

Docker image configuration. Use to toggle between native image and jvm image.

```xml

<plugin>
  <groupId>org.graalvm.buildtools</groupId>
  <artifactId>native-maven-plugin</artifactId>
</plugin>
  ...
<configuration>
<image>
  <builder>paketobuildpacks/builder-jammy-buildpackless-tiny</builder>
  <name>bjoggis/${project.artifactId}:v${project.version}-${os.detected.arch}</name>
  <buildpacks>
    <!--              <buildpack>paketobuildpacks/java-native-image</buildpack>-->
    <buildpack>paketobuildpacks/java</buildpack>
  </buildpacks>
  <env>
    <!--              <BPL_SPRING_AOT_ENABLED>true</BPL_SPRING_AOT_ENABLED>-->
    <!--              <BP_JVM_CDS_ENABLED>true</BP_JVM_CDS_ENABLED>-->
    <BP_JVM_VERSION>25</BP_JVM_VERSION>
    <!--              <BP_NATIVE_IMAGE_BUILD_ARGUMENTS>-H:-AddAllFileSystemProviders</BP_NATIVE_IMAGE_BUILD_ARGUMENTS>-->
    <!--              <BP_NATIVE_IMAGE>true</BP_NATIVE_IMAGE>-->
  </env>
</image>
</configuration>
```

# First time setup

Create docker volumes for grafana and prometheus to avoid having to set up the configuration every time the containers
are recreated.

```bash
docker volume create grafana-storage
docker volume create prometheus-storage
docker volume create alertmanager-storage
```