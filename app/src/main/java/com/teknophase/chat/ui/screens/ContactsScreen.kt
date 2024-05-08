package com.teknophase.chat.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import com.teknophase.chat.ui.common.AppTextField
import com.teknophase.chat.ui.constants.padding_small
import com.teknophase.chat.ui.contact.ContactItem
import com.teknophase.chat.viewmodel.ContactsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ContactsScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        val viewModel: ContactsViewModel = hiltViewModel()
        val state = viewModel._contactsState.collectAsState()
        val context = LocalContext.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        var searchText by remember {
            mutableStateOf("")
        }

        LaunchedEffect(key1 = true) {
            launch(Dispatchers.IO) {
                viewModel.loadContacts(context, context as ComponentActivity)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = padding_small),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Search
                AppTextField(
                    title = "",
                    value = searchText,
                    onValueChange = {
                        searchText = it
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    ),
                    placeholder = "search...",
                    leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = "search icon") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = padding_small)
                )
            }

            if (state.value.isLoading && state.value.contacts.isEmpty())
                item {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(padding_small)
                    )
                }
            else
                for (contact in state.value.contacts
                    .filter { contact ->
                        if (searchText.isNotEmpty())
                            contact.name.contains(searchText) || contact.phoneNumbers.any {
                                it.contains(searchText)
                            }
                        else true
                    }.sortedBy {
                        it.name
                    }) {
                    item {
                        ContactItem(contact = contact)
                    }
                }
        }
    }
}