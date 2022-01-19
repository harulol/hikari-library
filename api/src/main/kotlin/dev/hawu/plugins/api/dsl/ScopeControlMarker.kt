package dev.hawu.plugins.api.dsl

/**
 * Scope control marker for DSL-like builders.
 * @since 1.5
 */
@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
annotation class ScopeControlMarker
