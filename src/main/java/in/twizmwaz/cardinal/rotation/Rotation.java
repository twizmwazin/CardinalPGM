package in.twizmwaz.cardinal.rotation;

import in.twizmwaz.cardinal.rotation.exception.RotationLoadException;
import net.minecraft.util.org.apache.commons.codec.Charsets;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;

/* In this rotation class, rotation will be an array containing the maps and their order.
 Position will be the position of the currently playing map. */

public class Rotation {

    private String[] rotation;
    private int position;
    private String next;
    private File file;
    private boolean override;

    public Rotation(File file) {
        try {
            setRotationFile(file);
            this.position = 0;
            this.refresh();
            this.next = rotation[position + 1];
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.WARNING, ex.getMessage());
        }


    }

    public void refresh() throws RotationLoadException {
        try {
            List<String> lines = Files.readAllLines(file.toPath(), Charsets.UTF_8);
            this.rotation = lines.toArray(new String[lines.size()]);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            throw new RotationLoadException("An error occurred in loading the rotation file.");
        }
        position = 0;
        override = false;
    }

    public void setRotationFile(File file) throws RotationLoadException {
        try {
            this.file = file;
        } catch (NullPointerException ex) {
            throw new RotationLoadException("An error occurred in loading the rotation file. Is it missing?");
        }
    }

    public String move() {
        if (!override) {
            position++;
        }
        override = false;
        if (position > rotation.length - 1) {
            position = 0;
        }
        return rotation[position];
    }

    public String getNext() {
        if (override) {
            return next;
        } else return rotation[position];
    }

    public String getCurrent() {
        return rotation[position];
    }

    public String getEntry(int pos) throws RotationLoadException {
        try {
            return rotation[pos];
        } catch (NullPointerException ex) {
            throw new RotationLoadException("Could not load map. Is there an issue with the rotation?");
        }
    }

}
