package model

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay

data class Car(
    val name: String,
    var position: Int = 0,
) {
    suspend fun move(channel: Channel<Car>) {
        delay((1000L..2000L).random())
        position += 1
        channel.send(this)
    }

    fun isArrived(goal: Int) = goal <= position
}
