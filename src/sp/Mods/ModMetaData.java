package sp.Mods;

import java.util.ArrayList;

public class ModMetaData {
    public String name;
    public String version;
    public ArrayList<String> require;
    public ModMetaData(String name, String version, ArrayList<String> require){
        this.name = name;
        this.require = require;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public ArrayList<String> getRequire() {
        return require;
    }
}