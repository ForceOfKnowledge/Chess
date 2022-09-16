import board.Board
import board.Field
import board.Move
import piece.Piece
import piece.PieceType
import player.Player
import player.PlayerType
import java.awt.Color
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

class Game: Runnable, MouseListener {

    val board: Board = Board(this)
    val black: Player = Player(PlayerType.BLACK)
    val white: Player = Player(PlayerType.WHITE)
    var pieceSetterMode: Boolean = false
    var pieceSetterType: PieceType? = null
    var pieceSetterColor: PlayerType = PlayerType.BLACK
    var activePlayer: Player = white
    var pieceMover: Field? = null
    var gameActive: Boolean = true

    override fun run() {
        onGameStart()
    }

    private fun onGameStart() {
        board.printBoard()

        board.reset(this)
        board.isVisible = true

    }

    //Lässt das Programm abstürzen wenn keine Figur auf dem Übergebenen Feld ist
    fun getPossibleMovesForPieceOn(field: Field): Array<Move> {
        var moves: MutableList<Move> = mutableListOf()

        if(!field.isTaken()){
            println("Debug: Invalid Peace on this Field")
            return arrayOf()
        }

        when(field.piece!!.type){

            PieceType.KING -> {
                for(i in field.getAdjacentFields()){
                    moves.add(Move(field.id, i.id))
                }
            }
            PieceType.QUEEN -> {
                for(i in field.getDiagonals() + field.getStraights()){
                    moves.add(Move(field.id, i.id))
                }
            }
            PieceType.ROOK -> {
                for(i in field.getStraights()){
                    moves.add(Move(field.id, i.id))
                }
            }
            PieceType.BISHOP -> {
                for(i in field.getDiagonals()){
                    moves.add(Move(field.id, i.id))
                }
            }
            PieceType.KNIGHT -> {
                var numbers1: Array<Int> = arrayOf(1, -1)
                var numbers2: Array<Int> = arrayOf(2, -2)
                var knightTargetIds: MutableList<String> = mutableListOf()

                for(i in numbers1){
                    for(j in numbers2){
                        knightTargetIds.add((field.letter+i).toString() + (field.number+j).toString())
                        knightTargetIds.add((field.letter+j).toString() + (field.number+i).toString())
                    }
                }

                for(i in knightTargetIds){
                    var f: Field = board.getFieldById(i) ?: continue
                    moves.add(Move(field.id, f.id))
                }
            }
            PieceType.PAWN -> {
                if(field.piece!!.owner == PlayerType.WHITE){
                    var id: String = (field.letter).toString() + (field.number-1).toString()
                    if(board.getFieldById(id) != null){
                        if(!board.getFieldById(id)!!.isTaken()){
                            moves.add(Move(field.id, id))
                        }

                        var leftFieldId: String = (id[0]-1).toString() + id[1]
                        var rightFieldId: String = (id[0]+1).toString() + id[1]
                        if(board.getFieldById(leftFieldId) != null){
                            var leftField: Field = board.getFieldById(leftFieldId)!!
                            if(leftField.isTaken()){
                                moves.add(Move(field.id, leftField.id))
                            }

                        }
                        if(board.getFieldById(rightFieldId) != null){
                            var rightField: Field = board.getFieldById(rightFieldId)!!
                            if(rightField.isTaken()){
                                moves.add(Move(field.id, rightField.id))
                            }

                        }
                        if(field.piece!!.pawnCanMoveTwo){
                            var straightTwoField: Field = board.getFieldById((field.letter).toString() + (field.number-2).toString())!!
                            if(!straightTwoField.isTaken()){
                                moves.add(Move(field.id, straightTwoField.id))
                            }
                        }
                    }
                }else{
                    var id: String = (field.letter).toString() + (field.number+1).toString()
                    if(board.getFieldById(id) != null){
                        if(!board.getFieldById(id)!!.isTaken()){
                            moves.add(Move(field.id, id))
                        }

                        var leftFieldId: String = (id[0]-1).toString() + id[1]
                        var rightFieldId: String = (id[0]+1).toString() + id[1]
                        if(board.getFieldById(leftFieldId) != null){
                            var leftField: Field = board.getFieldById(leftFieldId)!!
                            if(leftField.isTaken()){
                                moves.add(Move(field.id, leftField.id))
                            }

                        }
                        if(board.getFieldById(rightFieldId) != null){
                            var rightField: Field = board.getFieldById(rightFieldId)!!
                            if(rightField.isTaken()){
                                moves.add(Move(field.id, rightField.id))
                            }

                        }
                        if(field.piece!!.pawnCanMoveTwo){
                            var straightTwoField: Field = board.getFieldById((field.letter).toString() + (field.number+2).toString())!!
                            if(!straightTwoField.isTaken()){
                                moves.add(Move(field.id, straightTwoField.id))
                            }
                        }
                    }
                }
            }

        }

        moves = checkMoves(moves)

        return moves.toTypedArray()
    }

    fun checkMoves(moves: MutableList<Move>): MutableList<Move> {
        var validMoves: MutableList<Move> = mutableListOf()
        for(move in moves){
            if(checkMove(move)){
                validMoves.add(move)
            }
        }
        return validMoves
    }

    private fun checkMove(move: Move): Boolean {
        val field: Field = board.getFieldById(move.originId)!!
        val targetField: Field = board.getFieldById(move.targetId)!!
        val piece: Piece = field.piece!!
        val targetPiece: Piece? = targetField.piece

        if(targetPiece != null){
            if(targetPiece.owner == piece.owner){
                return false
            }

        }

        return true
    }

    private fun checkForGameActive(): Boolean {
        var takenFields: Array<Field> = board.getTakenFields()

        

        true
    }

    override fun mouseClicked(e: MouseEvent?) {
        if (e != null) {
            if(e.component is Field){
                var field: Field = e.component as Field

                for(col in board.fields){
                    for(f in col){
                        f.background = f.color
                    }
                }

                if(pieceSetterMode){
                    board.setPiece(field.id, pieceSetterType, pieceSetterColor)
                    return
                }

                if(field.piece != null){
                    if(field.piece!!.owner != activePlayer.type && pieceMover == null){
                        println("Debug: Can't move, ${activePlayer.type.displayName}'s turn!")
                        return
                    }
                }

                if(field.piece != null || pieceMover != null){



                    if(pieceMover == null){
                        println("Debug: " + field.id + ": ${field.piece!!.displayName}")
                        pieceMover = field
                        var moves: Array<Move> = getPossibleMovesForPieceOn(field)

                        field.background = Color.CYAN
                        for(i in moves){
                            if(board.getFieldById(i.targetId)!!.color == Color.WHITE){
                                board.getFieldById(i.targetId)!!.background = Color.decode("#4444BB")
                            }else{
                                board.getFieldById(i.targetId)!!.background = Color.decode("#000077")
                            }
                        }
                    }
                    else{
                        var chosenMove: Move? = null

                        for(move in getPossibleMovesForPieceOn(pieceMover!!)){
                            if(move.targetId == field.id){
                                chosenMove = move
                            }
                        }

                        if(chosenMove != null){
                            pieceMover!!.piece!!.pawnCanMoveTwo = false
                            println("Debug: Moving...")
                            board.movePiece(chosenMove)
                            gameActive = checkForGameActive()

                            activePlayer = if(activePlayer == white){
                                black
                            }else{
                                white
                            }

                        }else{
                            println("Debug: Invalid Move!")
                        }
                        pieceMover = null
                    }

                }else{
                    println("Debug: " + field.id + ": empty")
                }
            }

        }
    }

    override fun mousePressed(e: MouseEvent?) {
    }

    override fun mouseReleased(e: MouseEvent?) {
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }


}