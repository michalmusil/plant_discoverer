package cz.mendelu.xmusil5.plantdiscoverer.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    labelTitle: String,
    value: MutableState<String>,
    errorMessage: MutableState<String>? = null,
    onTextChanged: (String) -> Unit = {},
    enabled: Boolean = true,
    singleLine: Boolean = true
){
    Column(
        modifier = Modifier
            .padding(vertical = 5.dp)
    ) {
        if (errorMessage != null && !errorMessage.value.isEmpty()){
            Text(
                text = errorMessage.value,
                color = Color.Red
            )
        }
        OutlinedTextField(
            value = value.value,
            onValueChange = {
                value.value = it
                onTextChanged(it)
            },
            label = {
                Text(text = labelTitle)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                disabledBorderColor = MaterialTheme.colorScheme.primary,
                disabledLabelColor = MaterialTheme.colorScheme.primary,
                disabledTextColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = enabled,
            singleLine = singleLine
        )
    }
}