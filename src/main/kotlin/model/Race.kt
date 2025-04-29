package model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import java.util.concurrent.atomic.AtomicBoolean

class Race(
    private val _cars: MutableList<Car>,
    private val goal: Int,
    private val channel: Channel<Car>,
    parentScope: CoroutineScope,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    val cars: List<Car> get() = jobs.keys.toList()
    private var isMovable: AtomicBoolean = AtomicBoolean(true)
    private val scope = CoroutineScope(parentScope.coroutineContext[Job]!! + dispatcher)
    private val jobs: MutableMap<Car, Job> = mutableMapOf()

    fun changeMovable(): AtomicBoolean {
        isMovable = AtomicBoolean(!isMovable.get())
        return isMovable
    }

    fun start() {
        _cars.forEach {
            createRunJob(it)
        }
    }

    fun addCar(newCar: Car): Boolean =
        if (jobs[newCar] != null) {
            false
        } else {
            createRunJob(newCar)
            true
        }

    private fun createRunJob(car: Car) {
        jobs[car] = scope.launch { run(car) }
    }

    private suspend fun run(car: Car) =
        coroutineScope {
            while (isActive) {
                if (isMovable.get()) {
                    car.move(channel)
                    if (car.isArrived(goal)) {
                        println("${car.name}가 최종 우승했습니다.")
                        channel.close()
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
