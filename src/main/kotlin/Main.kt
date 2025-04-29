import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
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
    val channel = Channel<Car>()

    val cars = view.createCars()
    val goal = view.readGoal()
    view.showCars(cars)
    try {
        runBlocking {
            val race = Race(cars, goal, channel, this)
            race.start()
            observeRaceChannel(channel, view)
            readCommand(race, view)
        }
    } catch (e: CancellationException) {
        view.finishApplication()
    }
}

private fun CoroutineScope.readCommand(
    race: Race,
    view: View,
) {
    while (isActive) {
        val command = readln()
        if (isActive) {
            when {
                command == COMMAND_PAUSE_OR_RESTART -> handlePauseOrRestartCommand(race, view)
                command.take(COMMAND_ADD_LENGTH) == COMMAND_ADD -> handleNewCarCommand(command, race, view)
                else -> view.invalidInput()
            }
        }
    }
}

private fun CoroutineScope.observeRaceChannel(
    channel: Channel<Car>,
    view: View,
) {
    launch(Dispatchers.IO) {
        for (car in channel) {
            view.showCar(car)
        }
    }
}

private fun handlePauseOrRestartCommand(
    race: Race,
    view: View,
) {
    val isMovable = race.changeMovable()
    view.showPauseOrResume(isMovable.get())
}

private fun handleNewCarCommand(
    command: String,
    race: Race,
    view: View,
) {
    val newCar = Car(command.drop(COMMAND_ADD_LENGTH))
    race.addCar(newCar)
    view.showNewCar(newCar)
    view.showCars(race.cars)
}

/*
car1,car2,car3,car4
5

add 빙티
add 소민
 */
