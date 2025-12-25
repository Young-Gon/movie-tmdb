package com.gondev.movie.ui.common.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.gondev.movie.ui.component.button.FilledButton
import com.gondev.movie.ui.component.button.LinedButton

object MovieDialog {
    var dialogStatus: MovieDialogStatus? by mutableStateOf(null)

    fun show(
        onDismiss: () -> Unit,
        properties: DialogProperties = DialogProperties(true, true, false),
        body: @Composable () -> Unit
    ) {
        dialogStatus = MovieDialogStatus(
            body = body,
            onDismiss = onDismiss,
            properties = properties
        )
    }

    fun showDialog(
        icon: ImageVector? = null,
        title: String? = null,
        onCancel: MovieDialogButton? = null,
        onOk: MovieDialogButton,
        properties: DialogProperties = DialogProperties(true, true, false),
        body: @Composable () -> Unit,
    ) {

        dialogStatus = MovieDialogStatus(onCancel?.onClick ?: onOk.onClick, properties) {
            MovieDialogBody(
                icon = icon,
                title = title,
                body = body,
                cancelButton = onCancel,
                okButton = onOk
            )
        }
    }

    fun showDialog(
        icon: ImageVector? = null,
        title: String? = null,
        body: String? = null,
        properties: DialogProperties = DialogProperties(true, true, false),
        onCancel: MovieDialogButton? = null,
        onOk: MovieDialogButton,
    ) {

        dialogStatus = MovieDialogStatus(onCancel?.onClick ?: onOk.onClick, properties) {
            MovieDialogBody(
                icon = icon,
                title = title,
                cancelButton = onCancel,
                okButton = onOk,
            ) {
                if (body != null) {
                    Text(body)
                }
            }
        }
    }

    fun dismiss() {
        dialogStatus = null
    }
}

data class MovieDialogStatus(
    val onDismiss: () -> Unit = {},
    val properties: DialogProperties = DialogProperties(true, true, false),
    val body: @Composable () -> Unit,
)

sealed class MovieDialogButton(
    val text: String,
    val onClick: () -> Unit
) {
    class Cancel(text: String = "Cancel", onClick: () -> Unit) : MovieDialogButton(text, onClick)
    class Ok(text: String = "Ok", onClick: () -> Unit) : MovieDialogButton(text, onClick)
}

@Composable
fun MovieDialog(
    status: MovieDialogStatus? = LocalMovieDialogStatus.current,
) {
    if (status != null) {
        Dialog(
            onDismissRequest = status.onDismiss,
            properties = status.properties
        ) {
            status.body()
        }
    }
}

@Composable
private fun MovieDialogBody(
    modifier: Modifier = Modifier,
    icon: ImageVector?,
    title: String?,
    cancelButton: MovieDialogButton?,
    okButton: MovieDialogButton,
    body: @Composable () -> Unit,
) {
    @Composable
    fun RowScope.DialogButton(dialogButton: MovieDialogButton) {
        when (dialogButton) {
            is MovieDialogButton.Cancel -> LinedButton(
                modifier = Modifier
                    .weight(1f),
                onClick = dialogButton.onClick
            ) {
                Text(
                    text = dialogButton.text,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            is MovieDialogButton.Ok -> FilledButton(
                modifier = Modifier
                    .weight(1f),
                onClick = dialogButton.onClick
            ) {
                Text(
                    text = dialogButton.text,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }

    ElevatedCard(
        modifier = modifier
            .padding(16.dp)
            .widthIn(max = 480.dp)
            .fillMaxWidth()
            .safeDrawingPadding(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Dialog Icon"
                    )
                }
                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
                body()
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (cancelButton != null) {
                    DialogButton(cancelButton)
                }
                DialogButton(okButton)
            }
        }
    }
}

val LocalMovieDialogStatus = compositionLocalOf {
    MovieDialog.dialogStatus
}

@Preview
@Composable
private fun MovieDialogBodyPreview() {
    MovieDialogBody(
        icon = Icons.Default.Info,
        title = "Title",
        cancelButton = MovieDialogButton.Cancel("Cancel") {},
        okButton = MovieDialogButton.Ok("OK") {}
    ) {
        Text("This is the body of the dialog.")
    }
}
