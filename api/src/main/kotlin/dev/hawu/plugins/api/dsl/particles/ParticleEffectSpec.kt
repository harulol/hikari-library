package dev.hawu.plugins.api.dsl.particles

import dev.hawu.plugins.api.dsl.ScopeControlMarker
import dev.hawu.plugins.api.particles.ParticleEffect
import dev.hawu.plugins.api.particles.ParticleEnum
import org.bukkit.Location
import org.bukkit.util.Vector

/**
 * The specification to build a particle effect
 * elegantly.
 */
@ScopeControlMarker
class ParticleEffectSpec internal constructor() {

    private var particleEnum: ParticleEnum? = null
    private val location = Vector(0, 0, 0)
    private val _offset = Vector(0, 0, 0)
    private var _longDistance = false
    private var count = 0
    private var speed = 0f
    private var data: IntArray? = null

    /**
     * Configures the effect for this particle.
     */
    val effect: IParticleEnumOption
        get() = ParticleEnumOption()

    /**
     * Sets the location of the particle.
     */
    val at: IParticleLocationOption
        get() = ParticleLocationOption()

    /**
     * Sets the speed or the count of the particle.
     */
    val with: IParticleOption
        get() = ParticleOption()

    /**
     * Sets the offset values of the particle.
     */
    val offset: IParticleOffsetOption
        get() = ParticleOffsetOption()

    /**
     * This particle may be viewed from further away,
     * if the server permits it.
     */
    val longDistance: Unit
        get() {
            _longDistance = true
        }

    /**
     * Sets the data of the particle.
     */
    fun data(vararg data: Int) {
        this.data = data
    }

    sealed interface IParticleEnumOption {
        infix fun named(s: String)
        infix fun named(enum: ParticleEnum)
    }

    private inner class ParticleEnumOption : IParticleEnumOption {
        override fun named(s: String) {
            particleEnum = ParticleEnum.getParticle(s)
        }

        override fun named(enum: ParticleEnum) {
            particleEnum = enum
        }
    }

    sealed interface IParticleLocationOption {
        infix fun x(x: Number): IParticleLocationOption
        infix fun y(y: Number): IParticleLocationOption
        infix fun z(z: Number): IParticleLocationOption
        infix fun location(loc: Location): IParticleLocationOption
    }

    private inner class ParticleLocationOption : IParticleLocationOption {
        override fun x(x: Number): IParticleLocationOption {
            location.x = x.toDouble()
            return this
        }

        override fun y(y: Number): IParticleLocationOption {
            location.y = y.toDouble()
            return this
        }

        override fun z(z: Number): IParticleLocationOption {
            location.z = z.toDouble()
            return this
        }

        override fun location(loc: Location): IParticleLocationOption {
            location.x = loc.x
            location.y = loc.y
            location.z = loc.z
            return this
        }
    }

    sealed interface IParticleOption {
        infix fun count(v: Int): IParticleOption
        infix fun speed(v: Number): IParticleOption
    }

    private inner class ParticleOption : IParticleOption {
        override fun count(v: Int): IParticleOption {
            count = v
            return this
        }

        override fun speed(v: Number): IParticleOption {
            speed = v.toFloat()
            return this
        }
    }

    sealed interface IParticleOffsetOption {
        infix fun x(x: Number): IParticleOffsetOption
        infix fun y(y: Number): IParticleOffsetOption
        infix fun z(z: Number): IParticleOffsetOption
        infix fun all(v: Number)
    }

    private inner class ParticleOffsetOption : IParticleOffsetOption {
        override fun x(x: Number): IParticleOffsetOption {
            _offset.x = x.toDouble()
            return this
        }

        override fun y(y: Number): IParticleOffsetOption {
            _offset.y = y.toDouble()
            return this
        }

        override fun z(z: Number): IParticleOffsetOption {
            _offset.z = z.toDouble()
            return this
        }

        override fun all(v: Number) {
            _offset.x = v.toDouble()
            _offset.y = v.toDouble()
            _offset.z = v.toDouble()
        }
    }

    internal fun build(): ParticleEffect {
        if(particleEnum == null) throw IllegalStateException("Particle Enum is not set or is not found.")
        return ParticleEffect.of(particleEnum!!)
            .setLocation(location)
            .setOffset(_offset)
            .longDistance(_longDistance)
            .setParticleCount(count)
            .setParticleData(speed)
            .apply {
                if(data != null) setData(*data!!)
            }
            .build()
    }

}
