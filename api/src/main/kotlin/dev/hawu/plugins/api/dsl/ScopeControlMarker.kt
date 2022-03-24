package dev.hawu.plugins.api.dsl

/**
 * A marker to specify the scope control.
 */
@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ScopeControlMarker
