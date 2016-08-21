package eu.waldonia.mydeps;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sih
 */
public class MavenParser {


    private static final Logger LOGGER = LoggerFactory.getLogger(MavenParser.class);

    /**
     * @param filePath
     * @return
     */
    List<Dependency> parse(final String filePath) throws IOException, XmlPullParserException {
        List<Dependency> dependencies = new ArrayList<>();

        MavenXpp3Reader mavenReader = new MavenXpp3Reader();
        File pom = Paths.get(filePath).toFile();
        Model model = mavenReader.read(new FileReader(pom));


        dependencies =  model.getDependencies();
        return dependencies;
    }
}
