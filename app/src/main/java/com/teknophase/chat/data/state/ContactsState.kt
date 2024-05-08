package com.teknophase.chat.data.state

import com.teknophase.chat.data.model.Contact

data class ContactsState(
    val isLoading: Boolean = false,
    val contacts: List<Contact> = emptyList()
)
