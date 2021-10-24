package dev.hawu.plugins.api;

import dev.hawu.plugins.api.reflect.MinecraftVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public final class PluginPackage {

    private final String id;
    private final String name;
    private final String description;
    private final String compatibility;
    private final boolean premium;
    private final List<String> dependencies;
    private final List<Release> releases;

    private Release latestRelease;
    private Release latestPrerelease;
    private Release latestDev;

    public PluginPackage(final @NotNull String id, final @NotNull String name, final @NotNull String description,
                         final @NotNull String compatibility, final boolean premium,
                         final @NotNull List<@NotNull String> dependencies, final @NotNull List<@NotNull Release> releases) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.compatibility = compatibility;
        this.premium = premium;
        this.dependencies = dependencies;
        this.releases = releases;
        this.computeReleases();
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    @NotNull
    public String getCompatibility() {
        return compatibility;
    }

    public boolean isPremium() {
        return premium;
    }

    @NotNull
    public List<@NotNull String> getDependencies() {
        return dependencies;
    }

    @NotNull
    public List<@NotNull Release> getReleases() {
        return Collections.unmodifiableList(this.releases);
    }

    public boolean hasRelease(final @NotNull String tag) {
        return releases.stream().anyMatch(s -> s.getTagName().equals(tag));
    }

    @Nullable
    public Release getLatestRelease() {
        return latestRelease;
    }

    @Nullable
    public Release getLatestPrerelease() {
        return latestPrerelease;
    }

    @Nullable
    public Release getLatestDev() {
        return latestDev;
    }

    private void computeReleases() {
        for(final Release release : releases) {
            switch(release.getReleaseType()) {
                case RELEASE:
                    if(latestRelease == null) latestRelease = release;
                    break;
                case PRERELEASE:
                    if(latestPrerelease == null) latestPrerelease = release;
                    break;
                case DEV:
                    if(latestDev == null) latestDev = release;
                    break;
            }
        }
    }

    public boolean isCompatible() {
        final String[] arr = this.compatibility.split("\\.");
        final int major = Integer.parseInt(arr[0]);
        final int minor = Integer.parseInt(arr[1]);
        final int patch = arr.length > 2 ? Integer.parseInt(arr[2]) : 0;

        return MinecraftVersion.getCurrent().isAtLeast(major, minor, patch);
    }

}
