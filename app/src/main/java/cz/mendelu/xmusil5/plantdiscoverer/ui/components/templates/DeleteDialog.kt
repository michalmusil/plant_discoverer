package cz.mendelu.xmusil5.plantdiscoverer.ui.components.templates

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.CustomOutlinedButton
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.grayCommon

const val TAG_DELETE_DIALOG_OK = "deleteDialogOk"
const val TAG_DELETE_DIALOG_CANCEL = "deleteDialogCancel"

@Composable
fun DeleteDialog(
    showDialog: MutableState<Boolean>,
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit = {}){
    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            CustomOutlinedButton(
                text = stringResource(id = R.string.delete),
                textSize = 12.sp,
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .padding(bottom = 5.dp)
                    .testTag(TAG_DELETE_DIALOG_OK),
                onClick = {
                    showDialog.value = false
                    onConfirm()
                }
            )
        },
        dismissButton = {
            CustomOutlinedButton(
                text = stringResource(id = R.string.cancel),
                textSize = 12.sp,
                backgroundColor = grayCommon,
                textColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .padding(bottom = 5.dp)
                    .testTag(TAG_DELETE_DIALOG_CANCEL),
                onClick = {
                    showDialog.value = false
                    onCancel()
                }
            )
        },
        backgroundColor = MaterialTheme.colorScheme.background,
    )

}