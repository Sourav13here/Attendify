package com.example.attendify.ui.teacher.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.attendify.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubjectPopup(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, String) -> Unit // subjectCode, subjectName, branch, semester
) {
    var selectedBranch by remember { mutableStateOf(Constants.BRANCHES[0]) }
    var selectedSemester by remember { mutableStateOf(Constants.SEMESTERS[0]) }
    var subjectName by remember { mutableStateOf("") }
    var subjectCode by remember { mutableStateOf("") }

    var expandedBranch by remember { mutableStateOf(false) }
    var expandedSemester by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onSubmit(subjectCode, subjectName, selectedBranch, selectedSemester)
                onDismiss()
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add Subject") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = subjectName,
                    onValueChange = { subjectName = it },
                    label = { Text("Subject Name") }
                )

                OutlinedTextField(
                    value = subjectCode,
                    onValueChange = { subjectCode = it },
                    label = { Text("Subject Code") }
                )

                ExposedDropdownMenuBox(
                    expanded = expandedBranch,
                    onExpandedChange = { expandedBranch = !expandedBranch }
                ) {
                    OutlinedTextField(
                        value = selectedBranch,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Branch") },
                        trailingIcon = {

                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedBranch)
                        },
                        modifier = Modifier.menuAnchor()
                    )

                    DropdownMenu(
                        expanded = expandedBranch,
                        onDismissRequest = { expandedBranch = false }
                    ) {
                        Constants.BRANCHES.forEach { branch ->
                            DropdownMenuItem(
                                text = { Text(branch) },
                                onClick = {
                                    selectedBranch = branch
                                    expandedBranch = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = expandedSemester,
                    onExpandedChange = { expandedSemester = !expandedSemester }
                ) {
                    OutlinedTextField(
                        value = selectedSemester,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Semester") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSemester)
                        },
                        modifier = Modifier.menuAnchor()
                    )

                    DropdownMenu(
                        expanded = expandedSemester,
                        onDismissRequest = { expandedSemester = false }
                    ) {
                        Constants.SEMESTERS.forEach { semester ->
                            DropdownMenuItem(
                                text = { Text(semester) },
                                onClick = {
                                    selectedSemester = semester
                                    expandedSemester = false
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}