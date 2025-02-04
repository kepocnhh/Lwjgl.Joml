package test.lwjgl.joml

import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.opengl.GL11

fun main() {
    val width = 640f
    val height = 480f
    val camera = BufferUtils.createFloatBuffer(16)
    val ui = BufferUtils.createFloatBuffer(16)
    var x = 0f
    var y = 0f
    var z = -1f
    TestEngine.run(
        width = width.toInt(),
        height = height.toInt(),
        onKeyCallback = object : GLFWKeyCallback() {
            override fun invoke(windowId: Long, key: Int, scancode: Int, action: Int, mods: Int) {
                when (key) {
                    GLFW.GLFW_KEY_ESCAPE -> {
                        GLFW.glfwSetWindowShouldClose(windowId, true)
                    }
                    GLFW.GLFW_KEY_C -> {
                        x = 0f
                        y = 0f
                        z = -1f
                    }
                    else -> Unit
                }
            }
        },
        onRender = { windowId: Long, diff: Long ->
            if (GLFW.glfwGetKey(windowId, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
                x -= 1.0f * diff.toFloat() / 1_000f
            } else if (GLFW.glfwGetKey(windowId, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
                x += 1.0f * diff.toFloat() / 1_000f
            } else if (GLFW.glfwGetKey(windowId, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
                y += 1.0f * diff.toFloat() / 1_000f
            } else if (GLFW.glfwGetKey(windowId, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
                y -= 1.0f * diff.toFloat() / 1_000f
            } else if (GLFW.glfwGetKey(windowId, GLFW.GLFW_KEY_Z) == GLFW.GLFW_PRESS) {
                z -= 1.0f * diff.toFloat() / 1_000f
            } else if (GLFW.glfwGetKey(windowId, GLFW.GLFW_KEY_X) == GLFW.GLFW_PRESS) {
                z += 1.0f * diff.toFloat() / 1_000f
            }
            //
            Matrix4f()
                .setPerspective(org.joml.Math.PI_OVER_2_f, width / height, 0.1f, 100f)
                .lookAt(Vector3f(x, y, z), Vector3f(), Vector3f(0f, 1f, 0f))
                .get(camera)
            GL11.glMatrixMode(GL11.GL_MODELVIEW)
            GL11.glLoadMatrixf(camera)
            GL11.glColor4f(1f, 0f, 0f, 1f)
            GL11.glBegin(GL11.GL_QUADS)
            GL11.glVertex3f(-.0f, -.0f, 0f)
            GL11.glVertex3f(+.5f, -.0f, 0f)
            GL11.glVertex3f(+.5f, +.5f, 0f)
            GL11.glVertex3f(-.0f, +.5f, 0f)
            GL11.glEnd()
            GL11.glColor4f(0f, 1f, 0f, 1f)
            GL11.glBegin(GL11.GL_QUADS)
            GL11.glVertex3f(-.5f, -.5f, 0f)
            GL11.glVertex3f(+.0f, -.5f, 0f)
            GL11.glVertex3f(+.0f, +.0f, 0f)
            GL11.glVertex3f(-.5f, +.0f, 0f)
            GL11.glEnd()
            GL11.glColor4f(0f, 0f, 1f, 1f)
            GL11.glBegin(GL11.GL_QUADS)
            GL11.glVertex3f(+.5f, -.0f, 0f)
            GL11.glVertex3f(+.5f, -.5f, 0f)
            GL11.glVertex3f(+.0f, -.5f, 0f)
            GL11.glVertex3f(-.0f, +.0f, 0f)
            GL11.glEnd()
            GL11.glPushMatrix()
            GL11.glLoadIdentity()
            //
            Matrix4f()
                .setOrtho(0f, width, height, 0f, -1f, 100f)
                .get(ui)
            GL11.glMatrixMode(GL11.GL_MODELVIEW)
            GL11.glLoadMatrixf(ui)
            GL11.glColor4f(1f, 0f, 1f, 1f)
            GL11.glBegin(GL11.GL_LINES)
            GL11.glVertex3f(width / 2, 0f, 0f)
            GL11.glVertex3f(width / 2, height, 0f)
            GL11.glEnd()
            GL11.glBegin(GL11.GL_LINES)
            GL11.glVertex3f(0f, height / 2, 0f)
            GL11.glVertex3f(width, height / 2, 0f)
            GL11.glEnd()
            GL11.glPushMatrix()
            GL11.glLoadIdentity()
        },
    )
}
