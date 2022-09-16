package board

import Game
import piece.Piece
import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

class Field(val number: Int, val letter: Char, val color: Color, private val gameInstance: Game): JPanel(){
    val iconLabel: JLabel = JLabel()
    var piece: Piece? = null
        set(piece) {
            if(piece != null){
                iconLabel.icon = piece.image
                iconLabel.isVisible = true
            }else{
                iconLabel.isVisible = false
            }
            field = piece
        }
    val id: String = letter + number.toString()

    init{

        this.background = color
        this.addMouseListener(gameInstance)
        this.iconLabel.isVisible = false
        this.add(iconLabel)

    }

    fun isTaken(): Boolean{
        return piece != null
    }

    //Gibt alle (bis zu 8) angrenzenden Felder zurück, angefangen mit dem Oben links und dann Reihe für Reihe, wobei das DIESES Feld ausgelassen wird.
    fun getAdjacentFields(): Array<Field>{
        var adjacents: MutableList<Field> = mutableListOf()

        for(i in -1..1){
            for(j in -1..1){
                if(i == 0 && j == 0){
                    continue
                }
                var f: Field = gameInstance.board.getFieldById((letter+j).toString()+(number+i).toString())?: continue
                adjacents.add(f)
            }
        }
        return adjacents.toTypedArray()
    }

    //Gibt die diagonale Reihe links oben zurück
    fun getDiagonalTopLeftFields(): Array<Field>{
        var diagonals: MutableList<Field> = mutableListOf()

        var counter: Int = 0
        var letter: Char = this.letter
        var number: Int = this.number
        while(letter-1 >= 'A' && number-1 >= 1){
            letter--
            number--
            var id: String = letter.toString() + number.toString()

            if(gameInstance.board.getFieldById(id)!!.isTaken()){
                diagonals.add(gameInstance.board.getFieldById(id)!!)
                break
            }
            diagonals.add(gameInstance.board.getFieldById(id)!!)

        }
        return diagonals.toTypedArray()
    }

    //Gibt die diagonale Reihe rechts oben zurück
    fun getDiagonalTopRightFields(): Array<Field>{
        var diagonals: MutableList<Field> = mutableListOf()

        var counter: Int = 0
        var letter: Char = this.letter
        var number: Int = this.number
        while(letter+1 <= 'H' && number-1 >= 1){
            letter++
            number--
            var id: String = letter.toString() + number.toString()

            if(gameInstance.board.getFieldById(id)!!.isTaken()){
                diagonals.add(gameInstance.board.getFieldById(id)!!)
                break
            }
            diagonals.add(gameInstance.board.getFieldById(id)!!)

        }
        return diagonals.toTypedArray()
    }

    //Gibt die diagonale Reihe links unten zurück
    fun getDiagonalBottomLeftFields(): Array<Field>{
        var diagonals: MutableList<Field> = mutableListOf()

        var counter: Int = 0
        var letter: Char = this.letter
        var number: Int = this.number
        while(letter-1 >= 'A' && number+1 <= 8){
            letter--
            number++
            var id: String = letter.toString() + number.toString()

            if(gameInstance.board.getFieldById(id)!!.isTaken()){
                diagonals.add(gameInstance.board.getFieldById(id)!!)
                break
            }
            diagonals.add(gameInstance.board.getFieldById(id)!!)

        }
        return diagonals.toTypedArray()
    }

    //Gibt die diagonale Reihe rechts unten zurück
    fun getDiagonalBottomRightFields(): Array<Field>{
        var diagonals: MutableList<Field> = mutableListOf()

        var counter: Int = 0
        var letter: Char = this.letter
        var number: Int = this.number
        while(letter+1 <= 'H' && number+1 <= 8){
            letter++
            number++
            var id: String = letter.toString() + number.toString()

            if(gameInstance.board.getFieldById(id)!!.isTaken()){
                diagonals.add(gameInstance.board.getFieldById(id)!!)
                break
            }
            diagonals.add(gameInstance.board.getFieldById(id)!!)

        }
        return diagonals.toTypedArray()
    }

    //Gibt alle Diagonalen Felder zurück, ohne DIESES Feld
    fun getDiagonals(): Array<Field>{
        var diagonals: MutableList<Field> = mutableListOf()
        diagonals.addAll(getDiagonalTopLeftFields())
        diagonals.addAll(getDiagonalTopRightFields())
        diagonals.addAll(getDiagonalBottomLeftFields())
        diagonals.addAll(getDiagonalBottomRightFields())

        return diagonals.toTypedArray()
    }

    fun getBottomColumn(): Array<Field>{
        var diagonals: MutableList<Field> = mutableListOf()

        var letter: Char = this.letter
        var number: Int = this.number
        while(number+1 <= 8){
            number++
            var id: String = letter.toString() + number.toString()

            if(gameInstance.board.getFieldById(id)!!.isTaken()){
                diagonals.add(gameInstance.board.getFieldById(id)!!)
                break
            }
            diagonals.add(gameInstance.board.getFieldById(id)!!)

        }
        return diagonals.toTypedArray()
    }

    fun getTopColumn(): Array<Field>{
        var diagonals: MutableList<Field> = mutableListOf()

        var letter: Char = this.letter
        var number: Int = this.number
        while(number-1 >= 1){
            number--
            var id: String = letter.toString() + number.toString()

            if(gameInstance.board.getFieldById(id)!!.isTaken()){
                diagonals.add(gameInstance.board.getFieldById(id)!!)
                break
            }
            diagonals.add(gameInstance.board.getFieldById(id)!!)

        }
        return diagonals.toTypedArray()
    }

    fun getleftRow(): Array<Field>{
        var diagonals: MutableList<Field> = mutableListOf()

        var letter: Char = this.letter
        var number: Int = this.number
        while(letter-1 >= 'A'){
            letter--
            var id: String = letter.toString() + number.toString()

            if(gameInstance.board.getFieldById(id)!!.isTaken()){
                diagonals.add(gameInstance.board.getFieldById(id)!!)
                break
            }
            diagonals.add(gameInstance.board.getFieldById(id)!!)

        }
        return diagonals.toTypedArray()
    }

    fun getRightRow(): Array<Field>{
        var diagonals: MutableList<Field> = mutableListOf()

        var letter: Char = this.letter
        var number: Int = this.number
        while(letter+1 <= 'H'){
            letter++
            var id: String = letter.toString() + number.toString()

            if(gameInstance.board.getFieldById(id)!!.isTaken()){
                diagonals.add(gameInstance.board.getFieldById(id)!!)
                break
            }
            diagonals.add(gameInstance.board.getFieldById(id)!!)

        }
        return diagonals.toTypedArray()
    }

    fun getStraights(): Array<Field>{
        return getRightRow() + getleftRow() + getTopColumn() + getBottomColumn()
    }

}
