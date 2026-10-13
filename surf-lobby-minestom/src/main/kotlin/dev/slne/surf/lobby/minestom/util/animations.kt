package dev.slne.surf.lobby.minestom.util

import dev.slne.surf.api.minestom.extension.editEntityMeta
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityPose
import net.minestom.server.entity.metadata.LivingEntityMeta
import net.minestom.server.timer.TaskSchedule

/**
 * Plays the riptide spin animation on this entity for [durationTicks] ticks.
 */
fun Entity.playSpinAttackAnimation(durationTicks: Int = 20) {
    if (entityMeta !is LivingEntityMeta) return

    editEntityMeta<LivingEntityMeta> { meta ->
        meta.isInRiptideSpinAttack = true
        pose = EntityPose.SPIN_ATTACK
    }

    scheduler().buildTask {
        editEntityMeta<LivingEntityMeta> { meta ->
            meta.isInRiptideSpinAttack = false

            if (pose == EntityPose.SPIN_ATTACK) {
                pose = EntityPose.STANDING
            }
        }
    }.delay(TaskSchedule.tick(durationTicks)).schedule()
}
