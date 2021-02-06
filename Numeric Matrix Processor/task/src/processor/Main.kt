package processor


fun menu(): String {
    var choice = ""
    while (choice !in arrayOf("1", "2", "3", "0")) {
        println("1. Add matrices")
        println("2. Multiply matrix by a constant")
        println("3. Multiply matrices")
        println("0. Exit")

        print("Your choice: > ")
        choice = readLine()!!
    }
    return choice
}

fun addMatrices() {
    val m1 = Matrix.readMat("first")
    val m2 = Matrix.readMat("second")

    try {
        val m3 = m1 + m2
        println("The result is:")
        println(m3)
    } catch (e: Exception) {
        println(e.message)
    }
}

fun matrixScalarMultiplication() {
    val m1 = Matrix.readMat()
    print("Enter constant: > ")
    val c = readLine()!!.toDouble()

    println("The result is:")
    println(c * m1)
//    println("\n")
//    println(m1 * c)
}

fun multiplyMatrices() {
    val m1 = Matrix.readMat("first")
    val m2 = Matrix.readMat("second")

    try {
        val m3 = m1 * m2
        println("The result is:")
        println(m3)
    } catch (e: Exception) {
        println(e.message)
    }
}

fun main() {

    while (true) {
        when (menu()) {
            "1" -> addMatrices()
            "2" -> matrixScalarMultiplication()
            "3" -> multiplyMatrices()
            "0" -> break
        }
    }
}
