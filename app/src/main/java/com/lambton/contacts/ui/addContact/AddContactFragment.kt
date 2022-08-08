package com.lambton.contacts.ui.addContact


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.lambton.contacts.MainActivity
import com.lambton.contacts.R
import com.lambton.contacts.data.Contact
import com.lambton.contacts.databinding.FragmentAddContactBinding
import com.lambton.contacts.di.ViewModelProviderFactory
import com.lambton.contacts.util.OPTIONS
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_contact_details.*
import kotlinx.android.synthetic.main.fragment_contact_details.view.*
import timber.log.Timber
import javax.inject.Inject


class AddContactFragment : DaggerFragment() {

    val REQUEST_IMAGE = 100
    var profilePicturePath: String? = null

    private lateinit var binding: FragmentAddContactBinding

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private lateinit var viewModel: AddContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_contact, container, false
        )

        binding.lifecycleOwner = this.viewLifecycleOwner

        viewModel = ViewModelProviders.of(this, factory)[AddContactViewModel::class.java]

        val args = AddContactFragmentArgs.fromBundle(requireArguments())

        if (args.id != -1) {
            val contact = viewModel.getContactById(args.id)
            contact.observe(viewLifecycleOwner, Observer {
                binding.contact = it
            })
        }

        binding.firstNameEditText.doAfterTextChanged {
            viewModel.firstName = it.toString()
        }

        binding.lastNameEditText.doAfterTextChanged {
            viewModel.lastName = it.toString()
        }

        binding.phoneNumberEditText.doAfterTextChanged {
            viewModel.phoneNumber = it.toString()
        }

        binding.emailEditText.doAfterTextChanged {
            viewModel.email = it.toString()
        }

        binding.uploadProfilePictureImageView.setOnClickListener {
            selectProfilePicture()
        }

        binding.saveButton.setOnClickListener {
            if (args.id == -1) {
                Timber.d("Hello this contact name is ${viewModel.firstName} ${viewModel.lastName}")
                viewModel.saveContact(
                    Contact(
                        viewModel.firstName,
                        viewModel.lastName,
                        viewModel.phoneNumber,
                        viewModel.email,
                        profilePicturePath
                    )
                )
                it.findNavController().navigate(
                    AddContactFragmentDirections
                        .actionAddContactFragmentToContactsFragment()
                )
            } else {
                Timber.d("Hello this contact name is ${viewModel.firstName}")
                viewModel.updateContact(
                    Contact(
                        viewModel.firstName,
                        viewModel.lastName,
                        viewModel.phoneNumber,
                        viewModel.email,
                        profilePicturePath,
                        args.id
                    )
                )
                Timber.d("Hello image is ${profilePicturePath}")

                it.findNavController().navigate(
                    AddContactFragmentDirections
                        .actionAddContactFragmentToContactsFragment()
                )
            }
        }

        binding.cancelButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_addContactFragment_to_contactsFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this)
        {
            // handling back button
            findNavController().navigate(
                AddContactFragmentDirections.actionAddContactFragmentToContactsFragment(),
                OPTIONS
            )
        }

        return binding.root
    }

    private fun selectProfilePicture() {
        val intent1 = Intent()
        intent1.type = "image/*"
        intent1.action = Intent.ACTION_GET_CONTENT
        someActivityResultLauncher.launch(Intent.createChooser(intent1, "Choose from here"))
    }

    private var someActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                Log.d("URI ", "$imageUri")

                profilePicturePath = imageUri.toString()
                Log.d("image path ***** ", "$profilePicturePath")
                binding.uploadProfilePictureImageView.setImageURI(imageUri!!)
            }
            Log.d("URI", "Result not ok")
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            Timber.d("URI $imageUri")

            profilePicturePath = imageUri.toString()
            Timber.d("image path ***** ${profilePicturePath}")
            binding.uploadProfilePictureImageView.setImageURI(data?.data)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = "Add New Contact"
    }
}
