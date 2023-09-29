package com.example.chapter_2_eduard

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class HomeActivity : AppCompatActivity() {

    private var buttonLogin: AppCompatButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        buttonLogin = findViewById(R.id.button_login)
        val txtName: EditText = findViewById(R.id.edit_email)

        val radioGroup: RadioGroup = findViewById(R.id.time_options)

        buttonLogin?.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            val radioButton: RadioButton = findViewById(selectedId)

            val selectedOption = radioButton.text.toString()
            val name = txtName.text.toString()

            if (name != ""){
                if (selectedId != -1) {
                    Toast.makeText(this, "Pilihan yang dipilih: $selectedOption", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Pilih salah satu opsi terlebih dahulu", Toast.LENGTH_SHORT).show()
                }

                val i = Intent(this, MainActivity::class.java)
                i.action = Intent.ACTION_SEND
                i.putExtra("nama", name)
                i.putExtra("waktu", selectedOption)
                i.type = "text/plain"


                if (i.resolveActivity(packageManager) != null){
                    startActivity(i)
                }
            } else {
                Toast.makeText(this, "Tolong Di Input Nama Anda!", Toast.LENGTH_SHORT).show()
            }

        }

    }
}