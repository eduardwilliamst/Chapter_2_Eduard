package com.example.chapter_2_eduard

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

class MainActivity : AppCompatActivity() {

    private var buttonCalculate: AppCompatButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent: Intent = intent

        val nama: String? = intent.getStringExtra("nama")
        val waktu: String? = intent.getStringExtra("waktu")

        val textViewName = findViewById<TextView>(R.id.textview_name)
        val textViewTime = findViewById<TextView>(R.id.textview_time)

        textViewName.text = "Welcome $nama"
        textViewTime.text = "Waktu $waktu"

        val radioGroupTip: RadioGroup = findViewById(R.id.tip_options)
        val inputEditText = findViewById<EditText>(R.id.cost_of_service)
        val roundUpSwitch = findViewById<Switch>(R.id.round_up_switch)
        val buttonCalculate = findViewById<Button>(R.id.calculate_button)

        buttonCalculate?.setOnClickListener {
            val selectedId = radioGroupTip.checkedRadioButtonId
            val radioButtonTip: RadioButton = findViewById(selectedId)
            val selectedOption = radioButtonTip.text.toString()

            val inputText = inputEditText.text.toString()

            try {
                var inputNumber = inputText.toDouble()
                val roundedNumber = if (roundUpSwitch.isChecked) {
                    Math.round(inputNumber).toInt()
                } else {
                    inputNumber
                }
                inputEditText.setText(roundedNumber.toString())

                val cost = inputEditText.text.toString()
                val jam: Int?
                val services: Int?

                if (waktu == "1 Hour"){
                    jam = 1
                } else if (waktu == "3 Hour"){
                    jam = 3
                } else {
                    jam = 12
                }

                if (selectedOption == "Amazing (20%)"){
                    services = 20
                } else if (selectedOption == "Good (18%)"){
                    services = 18
                } else {
                    services = 15
                }

                val costperjam = (cost.toDouble() * jam)
                val costperservice = (costperjam * services / 100.0)

                val textCost:String = costperjam.toString()
                val textServices:String = costperservice.toString()
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Pembayaran Tip")

                val text1 = "Harga Sewa per jam: $cost" // Gantilah dengan nilai yang sesuai
                val text2 = "Lama Bermain: $jam Jam"
                val text3 = "Total Sewa: $textCost"
                val text4 = "Service yang dipilih: $selectedOption"
                val text5 = "Total yang harus dibayar: $textServices"

                val message = "$text1\n$text2\n$text3\n$text4\n" +
                        "$text5\n"
                alertDialog.setMessage(message)

                val qrCodeBitmap = generateQRCode("Terima Kasih sudah membayar sebesar "+textServices)

                val imageView = ImageView(this)
                imageView.setImageBitmap(qrCodeBitmap)
                alertDialog.setView(imageView)
                alertDialog.setPositiveButton("OK") { dialog, _ ->

                    dialog.dismiss()
                }
                alertDialog.show()

            } catch (e: NumberFormatException) {

                Toast.makeText(this, "Input tidak valid", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun generateQRCode(data: String): Bitmap? {
        val width = 400
        val height = 400
        val bitMatrix: BitMatrix
        try {
            bitMatrix = MultiFormatWriter().encode(
                data,
                BarcodeFormat.QR_CODE,
                width,
                height
            )
        } catch (e: WriterException) {
            e.printStackTrace()
            return null
        }

        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }
}