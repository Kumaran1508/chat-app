package com.teknophase.chat.providers

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.teknophase.chat.data.model.Contact
import java.io.InputStream
import javax.inject.Inject


class ContactsProvider @Inject constructor() {
    @SuppressLint("Recycle", "Range")
    fun getPhoneContacts(context: Context, activity: Activity): List<Contact> {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_CONTACTS),
                0
            )
        } else {
            val contentResolver = activity.contentResolver
            val uri = ContactsContract.Contacts.CONTENT_URI
            val cursor = contentResolver.query(uri, null, null, null, null)
            val contacts = mutableListOf<Contact>()

            while (cursor?.moveToNext() == true) {
                val contactId =
                    cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val displayName =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                val contactUri = Uri.withAppendedPath(
                    ContactsContract.Contacts.CONTENT_URI,
                    contactId.toString()
                )
                val phoneNumbers = getContactNumbers(contactUri, contentResolver)

                val bitmap = getContactPhoto(contactUri, activity.contentResolver)

                if (phoneNumbers.isNotEmpty())
                    contacts.add(
                        Contact(contactId.toString(), displayName ?: "", phoneNumbers, bitmap)
                    )
            }

            return contacts.toList()
        }

        return emptyList()
    }

    private fun getContactPhoto(contactUri: Uri, contentResolver: ContentResolver): Bitmap? {
        var inputStream: InputStream? = null
        try {
            inputStream = ContactsContract.Contacts.openContactPhotoInputStream(
                contentResolver,
                contactUri
            )
            if (inputStream != null) {
                return BitmapFactory.decodeStream(inputStream)
            }
        } finally {
            inputStream?.close()
        }
        return null
    }

    @SuppressLint("Range")
    private fun getContactNumbers(contactUri: Uri, contentResolver: ContentResolver): List<String> {
        val numbers = mutableSetOf<String>()
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            arrayOf(ContentUris.parseId(contactUri).toString()),
            null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val phoneNumber =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                numbers.add(phoneNumber.replace(" ", ""))
            }
        }
        return numbers.toList()
    }
}

