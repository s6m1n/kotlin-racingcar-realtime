import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.Car
import model.Race
import model.View
import model.View.Companion.COMMAND_ADD
import model.View.Companion.COMMAND_ADD_LENGTH
import model.View.Companion.COMMAND_PAUSE_OR_RESTART

fun main() {
    val view = View()
    val cars = view.createCars()
    val goalPosition = view.readGoalPosition()

    runBlocking {
        val channel = Channel<Car>()
        view.showCars(cars)
        val race = Race(cars, goalPosition, channel)
        launch {
            for (car in channel) {
                println("${car.name} : ${"-".repeat(car.position)}")
            }
        }
        launch(Dispatchers.IO) {
            while (isActive) {
                val command = readln()
                if (command == COMMAND_PAUSE_OR_RESTART) {
                    val isMovable = race.changeMovable()
                    view.showPauseOrResume(isMovable.get())
                } else if (command.take(COMMAND_ADD_LENGTH) == COMMAND_ADD) {
                    val newCar = Car(command.drop(COMMAND_ADD_LENGTH))
                    race.addCar(newCar)
                    view.showNewCar(newCar)
                    view.showCars(race.cars)
                } else {
                    view.invalidInput()
                }
            }
        }
    }
}

/*
car1,car2,car3,car4
5

add 빙티
add 소민
 */
