package model

import kotlinx.coroutines.delay

data class Car(
    val name: String,
    var position: Int = 0,
) {
    suspend fun move() {
        delay((1000L..2000L).random())
        position += 1
    }

    fun isArrived(goal: Int) = goal <= position
}
