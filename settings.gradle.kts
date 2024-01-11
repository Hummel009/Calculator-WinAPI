pluginManagement {
	repositories {
		google()
		mavenCentral()
		gradlePluginPortal()
	}
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
	repositories {
		google()
		mavenCentral()
		gradlePluginPortal()
	}
}

include(":appKotlinJvm")
include(":appKotlinNative")
include(":appJava")