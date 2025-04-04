package com.example.inventory.ui.home

import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.R
import com.example.inventory.data.Word
import com.example.inventory.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToEdit: (Int?) -> Unit,
    navigateToChoice: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WordListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = navigateToChoice) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    uiState.words.isEmpty() -> {
                        TextFieldWords(null, false, false, true)
                    }
                    else -> {
                        TextFieldWords(
                            word = uiState.currentWord,
                            showEnglish = uiState.showEnglish,
                            showMongolian = uiState.showMongolian,
                            showBoth = uiState.showBoth
                        )
                    }
                }
            }

            ActionButtons(
                onAdd = { navigateToEdit(null) },
                onEdit = { uiState.currentWord?.let { navigateToEdit(it.id) } },
                onDelete = { viewModel.showDeleteDialog(true) },
                onPrev = { viewModel.previousWord() },
                onNext = { viewModel.nextWord() },
                isWordEmpty = uiState.words.isEmpty()
            )
        }
    }

    if (uiState.showDeleteDialog) {
        DeleteConfirmationDialog(
            onYes = {
                viewModel.deleteCurrentWord()
                viewModel.showDeleteDialog(false)
            },
            onNo = { viewModel.showDeleteDialog(false) }
        )
    }
}

@Composable
private fun TextFieldWords(
    word: Word?,
    showEnglish: Boolean,
    showMongolian: Boolean,
    showBoth: Boolean,
    modifier: Modifier = Modifier
) {
    word?.let {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                value = if (showBoth || showEnglish) it.engWord else "",
                onValueChange = {},
                readOnly = true,
                textStyle = MaterialTheme.typography.displayMedium.copy(textAlign = TextAlign.Center),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = if (showBoth || showMongolian) it.monWord else "",
                onValueChange = {},
                readOnly = true,
                textStyle = MaterialTheme.typography.displayMedium.copy(textAlign = TextAlign.Center),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@Composable
private fun ActionButtons(
    onAdd: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    isWordEmpty: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onAdd,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.нэмэх))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onEdit,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(1f),
                enabled = !isWordEmpty
            ) {
                Text(stringResource(R.string.шинэчлэх))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onDelete,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(1f),
                enabled = !isWordEmpty
            ) {
                Text(stringResource(R.string.устгах))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onPrev,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(1f),
                enabled = !isWordEmpty
            ) {
                Text(stringResource(R.string.өмнөх))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onNext,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(1f),
                enabled = !isWordEmpty
            ) {
                Text(stringResource(R.string.дараа))
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onYes: () -> Unit,
    onNo: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onNo,
        title = { Text(stringResource(R.string.устгах)) },
        text = { Text(stringResource(R.string.dialog_info)) },
        confirmButton = {
            TextButton(onClick = onYes) {
                Text(stringResource(R.string.устгах))
            }
        },
        dismissButton = {
            TextButton(onClick = onNo) {
                Text(stringResource(R.string.болих))
            }
        }
    )
}