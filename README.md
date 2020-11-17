[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
[![GitHub](https://github.com/patternika/patternika/workflows/Java%20CI%20with%20Gradle/badge.svg)](
  https://github.com/patternika/patternika/actions?query=workflow%3A%22Java+CI+with+Gradle%22)
---

# Patternika

__TODO__

## Using Gradle

### IntelliJ IDEA

To build the project in IntelliJ IDEA and to run static checks and tests,
you need to import the Gradle script into IntellIJ IDEA:

1. Open the "build.gradle" Gradle script file ("File | Open...").
1. In the dialog that will appear, select "Open as Project".
1. The project will open in IntelliJ IDEA.
1. Double-click the "build.gradle" file in the Project panel.
1. Open the Gradle panel ("View | Tool Windows | Gradle").
1. The Gradle panel will appear at the right.
1. Click "Reimport Gradle Project" in the Gradle panel.
1. Gradle will download all dependencies.
1. Now you can run build tasks in the Gradle panel.
1. For example, to build the project, select "Tasks | build | build".

### Command line

You can run Gradle tasks from the command line using the following command:

    ./gradlew build

`gradlew` is a Gradle wrapper that knows how to find and, if necessary, download Gradle.

## CI/CD

GitHub Workflow and Circle CI are configured for this project.
