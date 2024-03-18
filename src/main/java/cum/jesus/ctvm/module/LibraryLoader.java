package cum.jesus.ctvm.module;

import cum.jesus.ctni.INativeLoader;
import cum.jesus.ctni.NativeFunction;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public final class LibraryLoader {
    private final Module module;
    private Map<String, URLClassLoader> classLoaders = new HashMap<>();

    public LibraryLoader(Module module) {
        this.module = module;
    }

    public static LibraryLoader get(Module module) {
        if (module.hasLibraryLoader()) {
            return module.getLibraryLoader();
        }

        module.setLibraryLoader();
        return module.getLibraryLoader();
    }

    private URLClassLoader getOrCreateClassLoader(String jarPath) throws MalformedURLException {
        URLClassLoader classLoader = classLoaders.get(jarPath);
        if (classLoader == null) {
            classLoader = URLClassLoader.newInstance(new URL[] { new URL("jar:file:" + jarPath + "!/") });
            classLoaders.put(jarPath, classLoader);
        }
        return classLoader;
    }

    public void loadJarArchive(String jarPath) throws IOException {
        try (JarFile jarFile = new JarFile(jarPath)) {
            Manifest manifest = jarFile.getManifest();

            if (manifest == null) {
                System.out.println("no manifest");
                return;
            }

            String loaderPath = manifest.getMainAttributes().getValue("CT-Loader");

            if (loaderPath == null) {
                System.out.println("couldn't find 'CT-Loader'");
                return;
            }

            try {
                URLClassLoader classLoader = getOrCreateClassLoader(jarPath);
                Class<?> loaderClass = classLoader.loadClass(loaderPath);

                if (INativeLoader.class.isAssignableFrom(loaderClass)) {
                    INativeLoader loader = (INativeLoader) loaderClass.getDeclaredConstructor().newInstance();
                    loader.injectMethods(module.getNatives());
                }
            } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
