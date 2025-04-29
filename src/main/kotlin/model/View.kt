package model

class View {
    fun createCars(): MutableList<Car> {
        println("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)")
        val cars = readln().split(",").map { Car(it) }.toMutableList()
        return cars
    }

    fun readGoal(): Int {
        println("목표 거리를 입력하세요.")
        val finishPosition = readln().toInt()
        return finishPosition
    }

    fun showCar(car: Car) {
        println("${car.name} : ${"-".repeat(car.position)}")
    }

    fun showCars(cars: List<Car>) {
        println("자동차 목록 : ${cars.map { it.name }.joinToString()}")
    }

    fun showPauseOrResume(isMovable: Boolean) {
        println(if (isMovable) "경주를 다시 시작합니다." else "경주를 일시중지합니다.")
    }

    fun invalidInput() {
        println("입력 형식이 올바르지 않습니다.")
    }

    fun finishApplication() {
        println("프로그램을 종료합니다.")
    }

    fun showNewCar(newCar: Car) {
        println("새로운 자동차 [${newCar.name}] 추가")
    }

    companion object {
        const val COMMAND_ADD_LENGTH = 4
        const val COMMAND_ADD = "add "
        const val COMMAND_PAUSE_OR_RESTART = ""
    }
}
