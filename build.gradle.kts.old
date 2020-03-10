import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
import java.net.InetAddress
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_DATE

plugins {
	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
	kotlin("plugin.jpa") version "1.3.61"
	id("org.springframework.boot") version "2.2.4.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	id("org.owasp.dependencycheck") version "5.3.0"
	id("com.github.ben-manes.versions") version "0.28.0"
	id("com.gorylenko.gradle-git-properties") version "2.2.2"
}

group = "uk.gov.justice.digital.hmpps"

val today: Instant = Instant.now()
val todaysDate: String = LocalDate.now().format(ISO_DATE)

version = if (System.getenv().containsKey("CI")) {
	"${todaysDate}.${System.getenv("CIRCLE_BUILD_NUM")}"
} else {
	todaysDate
}

java.sourceCompatibility = JavaVersion.VERSION_13

repositories {
	mavenCentral()
}

dependencies {
	testRuntimeOnly("com.h2database:h2:1.4.200")
	runtimeOnly("org.postgresql:postgresql:42.2.10")
	runtimeOnly("org.flywaydb:flyway-core:6.2.4")

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.springfox:springfox-swagger2:2.9.2")
	implementation("io.springfox:springfox-swagger-ui:2.9.2")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<BootJar> {
	manifest {
		attributes["Implementation-Title"] = rootProject.name
		attributes["Implementation-Version"] = archiveVersion
	}
}

springBoot {
	buildInfo {
		properties {
			artifact = rootProject.name
			version = version
			group = group
			name = rootProject.name
			time = today

			additional = mapOf(
					"by" to System.getProperty("user.name"),
					"operatingSystem" to "${System.getProperty("os.name")} (${System.getProperty("os.version")})",
					"continuousIntegration" to System.getenv().containsKey("CI"),
					"machine" to InetAddress.getLocalHost().hostName
			)
		}
	}
}