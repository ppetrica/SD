import java.io.File

class Logger(private val filepath: String) {
    fun log(message: String) {
        println(message)
        File(filepath).appendText(message + '\n')
    }
}