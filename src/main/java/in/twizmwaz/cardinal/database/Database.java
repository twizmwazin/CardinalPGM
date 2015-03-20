package in.twizmwaz.cardinal.database;

import com.google.common.collect.Table;

public interface Database {

    public Table getTable(String name);

    public void save();

}
