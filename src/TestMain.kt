import java.util.*

fun main(args: Array<String>) {
    val size = d6(2) - 2
    val atm = d6(2) - 7 + size
    val temp = d6(2) + when (atm) {
        2, 3 -> -2
        4, 5, 14 -> -1
        8, 9 -> +1
        10, 13, 15 -> +2
        11, 12 -> +6
        else -> throw RuntimeException("This should never happen")
    }
    val hyd = when (size) {
        0 -> 0
        1 -> 0
        else -> throw RuntimeException("To be continued")
    }
    val pop = d6(2) - 2
    val gov = if (pop == 0) {
        0
    } else {
        d6(2) - 7 + pop
    }
    val fac = if (pop == 0) {
        0
    } else {
        roll(3) +
                when {
                    gov == 0 -> +1
                    gov == 7 -> +1
                    gov > 9 -> -1
                    else -> 0
                }
    }

}


fun d6(times: Int) = Array(times) { _ -> d6() }.sum()
fun d6() = roll(6)
fun roll(sides: Int): Int {
    val rand = Random()
    return rand.nextInt(sides) + 1
}
