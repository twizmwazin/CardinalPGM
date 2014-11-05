package in.twizmwaz.cardinal.rotation;

import net.minecraft.util.org.apache.commons.codec.Charsets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/* In this rotation class,rotation will be an array containing the maps and their order.
 Position will be the position of the currently playing map. */

public class Rotation {

    private String[] rotation;
    private int position;
    private String next;
    private File file;
    private boolean override;

    public Rotation(File file) {
        this.file = file;
        this.position = 0;
        this.refresh();
        this.next = rotation[position + 1];

    }

    public void refresh() {
        try {
            List<String> lines = Files.readAllLines(file.toPath(), Charsets.UTF_8);
            this.rotation = lines.toArray(new String[lines.size()]);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        position = 0;
        override = false;
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
        if (override){
        return next;}
        else return rotation[position];
    }

    public String getCurrent() {
        return rotation[position];
    }

    public String getEntry(int pos) {
        return rotation[pos];
    }

    public void setNext(String map) {
        next = map;
        override = true;
    }

}
