package model

data class Car(
    val name: String,
    var position: Int = 0,
) {
    private var isMovable: Boolean = true

    fun move() {
        position += 1
    }

    fun changeMovable() {
        isMovable = !isMovable
    }

    fun isMovable() = isMovable

    fun isArrived(finishPosition: Int) = finishPosition <= position
}
