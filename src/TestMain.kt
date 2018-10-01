import java.lang.Integer.max
import java.lang.Integer.min
import java.util.*

fun main(args: Array<String>) {
    fun Int.orZero() = max(this, 0)
    fun Int.orMax(i: Int) = min(this, i)

    fun Int.normalize(min: Int = 0, max: Int): Int {
        return min(max, max(min, this))
    }

    val size = (d6(2) - 2).orMax(10)
    val atm = (d6(2) - 7 + size).orZero().orMax(15)
    val temp = (d6(2) + when (atm) {
        2, 3 -> -2
        4, 5, 14 -> -1
        6, 7, 0 -> 0
        8, 9 -> +1
        10, 13, 15 -> +2
        11, 12 -> +6
        else -> throw RuntimeException("This should never happen")
    }).orZero().orMax(12)
    val hyd = when (size) {
        0 -> 0
        1 -> 0
        else -> {
            d6(2) - 7 + size + temp + when (atm) {
                0, 1, 10, 11, 12 -> -4
                else -> 0
            } + when {
                temp == 10 -> -2
                temp == 11 -> -2
                temp > 11 -> -6
                else -> 0
            }
        }
    }.orZero().orMax(10)

    val pop = (d6(2) - 2).orMax(10)
    val gov = if (pop == 0) {
        0
    } else {
        d6(2) - 7 + pop
    }.orZero().orMax(13)
    val numberOfFactions = if (pop == 0) {
        0
    } else {
        roll(3) +
                when {
                    gov == 0 -> +1
                    gov == 7 -> +1
                    gov > 9 -> -1
                    else -> 0
                }
    }.orMax(4)
    val fac = Array(numberOfFactions) { d6(2) }
    val law = (d6(2) - 7 + gov).orZero().orMax(9)
    val sta = d6(2)


    // Done with the easy stuff, lets print it out
    println("""
        size: $size
        atm: $atm
        temp: $temp
        hyd: $hyd
        pop: $pop
        gov: $gov
        number of factions: $numberOfFactions
        fac: [${fac.joinToString()}]
        law: $law
        sta: $sta
    """.trimIndent())


    // Okay lets continue
    val staClass = when (sta) {
        0, 1, 2 -> "x"
        3, 4 -> "E"
        5, 6 -> "D"
        7, 8 -> "C"
        9, 10 -> "B"
        11, 12 -> "A"
        else -> throw RuntimeException("are you okay dude?")
    }

    class Base(val staClass: String,
               val navel: BaseItem = BaseItem.NOT_POSSIBLE,
               val scout: BaseItem = BaseItem.NOT_POSSIBLE,
               val research: BaseItem = BaseItem.NOT_POSSIBLE,
               val tas: BaseItem = BaseItem.NOT_POSSIBLE,
               val imperialConsulate: BaseItem =
                       BaseItem.NOT_POSSIBLE,
               val pirate: BaseItem = BaseItem.NOT_POSSIBLE) {
        override fun toString(): String {
            return if (staClass == "X") {
                "DOES_NOT_EXIST"
            } else {
                """
                    staClass: ${this.staClass}
                    Navel: $navel
                    Scout: $scout
                    Research: $research
                    TAS: $tas
                    Imperial Consulate: $imperialConsulate
                    Pirate: $pirate
                    """.trimIndent()
            }
        }
    }

    fun itemIf(test: () -> Boolean): BaseItem {
        return if (test()) {
            BaseItem.YES
        } else {
            BaseItem.NO
        }
    }

    val base = Base(staClass,
            navel = when (staClass) {
                "A", "B" -> itemIf { d6(2) > 7 }
                else -> BaseItem.NOT_POSSIBLE
            },
            scout = when (staClass) {
                "A" -> itemIf { d6(2) > 9 }
                "B", "C" -> itemIf { d6(2) > 7 }
                "D" -> itemIf { d6(2) > 6 }
                else -> BaseItem.NOT_POSSIBLE
            },
            research = when (staClass) {
                "A" -> itemIf { d6(2) > 7 }
                "B", "C" -> itemIf { d6(2) > 9 }
                else -> BaseItem.NOT_POSSIBLE
            },
            tas = when (staClass) {
                "A" -> itemIf { d6(2) > 3 }
                "B" -> itemIf { d6(2) > 5 }
                "C" -> itemIf { d6(2) > 9 }
                else -> BaseItem.NOT_POSSIBLE
            },
            imperialConsulate = when (staClass) {
                "A" -> itemIf { d6(2) > 5 }
                "B" -> itemIf { d6(2) > 7 }
                "C" -> itemIf { d6(2) > 9 }
                else -> BaseItem.NOT_POSSIBLE
            },
            pirate = when (staClass) {
                "B", "D", "E" -> itemIf { d6(2) > 11 }
                "C" -> itemIf { d6(2) > 9 }
                else -> BaseItem.NOT_POSSIBLE
            })
    println("Base:\n$base")
    val tl = 5
}

enum class BaseItem { YES, NO, NOT_POSSIBLE }


fun d6(times: Int) = Array(times) { _ -> d6() }.sum()
fun d6() = roll(6)
fun roll(sides: Int): Int {
    val rand = Random()
    return rand.nextInt(sides) + 1
}
