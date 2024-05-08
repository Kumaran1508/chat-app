package com.teknophase.chat.viewmodel

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import com.teknophase.chat.data.state.ContactsState
import com.teknophase.chat.providers.ContactsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsProvider: ContactsProvider
): ViewModel() {
    private var contactsState: MutableStateFlow<ContactsState> = MutableStateFlow(ContactsState())
    val _contactsState: StateFlow<ContactsState> = contactsState.asStateFlow()

    fun loadContacts(context: Context, activity: ComponentActivity) {
        contactsState.value = contactsState.value.copy(isLoading = true)
        val contacts = contactsProvider.getPhoneContacts(context, activity)
        contactsState.value = contactsState.value.copy(isLoading = false, contacts = contacts)
    }

}