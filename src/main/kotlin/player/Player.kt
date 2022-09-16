package player

class Player(val type: PlayerType) {

    override fun toString(): String {
        return type.displayName
    }

}