package eu.waldonia.mydeps;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sih
 */
public class MavenParser {


    private static final Logger LOGGER = LoggerFactory.getLogger(MavenParser.class);

    /**
     * @param pom The POM file to read
     * @return A list of the dependencies found
     */
    List<Dependency> parse(final String pom) throws IOException, XmlPullParserException {
        List<Dependency> dependencies = new ArrayList<>();
        MavenXpp3Reader mavenReader = new MavenXpp3Reader();
        Model model = mavenReader.read(new StringReader(pom));

        dependencies =  model.getDependencies();



        dependencies.forEach(d -> {
            if (null == d.getScope()) d.setScope("compile");

        });


        return dependencies;
    }
}
