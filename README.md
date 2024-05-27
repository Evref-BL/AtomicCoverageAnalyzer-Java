# Atomic Coverage Analyzer for Java

Determine the coverage of each test method with [JUnit 4](https://junit.org/junit4/) and [JaCoCo](https://www.jacoco.org/).  
Run JUnit cases or suites, get a JaCoCo execution data file (.exec) for each test method.

> [!NOTE]
> This is developed as part of the [Modest](https://github.com/Evref-BL/Modest) project in order to optimize generated test suites.  
> However, it is applicable to any Java project.

## Usage

The analyzer is both an executable and an agent.  
It must be run with the `-classpath` option, and its value must contain the classpath of the project to be analyzed.  
It takes as arguments:
- `outpath`, path to an output directory where the exec files will be generated.
- `tests`, list of fully qualified test case/suite names.

```sh
java \
  -javaagent:atomic-coverage-analyzer.jar \
  -classpath atomic-coverage-analyzer.jar:<classpath> \
  fr.evref.modest.AtomicCoverageAnalyzer \
  <outpath> <tests...>
```

> [!IMPORTANT]
> The classpath is needed to find the classes in the project to analyze.
> This means that the `-jar` option cannot be used to run this program.
> From the [documentation](https://docs.oracle.com/javase/7/docs/technotes/tools/solaris/java.html):
>> When you use this option, the JAR file is the source of all user classes, and other user class path settings are ignored.

## Build

Using Maven:
```sh
mvn clean install
```
The generated jar can be found in the `target` folder.
