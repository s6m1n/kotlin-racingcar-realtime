package model

class View {
    fun createCars(): MutableList<Car> {
        println("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)")
        val cars = readln().split(",").map { Car(it) }.toMutableList()
        return cars
    }

    fun readGoalPosition(): Int {
        println("목표 거리를 입력하세요.")
        val finishPosition = readln().toInt()
        return finishPosition
    }

    fun showCars(cars: MutableList<Car>) {
        println("자동차 목록 : ${cars.map { it.name }.joinToString()}")
    }

    fun showPauseOrResume() {
        println("엔터가 입력되었습니다.")
    }

    fun invalidInput() {
        println("입력 형식이 올바르지 않습니다.")
    }

    fun showNewCar(newCar: Car) {
        println("${newCar.name}을 새로 만들었어요.")
    }
}
