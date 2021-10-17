package rendering.impl.app

import graph.Graph
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.stage.Stage
import rendering.RenderingApi
import rendering.impl.api.JavaFXRenderer

class JavaFXApplicationRunner(
    canvasHeight: Int,
    canvasWidth: Int,
    constructGraph: (RenderingApi<Double>) -> Graph
): Runnable {
    class JavaFXApplication : Application() {
        override fun start(primaryStage: Stage) {
            val canvas = Canvas(staticWidth.toDouble(), staticHeight.toDouble())
            primaryStage.scene = Scene(Group(canvas), Color.CORAL)
            staticGraphConstructor(
                JavaFXRenderer(
                    canvas,
                    staticHeight.toLong(),
                    staticWidth.toLong()
                )
            ).renderGraph()
            primaryStage.isResizable = false
            primaryStage.show()
        }
    }

    init {
        staticHeight = canvasHeight
        staticWidth = canvasWidth
        staticGraphConstructor = constructGraph
    }

    companion object {
        private var staticHeight: Int = 400
        private var staticWidth: Int = 600
        private lateinit var staticGraphConstructor: (RenderingApi<Double>) -> Graph
    }

    override fun run() {
        launch(JavaFXApplication::class.java)
    }
}