package dev.hawu.plugins.api.gui.pagination;

/**
 * Data holder for whether the defaults have been changed
 * at callsite.
 *
 * @since 2.0
 */
public final class PaginationDefaultData {

    private boolean backTemplateChanged = false;
    private boolean previousButtonTemplateChanged = false;
    private boolean nextButtonTemplateChanged = false;
    private boolean filterTemplateChanged = false;

    PaginationDefaultData(final boolean backTemplateChanged, final boolean previousButtonTemplateChanged,
                          final boolean nextButtonTemplateChanged, final boolean filterTemplateChanged) {
        this.backTemplateChanged = backTemplateChanged;
        this.previousButtonTemplateChanged = previousButtonTemplateChanged;
        this.nextButtonTemplateChanged = nextButtonTemplateChanged;
        this.filterTemplateChanged = filterTemplateChanged;
    }

    boolean isBackTemplateChanged() {
        return backTemplateChanged;
    }

    boolean isPreviousButtonTemplateChanged() {
        return previousButtonTemplateChanged;
    }

    boolean isNextButtonTemplateChanged() {
        return nextButtonTemplateChanged;
    }

    boolean isFilterTemplateChanged() {
        return filterTemplateChanged;
    }

}
