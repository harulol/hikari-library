package dev.hawu.plugins.api.dsl.misc

import dev.hawu.plugins.api.reflect.LambdaFactory
import java.lang.invoke.CallSite
import java.lang.invoke.MethodHandles
import java.lang.reflect.Method

/**
 * Generates a callsite using the provided method.
 *
 * If [lookup] is provided, it will use that lookup
 * instead of the default lookup generated within [LambdaFactory].
 */
fun Method.generateLambda(lookup: MethodHandles.Lookup? = null): CallSite {
    return if(lookup == null) LambdaFactory.generate(this) else LambdaFactory.generate(lookup, this)
}
