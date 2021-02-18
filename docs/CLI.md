Patternika Command-Line Interface
---

## Usage

The `patternika.jar` file is run using the command-line interface
to perform the following tasks:

- mine patterns
- patch source code using the patterns
- run web interface
- see debug information

Command looks like:
~~~
java -jar patternika.jar <action> <input file(s)/dir> [option(s)]
~~~ 

### Mine actions

##### **mine**

Mine patterns from the folder. The command looks like:
~~~
java -jar patternika.jar mine /path/to/folder 
~~~
You have to create an educational dataset to mine the pattern.
Educational datasets may be in 2 formats.
* 1 format. Folder contains numbered folders.
  Each numbered folder contains 2 java files ending `*_bug.*` and `*_fix.*`
* 2 format. Folder with any structure and each file contains `.before` and `.after` suffix,
  example: `my/project/src/filename.before.java`  
  No options required to specify format.

Options: `--database`

Outputs:
* 1 format: `data.json`
* 2 format: `new_data.json`

Example:
`java -jar patternika.jar mine testing_data --dump-ast`

##### **mine-link**

Mine all patterns from github project by the link or from git directory. The command looks like:
~~~
java -jar patternika.jar mine-link <link> 
~~~

Input: Link. Link could be any type, either `https` or `ssh://`

**Options:** `--comm`. Mining changes from specified commit or commits (separated by ",").
Otherwise, mining will be done from all commits by pairs.

**POM options:**
You can use following options to define which commits are suitable for pattern mining:

`--pom` - mine only patterns from commits, which contains updates in `pom.xml` file.

**Important**: You must have `com.springsource.org.custommonkey.xmlunit-1.2.0` exported in `Project Settings->Modules->Dependencies`.

`--fromVer` ``String`` - mine only patterns from commits, which contains VERSION field update from ``String`` in `pom.xml` file

`--toVer` ``String`` - mine only patterns from commits, which contains VERSION field update to ``String`` in `pom.xml` file

`--fromArtId` ``String`` - mine only patterns from commits, which contains ARTIFACTID field update from ``String`` in `pom.xml` file

`--toArtId` ``String`` - mine only patterns from commits, which contains ARTIFACTID field update to ``String`` in `pom.xml` file

_Examples:_
1) `mine https://github.com/padriano/catpeds --comm df561f066f389cdc6e4c7f126ca7f505d45b470f --database <insert path> --dump-json`
2) `mine https://github.com/padriano/catpeds --database <insert path> --dump-json`
3) `mine https://github.com/padriano/catpeds --comm 373754e5924f09766921a931ec88f77dc8471bb3 --pom --fromVer 2.20.1 --toVer 2.22.2`
4) `mine https://github.com/padriano/catpeds --comm df561f066f389cdc6e4c7f126ca7f505d45b470f,f2387add387af0a54d88ac3c2a67bd4cdbaad94d --database <insert path> --dump-json`

Validation by identifiers: `--validate <libName1(from)> <libName2(to)>` Important: corresponding .txt files with identifiers must be in "Data\\kwSet" folder!

##### **generalize**

Generalizes patterns that have been mined previously.
Uses anti-unification methods to reduce the number of redundant patterns.

~~~
java -jar patternika.jar generalize patterns.json
~~~

Input: .json file with patterns.
Output: .json file with generalized patterns.

### Patch Code actions

Output: depends on an option (src, AST or json)

* **patch** - patch file or directory with specified `.json` pattern.

Input: 1 input `.java` file / folder and 1 input `.json` file.
Note: if only `.json` file provided it works as **www-patch** action;

* **www-patch** - Starts a web server http://localhost:8000.
  You can patch code through the web interface. JSON file
  specified on web server start: 1 input `.json` file.


### Debug Actions

Output: depends on an option (src, AST or json)

* **parse-file** - parse source file to the AST.

Input: 1 source java file

* **parse-folder** - parse all source files from specified folder.

Input: 1 input directory path.

Output: creates output files with numbering, not with original names.

* **parse** - parse source file to the AST.

Input: can be file or folder.

Option `--generate` allows to parse source file with markup.
Example: `parse D:\code.json --generate --database D:\result_1.json --dump-json`

* **load-json** - parse AST from JSON to internal representation.

Input: 1 input .json file.

* **diff** - compares two `.java` files and creates resulting AST with
  actions that transform the first tree to the second. 2 input `.java` files.

* **match** - matching a source file and a pattern and marking all 
  matched nodes on the resulting AST.

Input: 1 input `.java` file and 1 input `.json` file

### Options
Zero or more options, from the list:
* **--dump-src** - Dump AST as source code.
* **--dump-ast** - Dump AST to file in the `DOT` format.
  The `dot` tool from the `Graphviz` package required to convert the `DOT` format to an image.
* **--dump-json** - Dump to `JSON` format.
* **--print-json** - Print to stdout in `JSON` format.
* **--print-src** - Print AST to stdout as source code.
* **--enable-traits** - Generate traits and enable features that involve them.

### Logging
Logging is available in the project. Configure levels and outputs by next options:
* **--log** - set a log file(s) for different loggers or for all of them
* **--log-level** - set a log level for different loggers or for all of them

Log all the messages with level **FINE** (default level is **INFO**)
from all the loggers to the `log.txt` file:
~~~
java -jar patternika.jar --www-patch patterns.json --log log.txt --log-level FINE
~~~

Log all the loggers to the `log.txt` file, but **myLogger** to the `myLog.txt` file:
~~~
java -jar patternika.jar --www-patch patterns.json --log "log.txt myLogger=myLog.txt"
~~~

Log all the loggers to the `log.txt` file and additionally **myLogger** to the `myLog.txt` file:
~~~
java -jar patternika.jar --www-patch patterns.json --log "log.txt myLogger+=myLog.txt"
~~~

Log **myLogger** to the `myLog.txt` file and print it to console as well:
~~~
java -jar patternika.jar --www-patch patterns.json --log myLogger+=myLog.txt
~~~

Log **myLogger** only to the `myLog.txt` file and don't print it to console:
~~~
java -jar patternika.jar --www-patch patterns.json --log myLogger=myLog.txt
~~~

Log all the messages with level **FINE** from **myLogger**,
but keep all the other loggers on default level:
~~~
java -jar patternika.jar --www-patch patterns.json --log-level myLogger=FINE
~~~

Log all the messages with level **FINE** from **myLogger**,
and all the other loggers on level **WARNING**:
~~~
java -jar patternika.jar --www-patch patterns.json --log-level "WARNING myLogger=FINE"
~~~

### DefectInstance Actions
* **create-di** - create a DefectInstance JSON file

Input:
`.java` file with source code, `.json` file with patterns,
`.json` path for a new JSON file with DefectInstances.

Example:
`java -jar patternika.jar create-di buggyFile.java patterns.json DIfolder/defectInstance_1.json`

### Examples:

#### Example 1

Match a source file and a pattern, then save a result as PNG image:

~~~
java -jar patternika.jar --match Source.java Patch.json --dump-ast --log log.txt --log-level ALL
"c:\Program Files (x86)\Graphviz2.38\bin\dot.exe" -Tpng Source.java.ast.txt -O
~~~
#### Example 2

If you want to mine patterns from changes, corresponding with "spring-boot-starter-parent" version change from
2.1.7.RELEASE to 2.2.2.RELEASE in https://github.com/padriano/catpeds project, you should use:

`--mine https://github.com/padriano/catpeds --pom --fromVer 2.1.7.RELEASE --toVer 2.2.2.RELEASE`

Or, if you already have cloned project, you can specify path to it on your PC, for example
`mine D:\catpeds --pom --fromVer 2.1.7.RELEASE --toVer 2.2.2.RELEASE`

As a result you will get file 1.json (in the folder where /catpeds located)
with mined patterns in it.

#### Example 3

If you want to mine patterns from changes, corresponding with ARTIFACTID
value change from "junit-jupiter-api" to
"junit" in https://github.com/cedricwalter/git-branch-renamer-maven-plugin project, you should use:

`--mine https://github.com/cedricwalter/git-branch-renamer-maven-plugin --pom --fromArtId junit-jupiter-api --toArtId junit`
