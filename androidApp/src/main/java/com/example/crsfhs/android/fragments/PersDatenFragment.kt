package com.example.crsfhs.android.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.crsfhs.android.R
import com.example.crsfhs.android.activities.loggedInUserKey
import com.example.crsfhs.android.activities.userLoggedIn
import com.example.crsfhs.android.databinding.FragmentPersDatenBinding
import com.example.crsfhs.android.services.Validation


class PersDatenFragment : Fragment() {
   private lateinit var binding: FragmentPersDatenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPersDatenBinding.inflate(inflater, container, false)

        Toast.makeText(activity, "Vervollständige deine Angaben mit einer Telefonnummer, manchmal" +
                "ist ein Anruf besser!", Toast.LENGTH_SHORT).show()

        binding.passwordText.apply { setText("bla") }

        setupListeners()

        if(!userLoggedIn) { // umleiten auf Login, wenn nicht eingeloggt
            findNavController().navigate(R.id.action_global_fragment_login)
        }

        R.id.meine_pers_daten_vorname


        binding.goButton.setOnClickListener {
           println("GEEEEEEEEEEEEEEEEEHT")
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupListeners() {
        binding.meinePersDatenVornameText.addTextChangedListener(
            TextFieldValidation(
                binding.meinePersDatenVornameText
            )
        )
        binding.meinePersDatenNachnameText.addTextChangedListener(
            TextFieldValidation(
                binding.meinePersDatenNachnameText
            )
        )

        binding.meinePersDatenTelText.addTextChangedListener(
            TextFieldValidation(
                binding.meinePersDatenTelText
            )
        )
/*
        binding.passwordText.addTextChangedListener(
            TextFieldValidation(
                binding.passwordText
            )
        )
*/
        binding.passwordText2.addTextChangedListener(
            TextFieldValidation(
                binding.passwordText2
            )
        )
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                /*
                R.id.usernameEditText -> {
                    validateUsername()
                }*/
                R.id.meine_pers_daten_vorname_text -> {
                    validateFirstname()
                }
                R.id.meine_pers_daten_nachname_text -> {
                    validateLastname()
                }
                /*
                R.id.password_text -> {
                    validatePassword()
                }*/
                R.id.password_text_2 -> {
                    validatePassword2()
                }
            }
        }
    }

    /**
     * 1) Eingabe darf nicht leer sein
     * 2) Eingabe muss aus Buchstaben bestehen
     */
    private fun validateFirstname(): Boolean {
        if (binding.meinePersDatenVornameText.text.toString().trim().isEmpty()) {
            binding.meinePersDatenVorname.error = getString(R.string.requiredInput)
            binding.meinePersDatenVornameText.requestFocus()
            return false
        } else if (!Validation.isValidName(binding.meinePersDatenVornameText.text.toString())) {
            binding.meinePersDatenVorname.error = getString(R.string.invalidFirstname)
            binding.meinePersDatenVornameText.requestFocus()
            return false
        } else {
            binding.meinePersDatenVorname.isErrorEnabled = false
        }
        return true
    }

    /**
     * 1) Eingabe darf nicht leer sein
     * 2) Eingabe muss aus Buchstaben bestehen
     */
    private fun validateLastname(): Boolean {
        if (binding.meinePersDatenNachnameText.text.toString().trim().isEmpty()) {
            binding.meinePersDatenNachname.error = getString(R.string.requiredInput)
            binding.meinePersDatenNachnameText.requestFocus()
            return false
        } else if (!Validation.isValidName(binding.meinePersDatenNachnameText.text.toString())) {
            binding.meinePersDatenNachname.error = getString(R.string.invalidLastname)
            binding.meinePersDatenNachnameText.requestFocus()
            return false
        } else {
            binding.meinePersDatenNachname.isErrorEnabled = false
        }
        return true
    }


    /**
     * 1) Eingabe darf nicht leer sein
     * 2) Passwordlänge muss mindestens 8 sein
     * 3) Passwort muss aus mindestens einer Zahl bestehen
     * 4) Passwort muss aus mindestens einen Groß- und Kleinbuchstaben bestehen
     * 5) Passwort muss aus mindestens einem Sonderzeichen bestehen
     */
    /*private fun validatePassword(): Boolean {
        if (binding.passwordText.text.toString().trim().isEmpty()) {
            binding.passwordContainer.error = getString(R.string.requiredInput)
            binding.passwordText.requestFocus()
            return false
        } else if (binding.passwordText.text.toString().length < 8) {
            binding.passwordContainer.error = getString(R.string.passwordMin8Char)
            binding.passwordText.requestFocus()
            return false
        } else if (!Validation.isStringContainNumber(binding.passwordText.text.toString())) {
            binding.passwordContainer.error = getString(R.string.passwordMin1Number)
            binding.passwordText.requestFocus()
            return false
        } else if (!Validation.isStringLowerAndUpperCase(binding.passwordText.text.toString())) {
            binding.passwordContainer.error = getString(R.string.passwordMin1UpperLower)
            binding.passwordText.requestFocus()
            return false
        } else if (!Validation.isStringContainSpecialCharacter(binding.passwordText.text.toString())) {
            binding.passwordContainer.error = getString(R.string.passwordMin1Special)
            binding.passwordText.requestFocus()
            return false
        } else {
            binding.passwordContainer.isErrorEnabled = false
        }
        return true
    }*/

    /**
     * 1) Eingabe darf nicht leer sein
     * 2) Passwordlänge muss mindestens 8 sein
     * 3) Passwort muss aus mindestens einer Zahl bestehen
     * 4) Passwort muss aus mindestens einen Groß- und Kleinbuchstaben bestehen
     * 5) Passwort muss aus mindestens einem Sonderzeichen bestehen
     */
    private fun validatePassword2(): Boolean {
        if (binding.passwordText2.text.toString().trim().isEmpty()) {
            binding.passwordContainer2.error = getString(R.string.requiredInput)
            binding.passwordText2.requestFocus()
            return false
        } else if (binding.passwordText2.text.toString().length < 8) {
            binding.passwordContainer2.error = getString(R.string.passwordMin8Char)
            binding.passwordText2.requestFocus()
            return false
        } else if (!Validation.isStringContainNumber(binding.passwordText2.text.toString())) {
            binding.passwordContainer2.error = getString(R.string.passwordMin1Number)
            binding.passwordText2.requestFocus()
            return false
        } else if (!Validation.isStringLowerAndUpperCase(binding.passwordText2.text.toString())) {
            binding.passwordContainer2.error = getString(R.string.passwordMin1UpperLower)
            binding.passwordText2.requestFocus()
            return false
        } else if (!Validation.isStringContainSpecialCharacter(binding.passwordText2.text.toString())) {
            binding.passwordContainer2.error = getString(R.string.passwordMin1Special)
            binding.passwordText2.requestFocus()
            return false
        } else {
            binding.passwordContainer2.isErrorEnabled = false
        }
        return true
    }



}
