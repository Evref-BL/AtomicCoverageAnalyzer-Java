# Atomic Coverage Analyzer for Java

Determine the coverage of each test method with [JUnit](https://junit.org/junit4/) and [JaCoCo](https://www.jacoco.org/).  
Run JUnit cases or suites, get a JaCoCo execution data file (.exec) for each test method.

This is developed as part of the [Modest](https://github.com/Evref-BL/Modest) project in order to optimize generated test suites.  
However, this is applicable to any Java project.

## Usage

The analyzer is both an executable and an agent.
The classpath is needed to find the classes in the project to be analyzed.
It expects the path to an output directory where the exec files will be generated, and a list of fully qualified test case/suite names.

```sh
java \
  -jar atomic-coverage-analyzer.jar \
  -javaagent:atomic-coverage-analyzer.jar \
  -classpath <analyzed-paths> \
  <output-directory> <cases-or-suites...>
```

## Build

```sh
mvn clean install
```
