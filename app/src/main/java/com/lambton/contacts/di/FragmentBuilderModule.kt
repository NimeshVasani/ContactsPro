package com.lambton.contacts.di

import com.lambton.contacts.di.addContact.AddContactViewModelModule
import com.lambton.contacts.di.contactDetails.ContactDetailsViewModelModule
import com.lambton.contacts.di.contactsList.ContactsListViewModelModule
import com.lambton.contacts.ui.addContact.AddContactFragment
import com.lambton.contacts.ui.contactDetails.ContactDetailsFragment
import com.lambton.contacts.ui.contactsList.ContactsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector(modules = [ContactsListViewModelModule::class])
    abstract fun contributeContactsFragment() : ContactsListFragment

    @ContributesAndroidInjector(modules = [AddContactViewModelModule::class])
    abstract fun contributeAddContactFragment() : AddContactFragment

    @ContributesAndroidInjector(modules = [ContactDetailsViewModelModule::class])
    abstract fun contributeContactDetailsFragment() : ContactDetailsFragment
}