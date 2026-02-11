package sp.Mods;
import NativeLibraries.JsonManager;
import sp.Annotations.Overrider;
import sp.Annotations.mod;

import java.io.File;

@mod
public abstract class Mod{
    /**
     * this method gets metadata of mod
     * @param pack  the package of mod
     *
     * @return ModMetaData exemplary
     */
    public ModMetaData GetMeta(String pack) {
        JsonManager JM = new JsonManager();
        JM.setTargetFile(new File(pack + "/modData.json"));

        return new ModMetaData(JM.getField("name"),JM.getField("desc"),JM.getArrayField("require"));
    }

    /**
     * method for register override classes
     * @apiNote it called on loading mod, and you can add here any code for loading mods
     */
    public abstract void RegisterOverrides();
}