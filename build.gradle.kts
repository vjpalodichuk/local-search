plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "com.capital7software.ai"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    
    // Mockito
    testImplementation("org.mockito:mockito-core:5.2.0")
    
    // JUnitParams
    testImplementation("pl.pragmatists:JUnitParams:1.1.1")
    
    // Cucumber
    testImplementation("info.cukes:cucumber-junit:1.2.6")
}

javafx {
    version = "21.0.1"
    modules.add("javafx.controls")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xlint:unchecked")
}

tasks.withType<Jar>().configureEach {
    manifest {
        attributes["Implementation-Title"] = "AI Local Search Example"
        attributes["Implementation-Version"] = "1.0.0.0"
        attributes["Main-Class"] = "com.capital7software.ai.localsearch.FindScheduleApplication"
    }
}

//create a single Jar with all dependencies
tasks.register("fatJar", Jar::class) {
    archiveBaseName = "${project.name}-fat"
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    with(tasks.jar.get() as CopySpec)
}

tasks.getByName<JavaExec>("run") {
    mainClass = "com.capital7software.ai.localsearch.FindScheduleApplication"
}