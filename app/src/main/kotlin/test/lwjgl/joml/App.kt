package test.lwjgl.joml

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryUtil

fun main() {
    GLFWErrorCallback.createPrint(System.err).set()
    check(GLFW.glfwInit()) { "Unable to initialize GLFW!" }
    val monitorId = GLFW.glfwGetPrimaryMonitor()
    check(monitorId != MemoryUtil.NULL) { "Monitor id is null!" }
    //
    GLFW.glfwDefaultWindowHints()
    GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE) // todo
    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE) // todo
    val width = 640
    val height = 480
    val windowId = GLFW.glfwCreateWindow(
        width,
        height,
        "Lwjgl.Joml",
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
    val onKeyCallback = object : GLFWKeyCallback() {
        override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
            when (key) {
                GLFW.GLFW_KEY_ESCAPE -> {
                    GLFW.glfwSetWindowShouldClose(windowId, true)
                }
                else -> Unit
            }
        }
    }
    GLFW.glfwSetKeyCallback(windowId, onKeyCallback)
    GLFW.glfwShowWindow(windowId)
    //
    GL11.glClearColor(0f, 0f, 0f, 1f)
    val timeMax = (1_000_000.0 / mode.refreshRate()).toLong()
    var timeLast = System.nanoTime() / 1_000
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
        val timeNow = System.nanoTime() / 1_000
        if (timeNow - timeLast < timeMax) continue
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        GLFW.glfwPollEvents()
        // todo
        GLFW.glfwSwapBuffers(windowId)
        timeLast = timeNow
    }
}
