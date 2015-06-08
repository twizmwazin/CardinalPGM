package in.twizmwaz.cardinal.database;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FlatFile implements Database {

    private final Map<String, Table> tables;
    private final Gson gson = new Gson();
    private final File dir;

    public FlatFile(File dir) {
        this.tables = Maps.newHashMap();
        this.dir = dir;
        if (dir.listFiles() == null) Bukkit.getLogger().severe("Failed to load FlatFile database!");
        for (File table : dir.listFiles(new Filter())) {
            try {
                tables.put(table.getName().split(".")[0], gson.fromJson(new FileReader(table), HashBasedTable.class));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public Table getTable(String name) {
        if (tables.get(name) != null) return tables.get(name);
        else {
            Table toReturn = HashBasedTable.create();
            tables.put(name, toReturn);
            return toReturn;
        }
    }

    @Override
    public void save() {
        for (String key : tables.keySet()) {
            File saveFile = new File(dir, key + ".json");
            try {
                JsonWriter writer = new JsonWriter(new FileWriter(saveFile));
                gson.toJson(tables.get(key), HashBasedTable.class, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class Filter implements FileFilter {
        @Override
        public boolean accept(File file) {
            return file.getName().endsWith(".json");
        }
    }

}
