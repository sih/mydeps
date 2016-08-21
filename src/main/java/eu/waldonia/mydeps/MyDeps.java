package eu.waldonia.mydeps;

import org.apache.maven.model.Dependency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author sih
 */
public class MyDeps {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyDeps.class);


    public static void main(String[] args) {
        try{

            String gitUser = args[0];
            String oauthToken = args[1];
            String neo4jUser = args[2];
            String neo4jPass = args[3];

            GitHubReader reader = new GitHubReader();
            MavenParser mp = new MavenParser();
            GraphMaker maker = new GraphMaker(neo4jUser,neo4jPass);

            Map<String,String> repos = reader.readRepos(gitUser,oauthToken);

            for (String repo: repos.keySet()) {
                String repoUrl = repos.get(repo);
                String pom = reader.fetchPom(repoUrl,oauthToken);
                if (pom != null) {
                    LOGGER.info("About to map "+repo);
                    List<Dependency> deps = mp.parse(pom);
                    maker.update(deps,repo);
                }
            }

            maker.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
