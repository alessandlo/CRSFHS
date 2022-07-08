package com.example.crsfhs.android.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.text.HtmlCompat
import com.example.crsfhs.android.R
import com.example.crsfhs.android.databinding.FragmentFriseursalonBinding


class FriseursalonFragment : Fragment() {

    private lateinit var binding: FragmentFriseursalonBinding
    private lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //btn = findViewById(R.id.sharebtn)




        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriseursalonBinding.inflate(inflater, container, false)



        binding.sharebtn.setOnClickListener{
            // Text rausholen
            val s = binding.hairsalonName.text.toString()

            // Share Intent
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, "Empfehlung geht raus!")
                putExtra(Intent.EXTRA_TEXT, s)
                putExtra(Intent.EXTRA_STREAM,Uri.parse("/drawable/fiverr_reshairvation_r1_02_final.png"))
                type = "*/*"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }

        // Inflate the layout for this fragment
        return binding.root
    }

}
