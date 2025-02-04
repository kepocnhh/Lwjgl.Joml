package test.lwjgl.joml

import org.joml.Matrix4d
import org.joml.Matrix4f
import org.joml.Vector2d
import org.joml.Vector3d
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.opengl.GL11

fun main() {
    val width = 640f
    val height = 480f
    val fb = BufferUtils.createFloatBuffer(16)
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
            val model = Matrix4f()
            val view = Matrix4f().translation(x, y, z)
            val projection = Matrix4f()
            projection.setPerspective(org.joml.Math.PI_OVER_2_f, width / height, 0.1f, 100f)
            GL11.glLoadMatrixf(projection.mul(view.mul(model)).get(fb))
            GL11.glColor4f(1f, 0f, 0f, 1f)
            GL11.glBegin(GL11.GL_QUADS)
            GL11.glVertex3f(-.0f, -.0f, 0f)
            GL11.glVertex3f(+.5f, -.0f, 0f)
            GL11.glVertex3f(+.5f, +.5f, 0f)
            GL11.glVertex3f(-.0f, +.5f, 0f)
            GL11.glEnd()
        },
    )
}
