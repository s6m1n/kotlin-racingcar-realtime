import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import model.Car
import model.View
import kotlin.coroutines.cancellation.CancellationException

fun main() {
    val view = View()
    try {
        runBlocking {
            val cars = view.createCars()
            val goalPosition = view.readGoalPosition()

            view.showCars(cars)
            val carJobs = cars.map { start(it, goalPosition) }.toMutableList()

            while (isActive) {
                val command = readln()
                if (command == "") {
                    changeCarsMovable(cars)
                    view.showPauseOrResume()
                } else if (command.take(4) == "add ") {
                    val newCar = Car(command.drop(4))
                    cars.add(newCar)
                    carJobs.add(start(newCar, goalPosition))
                    view.showNewCar(newCar)
                    view.showCars(cars)
                } else {
                    view.invalidInput()
                }
            }
        }
    } catch (e: CancellationException) {
        println("\n프로그램이 종료되었습니다.")
    }
}

private fun changeCarsMovable(cars: List<Car>) {
    cars.forEach { it.changeMovable() }
}

private fun CoroutineScope.start(
    car: Car,
    finishPosition: Int,
) = launch(Dispatchers.IO) {
    while (isActive) {
        if (car.isMovable()) {
            delay((1000L..5000L).random())
            car.move()
            println("${car.name} : ${"-".repeat(car.position)}")
            if (car.isArrived(finishPosition)) {
                println("${car.name}가 최종 우승했습니다.")
                this@start.cancel()
                break
            }
        } else {
            yield()
        }
    }
    this.cancel()
}

/*
car1,car2,car3,car4
10

add 빙티
add 소민
 */
