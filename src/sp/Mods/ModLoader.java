package sp.Mods;

import NativeLibraries.log.log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ModLoader {

    private static final String MODS_DIRECTORY = System.getenv("APPDATA") + "/ResonantDebris/mods";

    public static void loadMods() {
        File modsDir = new File(MODS_DIRECTORY);

        if (!modsDir.exists()) {
            modsDir.mkdirs();
            return;
        }

        File[] modFiles = modsDir.listFiles(file -> file.isFile() && file.getName().endsWith(".jar"));

        if (modFiles == null || modFiles.length == 0) {
            return;
        }

        List<URL> urls = new ArrayList<>();
        for (File modFile : modFiles) {
            try {
                urls.add(modFile.toURI().toURL());
            } catch (IOException e) {
               log.Error("Failed to convert the mod file to URL: " + modFile.getName() + ": " + e.getMessage());
            }
        }

        try (URLClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[0]), ModLoader.class.getClassLoader())) { // Родительским загрузчиком классов должен быть загрузчик классов вашей игры.
            for (File modFile : modFiles) {
                try {
                    // получение главного класса из манифеста Jar
                    JarFile jarFile = new JarFile(modFile);
                    Manifest manifest = jarFile.getManifest();
                    String mainClassName = null;
                    if (manifest != null) {
                        mainClassName = manifest.getMainAttributes().getValue("Main-Class");
                    }
                    if (mainClassName == null) {
                        log.Error("No Main-Class attribute defined in a manifest for mod: " + modFile.getName());
                        continue;
                    }

                    Class<?> modClass = classLoader.loadClass(mainClassName);
                    Object modInstance = modClass.getDeclaredConstructor().newInstance(); // Создание экземпляра класса

                    // Вызов метода RegisterOverrides
                    Method registerOverridesMethod = null;
                    try {
                        registerOverridesMethod = modClass.getMethod("RegisterOverrides");
                    } catch (NoSuchMethodException e) {
                        log.Error("Method RegisterOverrides not found in " + mainClassName);
                        continue;
                    }

                    registerOverridesMethod.invoke(modInstance);


                } catch (Exception e) {
                    log.Error("Failed to load mod from " + modFile.getName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            log.Error("Failed to create URLClassLoader: " + e.getMessage());
        }
    }
}