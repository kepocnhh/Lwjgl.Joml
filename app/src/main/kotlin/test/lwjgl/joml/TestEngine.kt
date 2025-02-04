package test.lwjgl.joml

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryUtil
import java.util.concurrent.atomic.AtomicBoolean

internal class TestEngine {
    companion object {
        private val running = AtomicBoolean(false)

        private fun createWindow(
            monitorId: Long,
            width: Int,
            height: Int,
            onKeyCallback: GLFWKeyCallback,
            title: String = "Engine",
        ): Long {
            GLFW.glfwDefaultWindowHints()
            GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE) // todo
            GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE) // todo
//            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4)
//            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 1)
//            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE)
            val windowId = GLFW.glfwCreateWindow(
                width,
                height,
                title,
                MemoryUtil.NULL,
                MemoryUtil.NULL,
            )
            check(windowId != MemoryUtil.NULL) { "Window id is null!" }
            val mode = GLFW.glfwGetVideoMode(monitorId) ?: error("Failed to get video mode by monitor: $monitorId")
            val xPosition = (mode.width() - width) / 2
            val yPosition = (mode.height() - height) / 2
            GLFW.glfwSetWindowPos(
                windowId,
                xPosition,
                yPosition,
            )
            //
            GLFW.glfwMakeContextCurrent(windowId)
            GL.createCapabilities()
            GLFW.glfwSwapInterval(1)
            GLFW.glfwSetKeyCallback(windowId, onKeyCallback)
            return windowId
        }

        fun run(
            width: Int,
            height: Int,
            onKeyCallback: GLFWKeyCallback,
            onRender: (Long, Long) -> Unit,
        ) {
            if (!running.compareAndSet(false, true)) TODO()
            GLFWErrorCallback.createPrint(System.err).set()
            check(GLFW.glfwInit()) { "Unable to initialize GLFW!" }
            val monitorId = GLFW.glfwGetPrimaryMonitor()
            check(monitorId != MemoryUtil.NULL) { "Monitor id is null!" }
            val windowId = createWindow(
                monitorId = monitorId,
                width = width,
                height = height,
                onKeyCallback = onKeyCallback,
            )
            GLFW.glfwShowWindow(windowId)
//            GL11.glViewport(0, 0, width, height)
            //
            GL11.glClearColor(0f, 0f, 0f, 1f)
            val mode = GLFW.glfwGetVideoMode(monitorId) ?: error("Failed to get video mode by monitor: $monitorId")
            val timeMax = (1_000_000_000.0 / mode.refreshRate()).toLong()
            var timeLast = System.nanoTime()
            //
            GL11.glLineWidth(1f)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GL11.glDisable(GL11.GL_SMOOTH)
            GL11.glDisable(GL11.GL_POINT_SMOOTH)
            GL11.glDisable(GL11.GL_LINE_SMOOTH)
            GL11.glDisable(GL11.GL_POLYGON_SMOOTH)
            //
            while (!GLFW.glfwWindowShouldClose(windowId)) {
                val timeNow = System.nanoTime()
                val d = timeNow - timeLast
                if (d < timeMax) continue
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
                GLFW.glfwPollEvents()
                onRender(windowId, d / 1_000_000)
                GLFW.glfwSwapBuffers(windowId)
                timeLast = timeNow
            }
        }
    }
}
