plugins {
    id("java")
    application
}

group = "il.ac.kinneret.mjmay"
version = "5784"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("il.ac.kinneret.mjmay.sendingacks.Main")
}
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "il.ac.kinneret.mjmay.sendingacks.Main"
    }
}
