package model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import java.util.concurrent.atomic.AtomicBoolean

class Race(
    cars: MutableList<Car>,
    private val goal: Int,
    private val channel: Channel<Car>,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    private var isMovable: AtomicBoolean = AtomicBoolean(true)
    private val scope = CoroutineScope(dispatcher + SupervisorJob())
    private val jobs: MutableMap<Car, Job> = mutableMapOf()
    val cars: List<Car> get() = jobs.keys.toList()

    init {
        cars.forEach {
            jobs[it] = scope.launch { start(it) }
        }
    }

    fun addCar(newCar: Car): Boolean =
        if (jobs[newCar] != null) {
            false
        } else {
            jobs[newCar] = scope.launch { start(newCar) }
            true
        }

    fun changeMovable(): AtomicBoolean {
        isMovable = AtomicBoolean(!isMovable.get())
        return isMovable
    }

    private fun start(car: Car) =
        scope.launch(Dispatchers.IO) {
            while (isActive) {
                if (isMovable.get()) {
                    car.move(channel)
                    if (car.isArrived(goal)) {
                        println("${car.name}가 최종 우승했습니다.")
                        scope.cancel()
                        break
                    }
                } else {
                    yield()
                }
            }
            this.cancel()
        }
}
