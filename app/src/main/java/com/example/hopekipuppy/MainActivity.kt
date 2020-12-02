package com.example.hopekipuppy

import android.os.Bundle
import android.telephony.mbms.MbmsErrors
import androidx.appcompat.app.AppCompatActivity
import com.example.hopekipuppy.login.Login
import org.json.JSONException


class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var login : Login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {AwsConnector().initializeAwsS3(this)}
        catch (e: com.amazonaws.AmazonClientException) {
            e.printStackTrace()
        }

    }
}