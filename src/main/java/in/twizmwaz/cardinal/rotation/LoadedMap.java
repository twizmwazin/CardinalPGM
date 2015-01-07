package in.twizmwaz.cardinal.rotation;

import java.io.File;
import java.util.List;

public final class LoadedMap {
    
    private final String name;
    private final List<String> authors;
    private final File folder;

    /**
     * @param name The name of the map
     * @param authors The authors of the map
     * @param folder The folder where the map can be found
     */
    public LoadedMap(String name, List<String> authors, File folder) {
        this.name = name;
        this.authors = authors;
        this.folder = folder;
    }

    /**
     * @return Returns the name of the map
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the authors of the map
     */
    public List<String> getAuthors() {
        return authors;
    }

    /**
     * @return Returns the folder where the map can be found
     */
    public File getFolder() {
        return folder;
    }
}
