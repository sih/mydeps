package eu.waldonia.mydeps;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Accept: application/vnd.github.v3+json
 * curl -H "Authorization: token d39e752ede51e8f3dda3f19f7df25d50042f2895" https://api.github.com/users/sih -I
 * curl -H "Authorization: token d39e752ede51e8f3dda3f19f7df25d50042f2895" "https://api.github.com/user/repos?visibility=all&affiliation=owner,collaborator" -I
 * curl -H "Authorization: token d39e752ede51e8f3dda3f19f7df25d50042f2895" "https://api.github.com/user/repos?visibility=all&affiliation=collaborator"
 * curl -H "Authorization: token d39e752ede51e8f3dda3f19f7df25d50042f2895" -H "Accept: application/vnd.github.v3.raw" "https://api.github.com/repos/sih/mydeps/contents/pom.xml"
 */
public class GitHubReader {

    private CloseableHttpClient http;
    private HttpGet get;


    private static final String RAW_OUTPUT = "application/vnd.github.v3.raw";
    private static final String JSON_OUTPUT = "application/vnd.github.v3+json";
    private static final String USER_REPOS_ENDPOINT = "https://api.github.com/user/repos?visibility=all&affiliation=owner,collaborator";

    private static final String NAME_KEY = "name";
    private static final String API_URL_KEY = "url";


    public GitHubReader() {
        http = HttpClients.createDefault();
    }

    private Logger LOGGER = LoggerFactory.getLogger(GitHubReader.class);

    Map<String,String> readRepos(String gitUser, String oauthToken) {
        LOGGER.info("Reading repos for user "+gitUser);
        LOGGER.debug("Using OAuth token "+oauthToken);

        HttpGet get = new HttpGet(USER_REPOS_ENDPOINT);
        get.setHeader("Authorization", "token "+oauthToken);
        get.setHeader("Accept", JSON_OUTPUT);

        Map<String,String> repoDetails = new HashMap<>();

        try (CloseableHttpResponse response = http.execute(get)) {

            HttpEntity entity = response.getEntity();

            StringWriter writer = new StringWriter();
            IOUtils.copy(entity.getContent(),writer, Charset.forName("utf-8"));

            String jsonRepos = writer.toString();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode items = mapper.readTree(jsonRepos);

            for(JsonNode item: items) {
                String name = item.get(NAME_KEY).getTextValue();
                String url = item.get(API_URL_KEY).getTextValue();

                LOGGER.debug(name+" => "+url);
                repoDetails.put(name,url);

            }


        }
        catch (IOException ioe) {
            LOGGER.error(ioe.getMessage());
        }


        return repoDetails;
    }




    public static void main(String[] args) {
        GitHubReader g = new GitHubReader();
        g.readRepos(args[0], args[1]);
    }

}
