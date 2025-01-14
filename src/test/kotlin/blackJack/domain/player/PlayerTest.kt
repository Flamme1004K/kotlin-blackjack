package blackJack.domain.player

import blackJack.domain.card.Card
import blackJack.domain.card.Denomination
import blackJack.domain.card.Suit
import blackJack.domain.card.Hit
import blackJack.domain.card.Stay
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class PlayerTest {

    @Test
    fun `플레이어는 이름이 지정되어 있고, 카드를 가지고 있다`() {
        // given
        val player = Player.of("김형준")

        // when
        player.receiveCard() {
            Card(Suit.HEARTS, Denomination.ACE)
        }
        player.receiveCard() {
            Card(Suit.SPADES, Denomination.KING)
        }

        // then
        assertAll({
            assertThat(player.name).isEqualTo("김형준")
            assertThat(player.status.toCards().size).isEqualTo(2)
        })
    }

    @Test
    fun `플레이어가 중복된 카드를 받는다면 에러`() {
        // given
        val player = Player.of("김형준")

        // when
        val actual = runCatching {
            player.receiveCard() {
                Card(Suit.HEARTS, Denomination.ACE)
            }
            player.receiveCard() {
                Card(Suit.SPADES, Denomination.KING)
            }
            player.receiveCard() {
                Card(Suit.SPADES, Denomination.KING)
            }
        }.exceptionOrNull()

        // then
        assertThat(actual).hasMessageContaining("중복 된 카드가 있습니다.")
    }

    @Test
    fun `플레이어의 현재 점수는 21점이다`() {
        // given
        val player = Player.of("김형준")
        player.receiveCard() {
            Card(Suit.HEARTS, Denomination.ACE)
        }
        player.receiveCard() {
            Card(Suit.SPADES, Denomination.KING)
        }

        // when
        val currentScore = player.getScore()

        // then
        assertThat(currentScore).isEqualTo(21)
    }

    @Test
    fun `플레이어의 현재 점수는 11점이면 21이하이기 때문에 카드를 더 받을 수 있다 해당 상태는 히트이다`() {
        // given
        val player = Player.of("김형준")
        player.receiveCard() {
            Card(Suit.HEARTS, Denomination.KING)
        }
        player.receiveCard() {
            Card(Suit.SPADES, Denomination.KING)
        }

        // when
        val decisionStatus = player.status.toStrategy()
        val isContinue = player.getAbleReceivedCard()

        // then
        assertAll({
            assertThat(decisionStatus is Hit).isEqualTo(true)
            assertThat(isContinue).isEqualTo(true)
        })
    }

    @Test
    fun `플레이어의 현재 점수는 11점이면 21점이하면 플레이어는 카드를 안받을 수 있다 안받는 다면 상태는 스테이이다`() {
        // given
        val player = Player.of("김형준")
        player.receiveCard() {
            Card(Suit.HEARTS, Denomination.ACE)
        }
        player.receiveCard() {
            Card(Suit.SPADES, Denomination.KING)
        }

        // when
        player.noReceiveCard()
        val decisionStatus = player.status.toStrategy()
        val isContinue = player.getAbleReceivedCard()

        // then
        assertAll({
            assertThat(decisionStatus is Stay).isEqualTo(true)
            assertThat(isContinue).isEqualTo(false)
        })
    }

    @Test
    fun `플레이어의 현재 점수가 21점이면 플레이어의 상태는 블랙잭이다`() {
        // given
        val player = Player.of("김형준")
        player.receiveCard() {
            Card(Suit.HEARTS, Denomination.ACE)
        }
        player.receiveCard() {
            Card(Suit.SPADES, Denomination.KING)
        }

        // when
        val isContinue = player.getAbleReceivedCard()
        val isBlackJack = player.isBlackJackPlayer()

        // then
        assertAll({
            assertThat(isBlackJack).isEqualTo(true)
            assertThat(isContinue).isEqualTo(false)
        })
    }

    @Test
    fun `플레이어가 ACE와 KING을 가지고 있을때, ACE는 11로 적용되서 합은 21이다`() {
        // given
        val player = Player.of("김형준")
        player.receiveCard() {
            Card(Suit.HEARTS, Denomination.ACE)
        }
        player.receiveCard() {
            Card(Suit.SPADES, Denomination.KING)
        }

        // when
        val score = player.status.getScore()

        // then
        assertThat(score).isEqualTo(21)
    }

    @Test
    fun `플레이어가 ACE 두개와 KING을 가지고 있을때, ACE는 1로 적용되서 합은 22이다`() {
        // given
        val player = Player.of("김형준")
        player.receiveCard() {
            Card(Suit.HEARTS, Denomination.ACE)
        }
        player.receiveCard() {
            Card(Suit.SPADES, Denomination.ACE)
        }
        player.receiveCard() {
            Card(Suit.SPADES, Denomination.KING)
        }

        // when
        val score = player.status.getScore()

        // then
        assertThat(score).isEqualTo(12)
    }

    @Test
    fun `플레이어가 ACE와 KING, QUEEN, NINE 가지고 있을때, ACE는 1로 적용되서 합은 22이다`() {
        // given
        val player = Player.of("김형준")
        player.receiveCard() {
            Card(Suit.HEARTS, Denomination.ACE)
        }
        player.receiveCard() {
            Card(Suit.SPADES, Denomination.KING)
        }
        player.receiveCard() {
            Card(Suit.SPADES, Denomination.QUEEN)
        }
        player.receiveCard() {
            Card(Suit.SPADES, Denomination.NINE)
        }

        // when
        val score = player.status.getScore()

        // then
        assertThat(score).isEqualTo(30)
    }

    @Test
    fun `플레이어의 현재 점수는 22점이면 21이상이기 때문에 카드를 더 받을 수 있다 해당 상태는 버스트이다`() {
        // given
        val player = Player.of("김형준")
        player.receiveCard() {
            Card(Suit.HEARTS, Denomination.TWO)
        }
        player.receiveCard() {
            Card(Suit.SPADES, Denomination.KING)
        }
        player.receiveCard() {
            Card(Suit.DIAMONDS, Denomination.KING)
        }

        // when
        val isBust = player.isBustPlayer()
        val isContinue = player.getAbleReceivedCard()

        // then
        assertAll({
            assertThat(isBust).isEqualTo(true)
            assertThat(isContinue).isEqualTo(false)
        })
    }

    @Test
    fun `플레이어는 돈을 베팅할 수 있다`() {
        // given
        val player = Player.of("Flamme")

        // when
        player.bet(10000)

        // then
        assertThat(player.bettingMoney.money).isEqualTo(10000)
    }

    @Test
    fun `플레이어의 베팅하기 전 베팅머니는 0이다`() {
        // given
        val player = Player.of("Flamme")

        // then
        assertThat(player.bettingMoney.money).isEqualTo(0)
    }
}
