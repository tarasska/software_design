package rendering.impl.app

import graph.Graph
import rendering.RenderingApi
import rendering.impl.api.AwtRenderer
import java.awt.Frame
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import kotlin.system.exitProcess

class AwtFrame(
    private val canvasHeight: Int,
    private val canvasWidth: Int,
    private val constructGraph: (RenderingApi<Double>) -> Graph
) : Frame(), Runnable {
    init {
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                exitProcess(0)
            }
        })
        setSize(canvasWidth, canvasHeight)
    }

    override fun paint(g: Graphics?) {
        super.paint(g)
        constructGraph(
            AwtRenderer(
                g as Graphics2D,
                canvasHeight.toLong(),
                canvasWidth.toLong()
            )
        ).renderGraph()
    }

    override fun run() {
        super.setVisible(true)
    }
}