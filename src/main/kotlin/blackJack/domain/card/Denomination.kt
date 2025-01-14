package blackJack.domain.card

import blackJack.domain.card.Signal.Companion.MAX_NUMBER

enum class Denomination(val value: String, val number: Int) {
    ACE("A", 1),
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("J", 10),
    QUEEN("Q", 10),
    KING("K", 10);

    companion object {
        const val ACE_BONUS_NUMBER = 10
    }
}
