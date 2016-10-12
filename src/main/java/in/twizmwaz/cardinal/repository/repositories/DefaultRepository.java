package in.twizmwaz.cardinal.repository.repositories;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.repository.exception.RotationLoadException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class DefaultRepository extends Repository {

    @SuppressWarnings("FieldCanBeLocal")
    private String DEFAULT_MAP_RESOURCE = "DefaultMap";

    public DefaultRepository() throws RotationLoadException, IOException {
        super(Cardinal.getNewRepoPath("default-cardinal-repo"));
    }

    @Override
    public void refreshRepo() throws RotationLoadException, IOException {
        cloneResources();
        super.refreshRepo();
    }

    private void cloneResources() throws IOException {
        cloneFile("map.xml");
        cloneFile("level.dat");
        cloneFile("region/r.0.0.mca" );
        cloneFile("region/r.0.-1.mca");
        cloneFile("region/r.-1.0.mca");
        cloneFile("region/r.-1.-1.mca");
    }

    private void cloneFile(String file) throws IOException {
        file = DEFAULT_MAP_RESOURCE + "/" + file;
        FileUtils.copyInputStreamToFile(Cardinal.getInstance().getResource(file), new File(getPath(), file));
    }

    @Override
    public String getSource(boolean op) {
        return "Default Cardinal Repository";
    }

}
