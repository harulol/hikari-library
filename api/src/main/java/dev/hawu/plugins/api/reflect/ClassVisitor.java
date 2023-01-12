package dev.hawu.plugins.api.reflect;

import dev.hawu.plugins.api.Tasks;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Holds the final instance of a class visitor that should
 * visit all possible jar entries within jar files.
 * <p>
 * This may be invading privacy.
 *
 * @since 2.0
 */
public final class ClassVisitor {

    private static final Set<String> PACKAGES = new HashSet<>();
    private static final Map<String, List<String>> CLASSES = new HashMap<>();

    private static List<JarFile> retrieveEntries(final @NotNull File file) {
        if(!file.isDirectory())
            return Collections.emptyList();

        final List<JarFile> entries = new ArrayList<>();
        final File[] files = file.listFiles();
        if(files == null)
            return Collections.emptyList();

        for(final File f : files) {
            try {
                if(f.isDirectory()) entries.addAll(retrieveEntries(f));
                else entries.add(new JarFile(f));
            } catch(final Exception exception) {
                // Ignore.
            }
        }

        return entries;
    }

    private static void scan() {
        final List<JarFile> jars = retrieveEntries(new File("."));
        for(final JarFile file : jars) {
            for(Enumeration<JarEntry> e = file.entries(); e.hasMoreElements(); ) {
                final JarEntry entry = e.nextElement();
                if(entry.isDirectory() || entry.getName().startsWith("META-INF") || !entry.getName().endsWith(".class")) continue;

                // Base random files, not classes.
                int lastIndex = entry.getName().lastIndexOf('/');
                if(lastIndex < 0)
                    continue;

                final String directoryName = entry.getName().substring(0, entry.getName().lastIndexOf('/'));
                final String entryName = entry.getName().substring(entry.getName().lastIndexOf('/') + 1);

                PACKAGES.add(directoryName);
                CLASSES.putIfAbsent(directoryName, new ArrayList<>());
                CLASSES.get(directoryName).add(entryName);
            }
        }
    }

    /**
     * Starts scanning all files from the root directory of the server
     * for all JarFiles.
     * <p>
     * So that this doesn't only include plugins, but also bundler's jars
     * that the bootstrapper pulls out of its arse.
     *
     * @since 2.0
     */
    public static void scavenge() {
        PACKAGES.clear();
        CLASSES.clear();

        synchronized(PACKAGES) {
            synchronized(CLASSES) {
                Tasks.scheduleAsync(runnable -> scan());
            }
        }
    }

    /**
     * Retrieves an unmodifiable view of the classes gathered.
     *
     * @return a map of names of packages and the items inside that package
     * @since 2.0
     */
    @NotNull
    public static Map<String, List<String>> getClasses() {
        return Collections.unmodifiableMap(CLASSES);
    }

    /**
     * Retrieves an unmodifiable view of the packages gathered.
     *
     * @return a set of names of packages
     * @since 2.0
     */
    @NotNull
    public static Set<String> getPackages() {
        return Collections.unmodifiableSet(PACKAGES);
    }

}
