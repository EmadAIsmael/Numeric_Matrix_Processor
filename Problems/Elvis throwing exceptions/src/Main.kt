fun main() {
    val line: String? = readLine()
    println("Elvis says: ${line ?: throw IllegalStateException()}")
}
