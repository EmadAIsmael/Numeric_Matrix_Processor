package processor

import java.util.Scanner


fun menu(scanner: Scanner): Int {
    var choice = 1000
    while (choice !in arrayOf(1, 2, 3, 4, 5, 0)) {
        println("1. Add matrices")
        println("2. Multiply matrix by a constant")
        println("3. Multiply matrices")
        println("4. Transpose matrix")
        println("5. Calculate a determinant")
        println("0. Exit")

        print("Your choice: > ")
        choice = scanner.nextInt()
    }
    choice = when (choice) {
        4 -> transpositionMenu(scanner)
        5 -> choice + 3
        else -> choice
    }
    return choice
}

fun transpositionMenu(scanner: Scanner): Int {
    var choice = 100
    while (choice !in arrayOf(1, 2, 3, 4)) {
        println(
            """
            1. Main diagonal
            2. Side diagonal
            3. Vertical line
            4. Horizontal line
        """.trimIndent()
        )
        print("Your choice: > ")
        choice = scanner.nextInt()
    }
    return choice + 3
}

fun addMatrices() {
    val m1 = Matrix.readMat("first")
    val m2 = Matrix.readMat("second")

    try {
        val m3 = m1 + m2
        println("The result is:")
        println(m3)
    } catch (e: IncompatibleDimensionsForMatrixOperationException) {
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
    } catch (e: IncompatibleDimensionsForMatrixOperationException) {
        println(e.message)
    }
}

fun mainDiagonalTransposition() {
    val m1 = Matrix.readMat()
    val m2 = m1.transpose()
    println("The result is:")
    println(m2)
}

fun sideDiagonalTransposition() {
    val m1 = Matrix.readMat()
    val m2 = m1.secondaryTranspose()
    println("The result is:")
    println(m2)
}

fun verticalLineTransposition() {
    val m1 = Matrix.readMat()
    val m2 = m1.verticalLineTranspose()
    println("The result is:")
    println(m2)
}

fun horizontalLineTransposition() {
    val m1 = Matrix.readMat()
    val m2 = m1.horizontalLineTranspose()
    println("The result is:")
    println(m2)
}

fun calculateDeterminant() {
    val m1 = Matrix.readMat()
    val m2 = m1.det(m1)
    println("The result is:")
    println(m2)
}

fun main() {
    val scanner = Scanner(System.`in`)
    while (true) {
        when (menu(scanner)) {
            1 -> addMatrices()
            2 -> matrixScalarMultiplication()
            3 -> multiplyMatrices()
            4 -> mainDiagonalTransposition()
            5 -> sideDiagonalTransposition()
            6 -> verticalLineTransposition()
            7 -> horizontalLineTransposition()
            8 -> calculateDeterminant()
            0 -> break
        }
    }
}
