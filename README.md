# My Dependencies (mydeps)
This uses the GitHub API to retrieve repository details for a user, extract any Maven files found, and then create a dependency graph that's stored in Neo4j.

![Dependency Example](/images/snapshot.png)

The graph captures:
* What group (provider) the app depends on
* The scope (compile, test, runtime) of the dependency - this is stored as a property of the DEPENDS_ON relationship
* The artifact, i.e. the library
* The version of the library

The utility uses both the [GitHub HTTP API](https://developer.github.com/v3/) and the [Maven Model library](http://maven.apache.org/ref/3.2.5/maven-model). 

The picture below shows a processing pipeline 

## Running the app

### Pre-requisites

You will need: 
* A [Personal Access token](https://github.com/settings/tokens) that allows repo access to your app for GitHub
* A locally running [Neo4J graph database](https://neo4j.com/download/)
* [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

### Installing
* Run ````mvn clean install```` to build the app
* From the project directory and run ````java -jar ./target/mydeps-1.0-SNAPSHOT.jar <gituser> <oauth token> <neo4juser> <neo4jpass>````

## TODOs
* Rewrite query to MERGE library details rather than create new
* Queries to explore the graph