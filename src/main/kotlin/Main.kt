import java.awt.EventQueue
import kotlin.system.exitProcess

fun main(args: Array<String>){

    println("Game is Starting...")

    var game: Game = Game()
    EventQueue.invokeLater(game)

    while(true){
        var input = readLine()
        if(input == "exit"){
            println("Stopping...")
            exitProcess(0)
        }
        else{
            println("I am Running")
        }
    }

}