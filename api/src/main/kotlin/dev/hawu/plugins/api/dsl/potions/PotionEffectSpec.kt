package dev.hawu.plugins.api.dsl.potions

import dev.hawu.plugins.api.TimeConversions
import dev.hawu.plugins.api.collections.MutableProperty
import dev.hawu.plugins.api.dsl.ScopeControlMarker
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * DSL for creating a potion effect intuitively.
 */
@ScopeControlMarker
class PotionEffectSpec internal constructor() {

    private var _ambient = false

    val effectType = MutableProperty.empty<PotionEffectType>()
    val duration = MutableProperty.of<Int>(20)
    val amplifier = MutableProperty.of<Int>(0)
    val particles = MutableProperty.of<Boolean>(true)

    /**
     * Hides the potion particles. Does the same thing
     * as `particles.set(false)`.
     */
    val hideParticles: Unit
        get() {
            particles.set(false)
        }

    /**
     * Toggles ambient to true.
     */
    val ambient: Unit
        get() {
            this._ambient = true
        }

    val swiftness: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.SPEED)
            return PotionEffectOption()
        }
    val speed by this::swiftness

    val slowness: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.SLOW)
            return PotionEffectOption()
        }
    val slow by this::slowness

    val haste: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.FAST_DIGGING)
            return PotionEffectOption()
        }
    val fastDigging by this::haste

    val miningFatigue: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.SLOW_DIGGING)
            return PotionEffectOption()
        }
    val slowDigging by this::miningFatigue

    val strength: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.INCREASE_DAMAGE)
            return PotionEffectOption()
        }
    val increaseDamage by this::strength

    val instantHealing: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.HEAL)
            return PotionEffectOption()
        }
    val heal by this::instantHealing

    val instantDamage: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.HARM)
            return PotionEffectOption()
        }
    val harm by this::instantDamage

    val jumpBoost: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.JUMP)
            return PotionEffectOption()
        }
    val jump by this::jumpBoost

    val nausea: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.CONFUSION)
            return PotionEffectOption()
        }
    val confusion by this::nausea

    val regeneration: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.REGENERATION)
            return PotionEffectOption()
        }

    val resistance: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.DAMAGE_RESISTANCE)
            return PotionEffectOption()
        }

    val fireResistance: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.FIRE_RESISTANCE)
            return PotionEffectOption()
        }

    val waterBreathing: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.WATER_BREATHING)
            return PotionEffectOption()
        }

    val invisibility: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.INVISIBILITY)
            return PotionEffectOption()
        }

    val blindness: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.BLINDNESS)
            return PotionEffectOption()
        }

    val nightVision: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.NIGHT_VISION)
            return PotionEffectOption()
        }

    val hunger: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.HUNGER)
            return PotionEffectOption()
        }

    val weakness: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.WEAKNESS)
            return PotionEffectOption()
        }

    val poison: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.POISON)
            return PotionEffectOption()
        }

    val wither: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.WITHER)
            return PotionEffectOption()
        }

    val healthBoost: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.HEALTH_BOOST)
            return PotionEffectOption()
        }

    val absorption: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.ABSORPTION)
            return PotionEffectOption()
        }

    val saturation: IPotionEffectOption
        get() {
            effectType.set(PotionEffectType.SATURATION)
            return PotionEffectOption()
        }

    internal fun build(): PotionEffect {
        return PotionEffect(effectType.get(), duration.get(), amplifier.get(), _ambient, particles.get())
    }

    /**
     * Option for chaining duration and amplifiers.
     */
    sealed interface IPotionEffectOption {
        /**
         * Sets the duration to a number of ticks.
         */
        infix fun lasting(value: Number): IPotionEffectOption

        /**
         * Sets the duration to a timestamp, this will be converted
         * to ticks.
         */
        infix fun lasting(value: String): IPotionEffectOption

        /**
         * Sets the amplifier for the potion.
         */
        infix fun amplifier(value: Int): IPotionEffectOption
    }

    private inner class PotionEffectOption : IPotionEffectOption {
        override fun lasting(value: Number): IPotionEffectOption {
            duration.set(value.toInt())
            return this
        }

        override fun lasting(value: String): IPotionEffectOption {
            duration.set((TimeConversions.convertToMillis(value).toLong() / 50).toInt())
            return this
        }

        override fun amplifier(value: Int): IPotionEffectOption {
            amplifier.set(value)
            return this
        }
    }

}
