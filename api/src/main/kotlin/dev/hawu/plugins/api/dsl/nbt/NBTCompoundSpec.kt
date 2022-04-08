package dev.hawu.plugins.api.dsl.nbt

import dev.hawu.plugins.api.dsl.ScopeControlMarker
import dev.hawu.plugins.api.nbt.NBTByte
import dev.hawu.plugins.api.nbt.NBTByteArray
import dev.hawu.plugins.api.nbt.NBTCompound
import dev.hawu.plugins.api.nbt.NBTDouble
import dev.hawu.plugins.api.nbt.NBTFloat
import dev.hawu.plugins.api.nbt.NBTInt
import dev.hawu.plugins.api.nbt.NBTIntArray
import dev.hawu.plugins.api.nbt.NBTList
import dev.hawu.plugins.api.nbt.NBTLong
import dev.hawu.plugins.api.nbt.NBTLongArray
import dev.hawu.plugins.api.nbt.NBTShort
import dev.hawu.plugins.api.nbt.NBTString
import dev.hawu.plugins.api.nbt.NBTType

/**
 * The specification for the compound.
 */
@ScopeControlMarker
class NBTCompoundSpec internal constructor(private val compound: NBTCompound = NBTCompound()) {

    /**
     * Binds the [value] to a [key][this].
     */
    infix fun String.to(value: NBTType) {
        compound[this] = value
    }

    /**
     * Binds the [value] of type Byte to a [key][this].
     */
    infix fun String.to(value: Byte) {
        compound[this] = NBTByte(value)
    }

    /**
     * Binds the [value] of type Short to a [key][this].
     */
    infix fun String.to(value: Short) {
        compound[this] = NBTShort(value)
    }

    /**
     * Binds the [value] of type Int to a [key][this].
     */
    infix fun String.to(value: Int) {
        compound[this] = NBTInt(value)
    }

    /**
     * Binds the [value] of type Long to a [key][this].
     */
    infix fun String.to(value: Long) {
        compound[this] = NBTLong(value)
    }

    /**
     * Binds the [value] of type Float to a [key][this].
     */
    infix fun String.to(value: Float) {
        compound[this] = NBTFloat(value)
    }

    /**
     * Binds the [value] of type Double to a [key][this].
     */
    infix fun String.to(value: Double) {
        compound[this] = NBTDouble(value)
    }

    /**
     * Binds the [value] of type String to a [key][this].
     */
    infix fun String.to(value: String) {
        compound[this] = NBTString(value)
    }

    /**
     * Binds the [value] of type ByteArray to a [key][this].
     */
    infix fun String.to(value: ByteArray) {
        compound[this] = NBTByteArray(value)
    }

    /**
     * Binds the [value] of type IntArray to a [key][this].
     */
    infix fun String.to(value: IntArray) {
        compound[this] = NBTIntArray(value)
    }

    /**
     * Binds the [value] of type LongArray to a [key][this].
     */
    infix fun String.to(value: LongArray) {
        compound[this] = NBTLongArray(value)
    }

    /**
     * Binds the [value] of type List to a [key][this].
     */
    infix fun String.to(value: NBTList) {
        compound[this] = value
    }

    /**
     * Binds the [value] of type Compound to a [key][this].
     */
    infix fun String.to(value: NBTCompound) {
        compound[this] = value
    }

    /**
     * Copies all values and overrides them into this compound
     * from [other].
     */
    fun copy(other: NBTCompound) {
        compound.putAll(other)
    }

    internal fun build(): NBTCompound {
        return compound
    }

}
