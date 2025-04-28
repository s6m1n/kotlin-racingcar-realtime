package model

data class Car(
    val name: String,
    var position: Int = 0,
) {
    fun move() {
        position += 1
    }

    fun isArrived(finishPosition: Int) = finishPosition <= position
}
