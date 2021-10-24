package dev.hawu.plugins.api;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Asset {

    private final long id;
    private final String name;
    private final String label;
    private final String contentType;
    private final long size;
    private final String downloadURL;

    public Asset(final long id, final @NotNull String name, final @Nullable String label,
                 final @NotNull String contentType, final long size, final @NotNull String downloadURL) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.contentType = contentType;
        this.size = size;
        this.downloadURL = downloadURL;
    }

    public long getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getLabel() {
        return label;
    }

    @NotNull
    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        return size;
    }

    @NotNull
    public String getDownloadURL() {
        return downloadURL;
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final Asset asset = (Asset) o;
        return new EqualsBuilder().append(id, asset.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }

    @NotNull
    public String toString() {
        return "Asset{" + "\"id\":" + id + "," +
            "\"name\":" + name + "," +
            "\"label\":" + label + "," +
            "\"size\":" + size + "," +
            "\"content_type\":" + contentType + "," +
            "\"download\":" + downloadURL + "}";
    }

}
