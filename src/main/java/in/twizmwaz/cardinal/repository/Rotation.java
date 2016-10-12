package in.twizmwaz.cardinal.repository;

import in.twizmwaz.cardinal.repository.exception.RotationLoadException;
import in.twizmwaz.cardinal.util.Config;
import org.apache.commons.io.Charsets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Rotation extends ArrayList<LoadedMap> {

    private int position = 0;

    /**
     * Refreshes the plugin's default rotation
     */
    public void refreshRotation(Stream<LoadedMap> loadedMaps) throws RotationLoadException, IOException {
        this.clear();
        File rotationFile = new File(Config.rotation);
        if (!rotationFile.exists()) {
            throw new RotationLoadException("Rotation file doesn't exist");
        }
        List<String> lines = Files.readAllLines(rotationFile.toPath(), Charsets.UTF_8).stream()
                .map(line -> line.replaceAll(" ", "").toLowerCase()).collect(Collectors.toList());
        if (lines.size() < 1) {
            throw new RotationLoadException("Rotation file is empty");
        }
        loadedMaps.filter(map -> lines.contains(map.getName().replaceAll(" ", "").toLowerCase())).forEach(this::add);
        if (this.size() == 0) {
            throw new RotationLoadException("Rotation has no maps");
        }
        position = 0;
    }

    /**
     * Move the position in the rotation by one. If the end of the rotation is reached, it will be automatically reset.
     */
    public int move() {
        position++;
        if (position > size() - 1) {
            position = 0;
        }
        return position;
    }

    /**
     * @return Returns the next map in the rotation
     */
    public LoadedMap getNext() {
        return get(position);
    }

    /**
     * @return Gets the rotation index of the next map
     */
    public int getNextIndex() {
        return position;
    }

}
