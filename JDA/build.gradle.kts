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

    //add jda
    compileOnly("net.dv8tion:JDA:5.0.0-beta.23") {
        exclude(module="opus-java")
    }

    api(project(":Core"))

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    testCompileOnly("org.projectlombok:lombok:1.18.32")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.32")

}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.readutf.annotationcommands"
            artifactId = "JDA"
            version = "1.1"

            from(components["java"])
        }
    }
}



tasks.test {
    useJUnitPlatform()
}