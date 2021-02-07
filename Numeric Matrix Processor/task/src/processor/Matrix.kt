package processor

import kotlin.math.pow

open class Matrix(private val rows: Int = 1, private val columns: Int = 1) {
    protected val mat = Array(rows) { DoubleArray(columns) }

    constructor(arr: Array<DoubleArray>) : this(arr.size, arr[0].size) {
        for (r in 0 until rows)
            for (c in 0 until columns)
                mat[r][c] = arr[r][c]
    }

    constructor(arr: MutableList<MutableList<Double>>) : this(arr.size, arr[0].size) {
        for (r in 0 until rows)
            for (c in 0 until columns)
                mat[r][c] = arr[r][c]
    }

    fun rows(): Int {
        return this.rows
    }

    fun columns(): Int {
        return this.columns
    }

    operator fun get(r: Int, c: Int): Double {
        return mat[r][c]
    }

    operator fun set(r: Int, c: Int, v: Double) {
        mat[r][c] = v
    }

    fun row(n: Int): RowVector {
        return RowVector(mat[n])
    }

    fun column(m: Int): ColumnVector {
        val v = Array(rows) { DoubleArray(1) { 0.0 } }
        for (r in 0 until rows)
            for (c in 0 until 1)
                v[r][c] = mat[r][m]
        return ColumnVector(v)
    }

    operator fun plus(other: Matrix): Matrix {
        if (rows != other.rows() && columns != other.columns())
            throw IncompatibleDimensionsForMatrixOperationException(
                "The operation cannot be performed."
            )

        val m = Matrix(rows, columns)
        for (r in 0 until rows)
            for (c in 0 until columns) {
                val v = mat[r][c] + other[r, c]
                m[r, c] = v
            }

        return m
    }

    operator fun times(constant: Double): Matrix {
        val m = Matrix(rows, columns)
        for (r in 0 until rows)
            for (c in 0 until columns)
                m[r, c] = constant * mat[r][c]
        return m
    }

    fun dotProduct(row: RowVector, col: ColumnVector): Double {
        var v = 0.0
        for (i in 0 until row.columns())
            v += row[0, i] * col[i, 0]
        return v
    }

    operator fun times(other: Matrix): Matrix {
        if (columns != other.rows)
            throw IncompatibleDimensionsForMatrixOperationException(
                "The operation cannot be performed."
            )

        val m = Matrix(rows, other.columns)
        for (r in 0 until m.rows)
            for (c in 0 until m.columns)
                m[r, c] = dotProduct(row(r), other.column(c))
        return m
    }

    fun transpose(): Matrix {
        val transpose = MutableList(columns) { MutableList(rows) { 0.0 } }
        for (r in 0 until rows)
            for (c in 0 until columns)
                transpose[c][r] = mat[r][c]
        return Matrix(transpose)
    }

    fun columnToRowReversed(c: Int): RowVector {
        val col = column(c)
        val n = col.rows()
        val row = RowVector(n)
        for (r in 0 until n)
            row[0, (n - r - 1)] = col[r, 0]
        return row
    }

    private fun addRow(row: RowVector, idx: Int) {
        for (c in 0 until columns())
            this[idx, c] = row[0, c]
    }

    fun addColumn(col: ColumnVector, colIdx: Int) {
        for (r in 0 until rows())
            this[r, colIdx] = col[r, 0]
    }

    fun secondaryTranspose(): Matrix {
        val m = Matrix(columns(), rows())
        var idx = 0
        for (c in columns() - 1 downTo 0) {
            val row = columnToRowReversed(c)
            m.addRow(row, idx++)
        }
        return m
    }

    fun verticalLineTranspose(): Matrix {
        val m = Matrix(rows(), columns())
        var idx = 0
        for (c in columns() - 1 downTo 0) {
            val col = column(c)
            m.addColumn(col, idx++)
        }
        return m
    }

    fun horizontalLineTranspose(): Matrix {
        val m = Matrix(rows(), columns())
        var top = 0
        var bottom = rows - 1
        while (top < bottom) {
            for (c in 0 until columns()) {
                m[top, c] = this[bottom, c]
                m[bottom, c] = this[top, c]
            }
            top++
            bottom--
        }
        return m
    }

    fun det(m: Matrix): Double {

        if (m.rows() == 1 && m.columns() == 1)
            return m[0,0]
        if (m.rows() == 2 && m.columns() == 2)
            return m[0,0] * m[1,1] - m[0,1] * m[1,0]

        var det = 0.0
        for (c in 0 until m.columns()) {
            det += m[0, c] * cofactor(m,0 + 1, c + 1)
        }
        return det
    }

    fun cofactor(m: Matrix, r: Int, c: Int): Double {
        return (-1.0).pow(r + c) * det(minor(m, r, c))
    }

    fun minor(m: Matrix, r: Int, c: Int): Matrix {
        val newM = Matrix(m.rows() - 1, m.columns() - 1)
        var mi = 0
        var mj = 0
        for (i in 0 until newM.rows()) {
            for (j in 0 until newM.columns()) {
                if (mi == r - 1) mi++
                if (mj == c - 1) mj++
                newM[i, j] = m[mi, mj]
                mj++
            }
            mj = 0
            mi++
        }
        return newM
    }

    override fun toString(): String {
        var output = ""
        for (r in 0 until rows) {
            for (c in 0 until columns)
                output += "${mat[r][c]} "
            output += "\n"
        }

        return output
    }

    companion object {
        fun readMat(prompt: String = ""): Matrix {
            print("Enter size of $prompt matrix: > ")
            val (rows, cols) = readLine()!!.split(' ').map { it.toInt() }

            println("Enter $prompt matrix:")
            val m = Matrix(rows, cols)
            for (r in 0 until rows) {
                print("> ")
                val row = readLine()!!.split(' ').map { it.toDouble() }
                for (c in 0 until cols)
                    m[r, c] = row[c]
            }
            return m
        }
    }
}

class RowVector(m: Int) : Matrix(rows = 1, columns = m) {
    constructor(row: DoubleArray) : this(row.size) {
        for (r in 0 until this.rows())
            for (c in 0 until this.columns())
                this.mat[r][c] = row[c]
    }
}

class ColumnVector(n: Int) : Matrix(rows = n, columns = 1) {
    constructor(a: Array<DoubleArray>) : this(a.size) {
        for (r in 0 until this.rows())
            for (c in 0 until this.columns())
                this.mat[r][c] = a[r][c]
    }
}

class IncompatibleDimensionsForMatrixOperationException(override val message: String) : Throwable()

operator fun Int.times(m: Matrix): Matrix {
    return m * this.toDouble()
}

operator fun Double.times(m: Matrix): Matrix {
    return m * this
}
