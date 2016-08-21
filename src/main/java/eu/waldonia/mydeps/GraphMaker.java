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
    private static final String MERGE_ALL = "MATCH (a:App {name:\"${appName}\"}) MERGE (a)-[:DEPENDS_ON {scope:\"${scope}\"}]-(p:Provider {name:\"${providerName}\"})-[:PROVIDES]-(l:Artifact {name:\"${artifactName}\"})-[:VERSION]-(v:Version {number:\"${versionNumber}\"})";

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

    /*
     * MERGE
     *  (a:App {name:\"${appName}\"})
     *      -[:DEPENDS_ON, {scope:\"${scope}\"}]
     *  -(p:Provider {name:\"${providerName}\"})
     *      -[:PROVIDES]
     *  -(l:Artifact {name:\"${artifactName}\"})
     *      -[:VERSION]
     *  -(v:Version {number:\"${versionNumber}\"})"
     */

    public void update(Dependency d, String appName) {

        Map<String,String> valuesMap = new HashMap<>();
        valuesMap.put("id",appName);
        valuesMap.put("appName",appName);
        valuesMap.put("scope", d.getScope());
        valuesMap.put("providerName", d.getGroupId());
        valuesMap.put("artifactName", d.getArtifactId());
        valuesMap.put("versionNumber", d.getVersion());

        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        String mergeApp = sub.replace(MERGE_APP);
        String mergeAll = sub.replace(MERGE_ALL);

        Transaction tx = session.beginTransaction();
        tx.run(mergeApp);
        tx.run(mergeAll);
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
