package com.lambton.contacts.di.contactsList

import androidx.lifecycle.ViewModel
import com.lambton.contacts.di.ViewModelKey
import com.lambton.contacts.ui.contactsList.ContactsListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ContactsListViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ContactsListViewModel::class)
    abstract fun bindContactsListViewModel(contactsListViewModel: ContactsListViewModel): ViewModel

}