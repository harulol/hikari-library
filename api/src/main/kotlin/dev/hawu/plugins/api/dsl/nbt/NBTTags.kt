package dev.hawu.plugins.api.dsl.nbt

/**
 * Opens a specification for an NBT Compound.
 */
fun compound(spec: NBTCompoundSpec.() -> Unit) = NBTCompoundSpec().apply(spec).build()

/**
 * Opens a specification for an NBT List.
 */
fun list(spec: NBTListSpec.() -> Unit) = NBTListSpec().apply(spec).build()
