package org.luteh.ecommerce.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun <T> RoundedDropdownField(
    modifier: Modifier = Modifier,
    dropDownModifier: Modifier = Modifier,
    items: List<T>,
    selectedItemText: String,
    onItemSelected: (T) -> Unit,
    labelText: String,
    dropdownItemText: @Composable (T, Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        RoundedTextField(
            modifier = modifier,
            valueText = selectedItemText,
            labelText = labelText,
            readOnly = true,
            trailingIcon = {
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
            }
        )
        DropdownMenu(
            modifier = dropDownModifier,
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { dropdownItemText(item, index) },
                    onClick = {
                        onItemSelected(items[index])
                        expanded = false
                    })
            }
        }
        Spacer(modifier = Modifier
            .matchParentSize()
            .clickable { expanded = true })
    }
}

@Composable
@Preview()
private fun RoundedDropdownFieldPreview() {
    RoundedDropdownField(
        items = listOf("Item 1", "Item 2", "Item 3"),
        selectedItemText = "Item 1",
        onItemSelected = {},
        labelText = "Label",
        dropdownItemText = { text, _ ->
            Text(text = text)
        })
}