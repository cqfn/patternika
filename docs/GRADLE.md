## Using Gradle

The project is built using [Gradle](https://gradle.org/).
The [Gradle build script](../build.gradle) is also responsible for static analysis,
testing, and measuring code coverage.

### IntelliJ IDEA

To build the project in IntelliJ IDEA and to run static checks and tests,
you need to import the Gradle script into IntelliJ IDEA:

1. Open the "build.gradle" Gradle script file `File > Open`.
1. In the dialog that will appear, select `Open as Project`.
1. The project will open in IntelliJ IDEA.
1. Double-click the `build.gradle` file in the Project panel.
1. Open the Gradle panel `View > Tool Windows > Gradle`.
1. The Gradle panel will appear at the right.
1. Click `Reimport Gradle Project` in the Gradle panel.
1. Gradle will download all dependencies.
1. Now you can run build tasks in the Gradle panel.
1. For example, to build the project, select `Tasks > build > build`.

### Command line

You can run Gradle tasks from the command line using the following command:

    ./gradlew build

`gradlew` is a Gradle wrapper that knows how to find and, if necessary, download Gradle.
