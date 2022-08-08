package com.lambton.contacts.ui.contactsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.lambton.contacts.MainActivity
import com.lambton.contacts.R
import com.lambton.contacts.databinding.FragmentContactsListBinding
import com.lambton.contacts.di.ViewModelProviderFactory
import com.lambton.contacts.util.OPTIONS
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ContactsListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    private lateinit var viewModel: ContactsListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentContactsListBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this, viewModelProviderFactory)
            .get(ContactsListViewModel::class.java)

        val adapter = ContactsAdapter(requireContext(), ContactsClickListener {
            val id = it.id
            this.findNavController().navigate(
                ContactsListFragmentDirections
                    .actionContactsListFragmentToContactDetailsFragment(id)
            )
        })

        binding.contactList.adapter = adapter

        subscribeUI(adapter)

        binding.fab.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_contactsListFragment_to_addContactFragment, null, OPTIONS)
        }

        return binding.root

    }

    private fun subscribeUI(adapter: ContactsAdapter) {
        viewModel.getAllContacts.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = "Contacts Pro"
    }
}
