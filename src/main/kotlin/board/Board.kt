package board

import Game
import piece.Piece
import piece.PieceType
import player.PlayerType
import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import javax.swing.*

class Board(gameInstance: Game): JFrame() {
    val fields: Array<Array<Field>> = Array(8) {i -> Array(8) {j -> Field(i+1, (j+65).toChar(), if(i%2 == 0){if(j%2 == 0){Color.WHITE} else{Color.DARK_GRAY }} else {if(j%2 != 0){Color.WHITE} else{Color.DARK_GRAY }}, gameInstance)} }
        get() = field

    private val windowSize: Dimension = Dimension(600, 800)
    val panda: JPanel = JPanel()
    val playFieldPanel: JPanel = JPanel()
    val controlPanel: JPanel = JPanel()

    init {

        this.size = windowSize
        this.title = "Chess"
        this.setLocationRelativeTo(null)
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.isResizable = false

        createBoard(gameInstance)

    }

    fun getFieldById(id: String): Field?{
        for(i in fields){
            for(j in i){
                if(j.id == id){
                    return j
                }
            }
        }
        return null
    }

    fun printBoard(){
        println("Printing Board...")
        for(row in fields){
            println()
            for(column in row){
                print("${column.id} ")
            }
        }
        println()
    }

    fun createBoard(gameInstance: Game){
        panda.layout = BoxLayout(panda, BoxLayout.Y_AXIS)
        playFieldPanel.preferredSize = Dimension(windowSize.width, windowSize.height*3/4)
        controlPanel.preferredSize = Dimension(windowSize.width, windowSize.height/4)
        //playFieldPane.background = Color.GREEN
        controlPanel.background = Color.GREEN

        playFieldPanel.layout = GridLayout(8, 8)
        for(cols in fields){
            for(field in cols){
                playFieldPanel.add(field)
            }
        }
        addControls(gameInstance)

        panda.add(playFieldPanel)
        panda.add(controlPanel)

        this.contentPane.add(panda)
        pack()
    }

    private fun addControls(gameInstance: Game) {

        val pieceSetterButton: JToggleButton = JToggleButton("Set Piece Mode")
        pieceSetterButton.addActionListener {
            gameInstance.pieceSetterMode = !gameInstance.pieceSetterMode
        }
        controlPanel.add(pieceSetterButton)


        val piecesBox: JComboBox<PieceType?> = JComboBox<PieceType?>()
        piecesBox.addItem(PieceType.KING)
        piecesBox.addItem(PieceType.ROOK)
        piecesBox.addItem(PieceType.QUEEN)
        piecesBox.addItem(PieceType.BISHOP)
        piecesBox.addItem(PieceType.KNIGHT)
        piecesBox.addItem(PieceType.PAWN)
        piecesBox.addItem(null)

        piecesBox.addActionListener {
            gameInstance.pieceSetterType = (it.source as JComboBox<*>).selectedItem as PieceType?

        }
        controlPanel.add(piecesBox)


        val colorCheckBox: JCheckBox = JCheckBox("White Figures: ")
        colorCheckBox.addActionListener {
            gameInstance.pieceSetterColor = if(gameInstance.pieceSetterColor == PlayerType.BLACK){
                PlayerType.WHITE
            }else{
                PlayerType.BLACK
            }
        }
        controlPanel.add(colorCheckBox)


        val resetButton: JButton = JButton("Reset")
        resetButton.addActionListener {
            reset(gameInstance)
        }
        controlPanel.add(resetButton)


        val clearButton: JButton = JButton("Clear")
        clearButton.addActionListener {
            clear()
        }
        controlPanel.add(clearButton)


        val positionTextField: JTextField = JTextField("File name")
        controlPanel.add(positionTextField)


        val savePositionButton: JButton = JButton("Save Position")
        savePositionButton.addActionListener {
            savePosition(positionTextField.text)
        }
        controlPanel.add(savePositionButton)



        val loadPositionButton: JButton = JButton("Load Position")
        loadPositionButton.addActionListener {
            loadPosition(positionTextField.text)
        }
        controlPanel.add(loadPositionButton)

    }

    fun clear(){
        for(cols in fields){
            for(field in cols){
                field.piece = null
            }
        }
    }

    fun reset(gameInstance: Game) {
        clear()
        loadPosition("default")
        gameInstance.activePlayer = gameInstance.white
    }

    fun loadPosition(fileName: String) {
        var file: File = File("Positions/$fileName.txt")
        if(!file.exists()){
            println("Debug: File '$fileName' does not exist!")
            return
        }
        var fileContent: String = ""
        for(line in file.readLines()){
            fileContent += line
        }
        for(i in fileContent.split(';')){
            if(i == ""){
                continue
            }
            var split1: List<String> = i.split('#')
            var split2: List<String> = split1[0].split(':')

            var id: String = split2[0]
            var type: String = split2[1]
            var owner: String = split1[1]

            setPiece(id, if(type == "null") null else PieceType.valueOf(type), if(owner == "null") PlayerType.BLACK else PlayerType.valueOf(owner))
        }
        println("Debug: Loaded Position '$fileName'")

    }

    fun savePosition(fileName: String){
        var file: File = File("Positions/$fileName.txt")
        if(file.exists()){
            println("Debug: File '$fileName' already exists!")
            return
        }
        file.createNewFile()
        for(col in fields){
            for(field in col){
                file.appendText("${field.id}:${field.piece?.type?.name}#${field.piece?.owner};\n")
            }
        }
        println("Debug: Saved Position '$fileName'")

    }

    fun movePiece(move: Move){
        var field: Field = getFieldById(move.originId)!!
        var piece: Piece = field.piece!!
        var targetField: Field = getFieldById(move.targetId)!!
        setPiece(move.originId, null, PlayerType.BLACK)
        targetField.piece = piece

    }

    fun setPiece(targetFieldId: String, type: PieceType?, owner: PlayerType){
        var targetField: Field = getFieldById(targetFieldId)!!
        var piece: Piece? = createPiece(type, owner)
        targetField.piece = piece
    }

    private fun createPiece(type: PieceType?, owner: PlayerType): Piece? {
        if(type == null){
            return null
        }
        return Piece(type, owner)
    }

    fun getTakenFields(): Array<Field> {
        var list: MutableList<Field> = mutableListOf()
        for(col in fields){
            for(field in col){
                if(field.isTaken()){
                    list.add(field)
                }
            }
        }
        return list.toTypedArray()
    }

}