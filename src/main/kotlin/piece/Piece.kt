package piece

import board.Move
import player.PlayerType
import java.awt.Image
import javax.swing.ImageIcon

class Piece(val type: PieceType, val owner: PlayerType) {
    val displayName: String = type.name
    val image: ImageIcon = ImageIcon("rsc/${displayName.lowercase()}_${owner.displayName.lowercase()}.png")
    var pawnCanMoveTwo: Boolean = true
}
