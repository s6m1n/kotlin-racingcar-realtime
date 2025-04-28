import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.Car
import kotlin.coroutines.cancellation.CancellationException

/*
car1,car2,car3,car4
5
 */

fun main() {
    try {
        runBlocking {
            println("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)")
            val cars = readln().split(",").map { Car(it) }

            println("목표 거리를 입력하세요.")
            val finishPosition = readln().toInt()

            cars.map { car ->
                launch {
                    while (isActive) {
                        delay((0..500L).random())
                        car.move()
                        println("${car.name} : ${"-".repeat(car.position)}")
                        if (car.isArrived(finishPosition)) {
                            println("${car.name}가 최종 우승했습니다.")
                            break
                        }
                    }
                    this@runBlocking.cancel()
                }
            }
        }
    } catch (e: CancellationException) {
        println("\n프로그램이 종료되었습니다.")
    }
}
