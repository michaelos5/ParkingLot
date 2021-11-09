package parking

import kotlin.system.exitProcess

data class Car(val reg: String = "", val color: String = "")
val parkingSpots: MutableMap<Int,Car?> = mutableMapOf()
var CARPARK_SIZE = 0;

const val NO_SPOTS_AVAILABLE: Int = -1
const val CREATE_NOT_INIT: Int = -2
const val PARKING_LOT_EMPTY: Int = -3
const val NO_RESULT: Int = -4
const val PARK_CMD: String = "park"
const val LEAVE_CMD: String = "leave"
const val CREATE_CMD: String = "create"
const val STATUS_CMD: String = "status"
const val REG_BY_COLOR: String = "reg_by_color"
const val SPOT_BY_COLOR: String = "spot_by_color"
const val SPOT_BY_REG: String = "spot_by_reg"
const val EXIT_CMD: String = "exit"

fun main() {
    do {
        val command: List<String> = readLine()!!.split(' ')
        when(command[0]) {
            PARK_CMD -> {
                when(val spotParked: Int = parkCar(Car(command[1], command[2]))) {
                    NO_SPOTS_AVAILABLE -> println("Sorry, the parking lot is full.")
                    CREATE_NOT_INIT -> println("Sorry, a parking lot has not been created.")
                    else -> println("${parkingSpots[spotParked]?.color} car parked in spot $spotParked.")
                }
            }
            LEAVE_CMD -> {
                when(val leaveStatus: Int = leaveCar(command[1].toInt())) {
                    CREATE_NOT_INIT -> println("Sorry, a parking lot has not been created.")
                    else -> println("Spot $leaveStatus is free.")
                }
            }
            CREATE_CMD -> {
                CARPARK_SIZE = command[1].toInt()
                parkingSpots.clear()
                println("Created a parking lot with $CARPARK_SIZE spots.")
            }
            STATUS_CMD -> {
                when(val status: Int = getParkingLotStatus()) {
                    CREATE_NOT_INIT -> println("Sorry, a parking lot has not been created.")
                    PARKING_LOT_EMPTY -> println("Parking lot is empty.")
                    else -> printParkingSpotStatus()
                }
            }
            REG_BY_COLOR -> {
                when(val status: Int = getRegByColor(command[1])) {
                    CREATE_NOT_INIT -> println("Sorry, a parking lot has not been created.")
                    NO_RESULT -> println("No cars with color ${command[1]} were found.")
                }
            }
            SPOT_BY_COLOR -> {
                when(val status: Int = spotByColor(command[1])) {
                    CREATE_NOT_INIT -> println("Sorry, a parking lot has not been created.")
                    NO_RESULT -> println("No cars with color ${command[1]} were found.")
                }
            }
            SPOT_BY_REG -> {
                when(val status: Int = spotByReg(command[1])) {
                    CREATE_NOT_INIT -> println("Sorry, a parking lot has not been created.")
                    NO_RESULT -> println("No cars with registration number ${command[1]} were found.")
                }
            }
        }
    } while(command[0] != EXIT_CMD)
}

fun spotByReg(reg: String): Int {
    if (!isParkingLotCreated()) return CREATE_NOT_INIT
    val filteredCars = parkingSpots.filter {it.value?.reg.equals(reg, ignoreCase = true)}
    if (filteredCars.isEmpty()) return NO_RESULT
    var temp: String = ""
    for (item in filteredCars) temp += (item.key.toString() + ", ")
    println(temp.substring(0, temp.length - 2))
    return 0
}
fun spotByColor(color: String): Int {
    if (!isParkingLotCreated()) return CREATE_NOT_INIT
    val filteredCars = parkingSpots.filter {it.value?.color.equals(color, ignoreCase = true)}
    if (filteredCars.isEmpty()) return NO_RESULT
    var temp: String = ""
    for (item in filteredCars) temp += (item.key.toString() + ", ")
    println(temp.substring(0, temp.length - 2))
    return 0
}
fun getRegByColor(color: String): Int {
    if (!isParkingLotCreated()) return CREATE_NOT_INIT
    val filteredCars = parkingSpots.filter {it.value?.color.equals(color, ignoreCase = true)}
    if (filteredCars.isEmpty()) return NO_RESULT
    var temp: String = ""
    for (item in filteredCars) temp += (item.value?.reg + ", ")
    println(temp.substring(0, temp.length - 2))
    return 0
}

fun isParkingLotCreated(): Boolean {
    return CARPARK_SIZE != 0
}

fun isParkingSpotOccupied(carSpot: Int): Boolean {
    return (parkingSpots[carSpot] != null)
}

fun findClosestAvailableParkingSpot(): Int {
    for(i in 1..CARPARK_SIZE) {
        if(!isParkingSpotOccupied(i)) return i
    }
    return NO_SPOTS_AVAILABLE
}

// returns -1 if no 'space available'

fun parkCar(car: Car): Int {
    if(!isParkingLotCreated()) return CREATE_NOT_INIT
    val spot: Int = findClosestAvailableParkingSpot()
    if(spot != NO_SPOTS_AVAILABLE)  {
        parkingSpots[spot] = car
        return spot
    }
    return NO_SPOTS_AVAILABLE
}

// returns -2 if 'create' has not been called yet
fun leaveCar(carSpot: Int): Int {
    if(!isParkingLotCreated()) return CREATE_NOT_INIT
    parkingSpots.remove(carSpot)
    return carSpot
}

fun printParkingSpotStatus() {
    for(i in 1..CARPARK_SIZE) {
        if(isParkingSpotOccupied(i)) println("$i ${parkingSpots[i]?.reg} ${parkingSpots[i]?.color}")
    }
}

fun getParkingLotStatus(): Int {
    if(CARPARK_SIZE == 0 ) return CREATE_NOT_INIT
    if(parkingSpots.isEmpty()) return PARKING_LOT_EMPTY
    return 0
}
