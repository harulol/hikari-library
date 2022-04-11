package dev.hawu.plugins.api.dsl.nbt

import dev.hawu.plugins.api.dsl.ScopeControlMarker
import dev.hawu.plugins.api.nbt.NBTByte
import dev.hawu.plugins.api.nbt.NBTByteArray
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
 * Specification for creating an NBT list.
 */
@ScopeControlMarker
class NBTListSpec {

    private val values = mutableListOf<NBTType>()

    /**
     * Adds an nbt of type.
     */
    operator fun NBTType.unaryPlus() {
        values.add(this)
    }

    /**
     * Adds a string NBT tag.
     */
    operator fun String.unaryPlus() {
        values.add(NBTString(this))
    }

    /**
     * Adds a byte array NBT tag.
     */
    operator fun ByteArray.unaryPlus() {
        values.add(NBTByteArray(this))
    }

    /**
     * Adds an int array NBT tag.
     */
    operator fun IntArray.unaryPlus() {
        values.add(NBTIntArray(this))
    }

    /**
     * Adds a long array NBT tag.
     */
    operator fun LongArray.unaryPlus() {
        values.add(NBTLongArray(this))
    }

    /**
     * Retrieves an NBT byte.
     */
    fun byte(byte: Byte) = NBTByte(byte)

    /**
     * Retrieves an NBT short.
     */
    fun short(short: Short) = NBTShort(short)

    /**
     * Retrieves an NBT int.
     */
    fun int(int: Int) = NBTInt(int)

    /**
     * Retrieves an NBT long.
     */
    fun long(long: Long) = NBTLong(long)

    /**
     * Retrieves an NBT float.
     */
    fun float(float: Float) = NBTFloat(float)

    /**
     * Retrieves an NBT double.
     */
    fun double(double: Double) = NBTDouble(double)

    internal fun build() = NBTList().apply {
        addAll(values)
    }

}
