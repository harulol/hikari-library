package dev.hawu.plugins.api.dsl.misc

import dev.hawu.plugins.api.dsl.particles.ParticleEffectSpec
import dev.hawu.plugins.api.particles.ParticleEffect
import dev.hawu.plugins.api.particles.ParticlePacketAdapter
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * Configures and creates a particle effect via a DSL.
 */
fun particleEffect(spec: ParticleEffectSpec.() -> Unit) = ParticleEffectSpec().apply(spec).build()

/**
 * Sends a particle effect to the [player].
 */
fun ParticleEffect.sendPlayer(player: Player) = ParticlePacketAdapter.getAdapter().send(player, this)

/**
 * Sends a particle effect to all online players.
 */
fun ParticleEffect.sendAll() = Bukkit.getOnlinePlayers().forEach { sendPlayer(it) }

/**
 * Sends a particle [effect] to [this] player.
 */
fun Player.sendParticle(effect: ParticleEffect) = effect.sendPlayer(this)
