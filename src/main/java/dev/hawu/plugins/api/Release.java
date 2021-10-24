package dev.hawu.plugins.api;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Release {

    private final long id;
    private final String name;
    private final String tagName;
    private final boolean prerelease;
    private final List<Asset> assets;
    private final ReleaseType type;

    public Release(final long id, final @NotNull String name, final @NotNull String tagName,
                   final boolean prerelease, final @NotNull List<@NotNull Asset> assets) {
        this.id = id;
        this.name = name;
        this.tagName = tagName;
        this.prerelease = prerelease;
        this.assets = assets;

        if(this.prerelease && (this.tagName.toLowerCase().contains("dev") || this.tagName.toLowerCase().contains("nightly"))) {
            this.type = ReleaseType.DEV;
        } else if(this.prerelease) {
            this.type = ReleaseType.PRERELEASE;
        } else this.type = ReleaseType.RELEASE;
    }

    public long getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getTagName() {
        return tagName;
    }

    public boolean isPrerelease() {
        return prerelease;
    }

    @NotNull
    public List<@NotNull Asset> getAssets() {
        return assets;
    }

    @NotNull
    public ReleaseType getReleaseType() {
        return this.type;
    }

    enum ReleaseType {
        RELEASE, PRERELEASE, DEV
    }

}
