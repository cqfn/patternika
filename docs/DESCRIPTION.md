
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
