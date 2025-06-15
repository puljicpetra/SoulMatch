package hr.unipu.java.soulmatch.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import hr.unipu.java.soulmatch.loadImageBitmap
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun ProfileImage(url: String, name: String) {
    val imageModifier = Modifier.size(150.dp).clip(CircleShape)
    if (url.isNotBlank()) {
        val bitmap = remember(url) { loadImageBitmap(File(url)) }

        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = "$name's profile picture",
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
        } else {
            println("ERROR: Could not load image from path: $url")
            ProfileImagePlaceholder(imageModifier)
        }
    } else {
        ProfileImagePlaceholder(imageModifier)
    }
}

@Composable
fun ProfileImagePlaceholder(modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(Color.LightGray)) {
        Icon(
            Icons.Default.Person,
            contentDescription = "No profile picture",
            modifier = Modifier.fillMaxSize(0.7f).align(Alignment.Center),
            tint = Color.White
        )
    }
}


@Composable
fun Chip(text: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFE0E0E0),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun LikeDislikeButtons(onDislike: () -> Unit, onLike: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = onDislike,
            modifier = Modifier.size(72.dp),
            shape = CircleShape,
            border = BorderStroke(2.dp, Color.Red)
        ) {
            Icon(Icons.Default.Clear, contentDescription = "Decline", tint = Color.Red, modifier = Modifier.size(36.dp))
        }
        Button(
            onClick = onLike,
            modifier = Modifier.size(72.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50))
        ) {
            Icon(Icons.Default.Done, contentDescription = "Like", tint = Color.White, modifier = Modifier.size(36.dp))
        }
    }
}

@Composable
fun FileDialog(
    parent: Frame? = null,
    onCloseRequest: (result: File?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, "Choose an image", LOAD) {
            override fun dispose() {
                onCloseRequest(files.firstOrNull())
                super.dispose()
            }
        }
    },
    dispose = FileDialog::dispose
)