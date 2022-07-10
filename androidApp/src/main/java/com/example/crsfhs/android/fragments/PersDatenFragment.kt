package com.example.crsfhs.android.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.crsfhs.android.R
import com.example.crsfhs.android.activities.loggedInUserKey
import com.example.crsfhs.android.activities.userLoggedIn
import com.example.crsfhs.android.api.DbApi
import com.example.crsfhs.android.api.UpdateUserDetails
import com.example.crsfhs.android.api.UpdateUserDetailsSet
import com.example.crsfhs.android.api.UserDetails
import com.example.crsfhs.android.databinding.FragmentPersDatenBinding
import com.example.crsfhs.android.services.Encryption.toSHA
import com.example.crsfhs.android.services.Validation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersDatenFragment : Fragment() {
    private lateinit var binding: FragmentPersDatenBinding
    val tRetrofitData = DbApi.retrofitService.getUser(loggedInUserKey.toString())
    private lateinit var currentPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //unsubscribe()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersDatenBinding.inflate(inflater, container, false)

        setupListeners()

        if (!userLoggedIn) { // umleiten auf Login, wenn nicht eingeloggt
            findNavController().navigate(R.id.action_global_fragment_login)
        } else {
            println("getUser(): $tRetrofitData")

            tRetrofitData.enqueue(object : Callback<UserDetails> {
                override fun onResponse(call: Call<UserDetails>, response: Response<UserDetails>) {

                    //println("getUser(): Response: " + response.body()!!)
                    //println("getUser(): email. " + response.body()!!.email)

                    when (response.body()!!.gender) {
                        "männlich" -> binding.genderSelection.setText("männlich", false)
                        "weiblich" -> binding.genderSelection.setText("weiblich", false)
                        "divers" -> binding.genderSelection.setText("divers", false)
                    }

                    currentPassword = response.body()!!.password
                    println("getUser(): currentPassword: $currentPassword")

                    binding.meinePersDatenVornameText.apply { setText(response.body()!!.firstname) }
                    binding.meinePersDatenNachnameText.apply { setText(response.body()!!.lastname) }
                }

                override fun onFailure(call: Call<UserDetails>, t: Throwable) {
                    Log.e("Hairdresser", "onFailure: " + t.message)
                }
            })
        }
        // testing


        binding.goButton.setOnClickListener {
            if (isValidate()) {
                updtUser()
                //binding.passwordText2.apply { setText("") }
            }

            if (currentPassword == binding.passwordText.text.toString().toSHA()) {
                //println("getUser(): GEEEEEEEEEEEEEEEEEHT")
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }


    private fun updtUser() {
        var pw = currentPassword
        //var gender = ""
        if (binding.passwordText2.text.toString().isNotEmpty()) {
            pw = binding.passwordText2.text.toString().toSHA()
        }

        val retrofitData = DbApi.retrofitService.updateUser(
            loggedInUserKey.toString(),
            UpdateUserDetailsSet(
                null,
                UpdateUserDetails(
                    firstname = binding.meinePersDatenVornameText.text.toString(),
                    lastname = binding.meinePersDatenNachnameText.text.toString(),
                    phone = binding.meinePersDatenTelText.text.toString(),
                    gender = binding.genderSelection.text.toString(),
                    password = pw
                )
            )
        )

        retrofitData.enqueue(object : Callback<UpdateUserDetailsSet?> {
            override fun onResponse(
                call: Call<UpdateUserDetailsSet?>,
                response: Response<UpdateUserDetailsSet?>
            ) {
                Log.i("Update user", "geht klar")
                Toast.makeText(activity, "Daten wurden aktualisiert!", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onFailure(call: Call<UpdateUserDetailsSet?>, t: Throwable) {
                println("Daten wurden nicht aktualisiert")
            }
        })


    }

    private fun isValidate(): Boolean =
        validateFirstname() && validateLastname() && validatePassword() && validatePassword2()

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
                R.id.meine_pers_daten_vorname_text -> {
                    validateFirstname()
                }
                R.id.meine_pers_daten_nachname_text -> {
                    validateLastname()
                }

                R.id.password_text -> {
                    validatePassword2()
                }
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
    private fun validatePassword(): Boolean {
        if (binding.passwordText.text.toString()
                .isNotEmpty() && currentPassword != binding.passwordText.text.toString().toSHA()
        ) {
            binding.passwordContainer.error = "Falsches Passwort!"
            binding.passwordText.requestFocus()
            return false
        } else {
            binding.passwordContainer.isErrorEnabled = false
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
    private fun validatePassword2(): Boolean {
        if (binding.passwordText.text.toString().isNotEmpty()) {
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
        } else {
            binding.passwordContainer2.isErrorEnabled = false
        }
        return true
    }
}
