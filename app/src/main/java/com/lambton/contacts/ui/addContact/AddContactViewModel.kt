package com.lambton.contacts.ui.addContact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lambton.contacts.data.Contact
import com.lambton.contacts.data.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddContactViewModel @Inject constructor(private val repository: Repository): ViewModel()  {

    var firstName = ""
    var lastName = ""
    var phoneNumber = ""
    var email = ""

    private fun updateRepositoryContact(contact: Contact){
        viewModelScope.launch {
            repository.updateContact(contact)
        }
    }

    fun updateContact(contact: Contact){
        updateRepositoryContact(contact)
    }

    fun saveContact(contact: Contact){
        insertContact(contact)
    }

    private fun insertContact(contact: Contact){
        viewModelScope.launch {
            repository.insertContact(contact)
        }
    }

    fun getContactById(contactId: Int) = repository.getContactById(contactId)
}