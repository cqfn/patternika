[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
[![GitHub](https://github.com/patternika/patternika/workflows/Java%20CI%20with%20Gradle/badge.svg)](
  https://github.com/patternika/patternika/actions?query=workflow%3A%22Java+CI+with+Gradle%22)
---

# Patternika
There are a lot of tools dedicated to library migrations, like 
[ORMIT](https://renaps.com/en/products/ormit-java), 
[flyway](https://flywaydb.org/),
[Migration Tool for Sun Java System Application Server](https://docs.oracle.com/cd/E19830-01/819-4725/6n6rv9st3/index.html), 
[SLF4J Migrator](http://www.slf4j.org/migrator.html), etc. All these powerful 
tools are based on static migration rules and would work only in scenarios like 
(`specific library` to `specific library`).

**Patternika** provides a wide variety of possibilities to work with API migrations.
The main goal is to provide a _seamless_ and _automated_ migration process of **any** library 
in **any** project with **any** programming language.
That's not all! Patternika is also a powerful instrument for source-code tree analysis, code patching, 
pattern designing and visualisation. 

## How it works
Fundamental working principle of our instrument is based on ASTs and patterns - code changes rules.

The AST (Abstract Syntax Tree) is a tree representation of source code: 
every Java (or other language) code can be entirely represented as an AST. 
For instance, if we take Java as source code:
every type of instruction is a special element of the Java Programming Language.
 E.g. there are nodes for method declarations (MethodDeclaration), 
 variable declaration (VariableDeclarationFragment), assignments and so on.

### 
Let's say that you have a simple Java-class:
```java
class Test {
    long concat(Integer a, int b, Long c) {
        return a + b + c;
    }
}
```

You want to change the logic of `concat()` method as follows:

```java
class Test {
    String concat(Integer a, int b, Long c) {
        return a.toString() + b + c.toString();
    }
}
```

Patternika can automate such process by using a special pattern. This pattern
will contain the AST _node_ which should be changed and _actions_ that change it.
In our case the _node_ will be a `return statement` and _actions_ would imply addition 
of method call `toString()` to variable `a` and `c`.

This example looks simple. Let's take a step further:

Original code:
```java
public class Car extends Rectangle {
	public static final Logger logger = LogManager.getLogger(Car.class);
}
```
Updated code:
```java
public class Car extends Rectangle {
    public static final Logger logger = Logger.getLogger(Car.class.getName());
}
```

Patternika allows you not only to automate the migration process, but create and use 
your own migration scenarios. For example, let's say that you have migrated one 
project from Jackson library to Gson library. But your first project is a Rest application, 
and the other is an android app. You can simply use patterns (changes) from Rest app to make 
migration in the android project. Patternika will create all patterns by itself.

Patternika can not only make such migration automatically, 
you can write your own rules and Patternika will patch whole projects 
for you, using your rule-set!

## Building

The project is built using the Gradle build automation tool.
Instructions on how to use it are [here](docs/GRADLE.md).

## CI/CD

[GitHub Workflow](
https://github.com/patternika/patternika/actions?query=workflow%3A%22Java+CI+with+Gradle%22
)
and Circle CI are configured for this project.

## Contributing

Anyone and everyone is welcome to contribute. Please take a moment to
review the [guidelines for contributing](docs/CONTRIBUTING.md).

* [Code of conduct](docs/CODE_OF_CONDUCT.md)
* [Bug reports](docs/CONTRIBUTING.md#bug-reports)
* [Feature requests](docs/CONTRIBUTING.md#feature-suggestions)
* [Pull requests](docs/CONTRIBUTING.md#pull-requests)
* [Code quality policy](docs/CODE_QUALITY.md)

**IMPORTANT**: By submitting any contribution, you agree to allow the project owner to
license your work under the same [license](LICENSE) as that used for the project.
