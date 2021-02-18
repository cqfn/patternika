[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
[![GitHub](https://github.com/patternika/patternika/workflows/Java%20CI%20with%20Gradle/badge.svg)](
  https://github.com/patternika/patternika/actions?query=workflow%3A%22Java+CI+with+Gradle%22)
[![codecov](https://codecov.io/gh/patternika/patternika/branch/main/graph/badge.svg?token=0V10JBZRRO)](https://codecov.io/gh/patternika/patternika)

There are a lot of tools dedicated to library migrations, like 
[ORMIT](https://renaps.com/en/products/ormit-java), 
[Migration Tool for Sun Java System Application Server](https://docs.oracle.com/cd/E19830-01/819-4725/6n6rv9st3/index.html), 
[SLF4J Migrator](http://www.slf4j.org/migrator.html), etc. All these powerful 
tools are based on static migration rules and would work only in scenarios like 
(`specific library` to `specific library`).

**Patternika** provides a wide variety of possibilities to work with API migrations.
The main goal is to provide a _seamless_ and _automated_ migration process of **any** library 
in **any** project with **any** programming language.
That's not all! Patternika is also a powerful instrument for source-code tree analysis, code patching, 
pattern designing and visualisation. 

## Usage

* [Main concepts](docs/DESCRIPTION.md)
* [Command-line interface](docs/CLI.md)

## Building

The project is built using the Gradle build automation tool.
Instructions on how to use it are [here](docs/GRADLE.md).

## CI/CD

[GitHub Workflow](
https://github.com/patternika/patternika/actions?query=workflow%3A%22Java+CI+with+Gradle%22) 
and Circle CI are configured for this project.

## Contributing

Anyone and everyone is welcome to contribute. Please take a moment to
review the [guidelines for contributing](docs/CONTRIBUTING.md).

* [Code of conduct](docs/CODE_OF_CONDUCT.md)
* [Bug reports](docs/CONTRIBUTING.md#bug-reports)
* [Feature requests](docs/CONTRIBUTING.md#feature-suggestions)
* [Pull requests](docs/CONTRIBUTING.md#pull-requests)
* [Code quality policy](docs/CODE_QUALITY.md)

IMPORTANT: By submitting any contribution, you agree to allow the project owner to
license your work under the same [license](LICENSE) as that used for the project.
