plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
    id("org.jetbrains.intellij") version "1.16.0"
}

group = "clueqva"
version = "1.3.1"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.2.1")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("java"))
}

dependencies {
    // version 0.10.2 results in an exception
    implementation("org.reflections:reflections:0.9.12")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testCompileOnly ("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor ("org.projectlombok:lombok:1.18.30")

}
tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("221")
        untilBuild.set("")
        changeNotes.set("""
      v1.3.1<br>
      Dependency versions updated.<br>      
      v1.3<br>
      Type checking improved.<br>
      v1.2<br>
      Completions are also shown after method calls, string literals and class references.<br>  
      v1.1<br>
      Cactoos is now used as a plugin logo and as a completions icon.<br>""")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
