import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
	id("application")
}

group = "hummel"
version = "v" + LocalDate.now().format(DateTimeFormatter.ofPattern("yy.MM.dd"))

val embed: Configuration by configurations.creating

dependencies {
	embed("net.java.dev.jna:jna:5.13.0")
	embed("net.java.dev.jna:jna-platform:5.13.0")
	implementation("net.java.dev.jna:jna:5.13.0")
	implementation("net.java.dev.jna:jna-platform:5.13.0")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

application {
	mainClass = "hummel.MainKt"
}

tasks {
	named<JavaExec>("run") {
		standardInput = System.`in`
	}
	jar {
		manifest {
			attributes(
				mapOf(
					"Main-Class" to "hummel.Main"
				)
			)
		}
		from(embed.map {
			if (it.isDirectory) it else zipTree(it)
		})
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	}
}
