package in.twizmwaz.cardinal.cycle;

import net.minecraft.util.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by kevin on 10/31/14.
 */
public class GenerateMap {

    public static void copyWorldFromRepository(String mapname, UUID uuid) {
        File src = new File("maps/" + mapname);
        File dest = new File("matches/" + uuid.toString());
        try {
            FileUtils.copyDirectory(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
