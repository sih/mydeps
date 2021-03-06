package eu.waldonia.mydeps;

import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.maven.model.Dependency;
import org.neo4j.driver.v1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sih
 */
public class GraphMaker {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphMaker.class);

    private static final String MERGE_APP = "MERGE (a:App {name:\"${appName}\"})";
    private static final String MERGE_ARTIFACT = "MERGE (p:Provider {name:\"${providerName}\"})-[:PROVIDES]-(l:Artifact {name:\"${artifactName}\"})";
    private static final String LINK = "MATCH (a:App {name:\"${appName}\"}), (l:Artifact {name:\"${artifactName}\"}) CREATE (a)-[:${scope} {version:\"${versionNumber}\"}]->(l)";

    private Driver driver;
    private Session session;

    /**
     *
     */
    public GraphMaker(String user, String pass) {
        driver = GraphDatabase
                .driver( "bolt://localhost",
                        AuthTokens.basic( user, pass)
                );
        session = driver.session();
    }


    public void update(Dependency d, String appName) {

        Map<String,String> valuesMap = new HashMap<>();
        valuesMap.put("id",appName);
        valuesMap.put("appName",appName);
        valuesMap.put("scope", d.getScope().toUpperCase());
        valuesMap.put("providerName", d.getGroupId());
        valuesMap.put("artifactName", d.getArtifactId());
        valuesMap.put("versionNumber", d.getVersion());

        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        String mergeApp = sub.replace(MERGE_APP);
        String mergeArtifact = sub.replace(MERGE_ARTIFACT);
        String link = sub.replace(LINK);

        Transaction tx = session.beginTransaction();
        tx.run(mergeApp);
        tx.run(mergeArtifact);
        tx.run(link);
        tx.success();
        tx.close();
    }


    /**
     * @param dependencies
     * @param appName
     */
    public void update(List<Dependency> dependencies, String appName) {
        for (Dependency d: dependencies) {
            update(d,appName);
        }
    }


    public void close() {
        session.close();
    }


}
