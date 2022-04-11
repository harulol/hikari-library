package dev.hawu.plugins.api.dsl.misc

import dev.hawu.plugins.api.dsl.title.TitleComponentSpec

/**
 * Builds a title component.
 */
fun title(spec: TitleComponentSpec.() -> Unit) = TitleComponentSpec().apply(spec).build()
