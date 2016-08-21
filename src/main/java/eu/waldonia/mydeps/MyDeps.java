package eu.waldonia.mydeps;

import org.apache.maven.model.Dependency;

import java.util.List;

/**
 * @author sih
 */
public class MyDeps {


    public static void main(String[] args) {
        try{
            MavenParser mp = new MavenParser();
            List<Dependency> deps = mp.parse("/Users/sid/dev/mydeps/src/main/resources/lwmpom.xml");
            GraphMaker maker = new GraphMaker(args[0], args[1]);

            maker.update(deps,"LWM");
            maker.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
