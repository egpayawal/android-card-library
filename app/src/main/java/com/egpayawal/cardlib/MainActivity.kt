package com.egpayawal.cardlib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.egpayawal.card_library.view.CreditCardExpiryTextWatcher
import com.egpayawal.cardlib.databinding.ActivityMainBinding
import com.egpayawal.cardlib.ext.viewBinding

/**
 * Created by Eraño Payawal on 3/19/22.
 * hunterxer31@gmail.com
 */
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.txtInputCardExpiration.addTextChangedListener(CreditCardExpiryTextWatcher(binding.txtInputCardExpiration))
    }

}