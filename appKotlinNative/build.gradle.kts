import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
	id("org.jetbrains.kotlin.multiplatform")
}

group = "hummel"
version = LocalDate.now().format(DateTimeFormatter.ofPattern("yy.MM.dd"))

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

kotlin {
	mingwX64 {
		binaries {
			executable {
				entryPoint("hummel.main")
				linkerOpts("-lwinmm")
				baseName = "${project.name}-${project.version}"
			}
		}
	}
	sourceSets {
		configureEach {
			languageSettings {
				optIn("kotlinx.cinterop.ExperimentalForeignApi")
			}
		}
	}
}
