## Code Quality Policy

Any committed code must meet quality standards enforced
by the following automated checks.

**Tests** &mdash; all tests must pass without failures.

**JaCoCo** &mdash; method complexity greater than 10 is unacceptable.

**Javadoc** &mdash; documentation must have a valid format and must be compiled
to HTML without errors.

**Checkstyle** &mdash; code style must comply with
[Sun's Java Style](https://checkstyle.sourceforge.io/sun_style.html) conventions.
Changes made to the conventions in this project:
* line length limit is set to 100;
* constructor parameters are allowed to have the same names as fields;
* magic numbers 0, 1, -1, 2, and 31 (for hashes) are allowed.

**PMD** &mdash; code must satisfy static analysis rules, which enforce compliance
with [best coding practices](https://pmd.github.io/pmd-6.30.0/pmd_rules_java.html) for Java.
The rule set includes the following categories:

* [Best Practices](https://pmd.github.io/pmd-6.30.0/pmd_rules_java.html#best-practices)
  except for:
     * `JUnitAssertionsShouldIncludeMessage`
     * `JUnitTestContainsTooManyAsserts`

* [Code Style](https://pmd.github.io/pmd-6.30.0/pmd_rules_java.html#code-style)
  except for:
     * `AtLeastOneConstructor`
     * `CommentDefaultAccessModifier`
     * `OnlyOneReturn`
     * `EmptyMethodInAbstractClassShouldBeAbstract`

* [Design](https://pmd.github.io/pmd-6.30.0/pmd_rules_java.html#design)
  except for:
     * `LawOfDemeter`

* [Documentation](https://pmd.github.io/pmd-6.30.0/pmd_rules_java.html#documentation)

* [Error Prone](https://pmd.github.io/pmd-6.30.0/pmd_rules_java.html#error-prone)
  except for:
     * `BeanMembersShouldSerialize`
     * `CompareObjectsWithEquals`

* [Performance](https://pmd.github.io/pmd-6.30.0/pmd_rules_java.html#performance)
  except for:
     * `AvoidInstantiatingObjectsInLoops`

Also, some rules from these categories have been modified.
See the configuration [file](../config/pmd/ruleset.xml) for details.
