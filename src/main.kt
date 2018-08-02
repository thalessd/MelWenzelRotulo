import br.com.devdes.melwenzelrotulo.model.Util
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage


import javafx.scene.image.Image

class App : Application() {
    override fun start(primaryStage: Stage?) {

        val u = Util()

        val root : Parent = FXMLLoader.load(
                javaClass.getResource("${ u.pathPackage() }/view/main.fxml")
        )

        primaryStage?.title = "Gera Rótulo - Mel Wenzel"
        primaryStage?.isResizable = false
        primaryStage?.scene = Scene(root)
        primaryStage?.icons?.addAll(
                Image("${ u.pathPackage() }/assets/icon.png")
        )
        primaryStage?.show()
    }

    fun main(args: Array<String>) {
        Application.launch(*args)
    }
}

fun main(args: Array<String>) {
    App().main(args)
}