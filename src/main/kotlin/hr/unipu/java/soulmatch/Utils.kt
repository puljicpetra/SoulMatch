package hr.unipu.java.soulmatch

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image as SkiaImage
import java.io.File

fun loadImageBitmap(file: File): ImageBitmap? {
    return try {
        SkiaImage.makeFromEncoded(file.readBytes()).toComposeImageBitmap()
    } catch (e: Exception) {
        println("Error loading image: ${e.message}")
        null
    }
}

fun getAppDataDirectory(): File {
    val userHome = System.getProperty("user.home")
    val appDir = File(userHome, ".soulmatch-app")
    if (!appDir.exists()) {
        appDir.mkdirs()
    }
    return appDir
}