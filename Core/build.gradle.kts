plugins {
    id("java-library")
    `maven-publish`
}

group = "com.readutf.annotationcommands"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    testCompileOnly("org.projectlombok:lombok:1.18.32")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.32")

    implementation("org.apache.logging.log4j:log4j-api:2.7")
    implementation("org.apache.logging.log4j:log4j-core:2.7")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.7")

    implementation("org.jetbrains:annotations:24.0.0")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.readutf.annotationcommands"
            artifactId = "Core"
            version = "1.1"

            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}