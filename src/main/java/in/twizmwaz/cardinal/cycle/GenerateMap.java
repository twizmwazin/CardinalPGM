package in.twizmwaz.cardinal.cycle;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class GenerateMap {

    public static void copyWorldFromRepository(File source, UUID uuid) {
        File dest = new File("matches/" + uuid.toString());
        try {
            FileUtils.copyDirectory(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
