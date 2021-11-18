package blackJack.domain

interface PlayerStatus {
    fun isPlayer(): Boolean
    fun getAbleReceivedCard(): Boolean
    fun isBlackJackPlayer(): Boolean
    fun isBustPlayer(): Boolean
    fun getScore(): Int
    fun noReceiveCard()
    fun receiveCard(card: Card, isContinue: Boolean = true)
}

class PlayerStatusImpl(private var _cards: Cards, private var _decisionStatus: PlayerDecision = Hit()) : PlayerStatus {

    val cards: Cards
        get() = _cards

    val decisionStatus: PlayerDecision
        get() = _decisionStatus

    override fun isPlayer(): Boolean = true

    override fun getAbleReceivedCard(): Boolean = _decisionStatus.isContinue()

    override fun isBlackJackPlayer(): Boolean = _decisionStatus is BlackJack

    override fun isBustPlayer(): Boolean = _decisionStatus is Bust

    override fun getScore() = _cards.sumCards()

    override fun noReceiveCard() {
        this._decisionStatus = PlayerDecision.changeDecision(_cards, false)
    }

    override fun receiveCard(card: Card, isContinue: Boolean) {
        if (isContinue) {
            this._cards += card
            this._decisionStatus = PlayerDecision.changeDecision(_cards)
        } else {
            noReceiveCard()
        }
    }

    companion object {
        fun of(): PlayerStatusImpl {
            return PlayerStatusImpl(Cards(listOf()), Hit())
        }
    }
}