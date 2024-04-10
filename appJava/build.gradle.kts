import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
	id("application")
}

group = "com.github.hummel"
version = LocalDate.now().format(DateTimeFormatter.ofPattern("yy.MM.dd"))

val embed: Configuration by configurations.creating

dependencies {
	embed("net.java.dev.jna:jna:5.14.0")
	embed("net.java.dev.jna:jna-platform:5.14.0")
	implementation("net.java.dev.jna:jna:5.14.0")
	implementation("net.java.dev.jna:jna-platform:5.14.0")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

application {
	mainClass = "com.github.hummel.wincalc.Main"
}

tasks {
	named<JavaExec>("run") {
		standardInput = System.`in`
	}
	jar {
		manifest {
			attributes(
				mapOf(
					"Main-Class" to "com.github.hummel.wincalc.Main"
				)
			)
		}
		from(embed.map {
			if (it.isDirectory) it else zipTree(it)
		})
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	}
	withType<JavaCompile>().configureEach {
		options.encoding = "UTF-8"
	}
}
