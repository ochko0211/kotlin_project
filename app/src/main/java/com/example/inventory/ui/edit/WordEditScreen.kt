package com.example.inventory.ui.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.example.inventory.R
import com.example.inventory.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordEditScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WordEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {  },
                navigationIcon = {
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = uiState.engWord,
                onValueChange = { viewModel.updateEngWord(it) },
                label = { Text(stringResource(R.string.гадаад_үг)) },
                modifier = Modifier.fillMaxWidth()

            )

            TextField(
                value = uiState.monWord,
                onValueChange = { viewModel.updateMonWord(it) },
                label = { Text(stringResource(R.string.монгол_үг)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.width(50.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = navigateBack,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(stringResource(R.string.болих))
                }

                Spacer(Modifier.width(16.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveWord()
                            navigateBack()
                        }
                    },

                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(stringResource(R.string.оруулах))
                }
            }
        }
    }
}