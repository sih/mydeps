# My Dependencies (mydeps)
This parses output from mvn dependency:list and stores as a graph in Neo4j. 

![Dependency Example](/images/snapshot.png)


The graph captures:
* What group (provider) the app depends on
* The scope (compile, test, runtime) of the dependency - this is stored as a property of the DEPENDS_ON relationship
* The artifact, i.e. the library
* The version of the library

# To Do

* Include packaging details (probably as a property of the artifact)
* Pull maven files from GitHub repositories and build these automagically
