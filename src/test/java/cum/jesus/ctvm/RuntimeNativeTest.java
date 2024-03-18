package cum.jesus.ctvm;

import cum.jesus.ctni.INativeLoader;
import cum.jesus.ctni.NativeFunction;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public final class RuntimeNativeTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String jarPath = "C:/Users/Jannik/IdeaProjects/CTVMNative/build/libs/CTVMNative-0.1.jar";

        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace("/", ".").replaceAll("\\.class$", "");
                    System.out.println(className);
                }
            }

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

            try (URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new URL("jar:file:" + jarPath + "!/") })) {
                Class<?> loaderClass = classLoader.loadClass(loaderPath);

                if (INativeLoader.class.isAssignableFrom(loaderClass)) {
                    INativeLoader loader = (INativeLoader) loaderClass.getDeclaredConstructor().newInstance();
                    Map<String, NativeFunction> tmp = new HashMap<>();
                    loader.injectMethods(tmp);
                }
            }
        }
    }
}
