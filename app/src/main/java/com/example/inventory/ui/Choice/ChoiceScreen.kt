package com.example.inventory.ui.Choice

import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.R
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.home.WordListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WordListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { })
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            RadioRow(
                text = stringResource(R.string.хоёр_харуулах),
                selected = uiState.showBoth,
                onSelect = { viewModel.toggleShowBoth() }
            )

            RadioRow(
                text = stringResource(R.string.гадаад_үг_харуулах),
                selected = uiState.showEnglish,
                onSelect = { viewModel.toggleShowEnglish() }
            )

            RadioRow(
                text = stringResource(R.string.монгол_үг_харуулах),
                selected = uiState.showMongolian,
                onSelect = { viewModel.toggleShowMongolian() }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp).padding(top = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = navigateBack,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(text = stringResource(R.string.буцах))
                }

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = {
                        viewModel.viewModelScope.launch {
                            viewModel.savePreferences()
                            navigateBack()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(stringResource(R.string.хадгалах))
                }
            }
        }
    }
}

@Composable
private fun RadioRow(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.width(300.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}