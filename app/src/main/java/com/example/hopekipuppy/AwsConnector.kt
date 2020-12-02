package com.example.hopekipuppy

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.internal.Constants

@Suppress("DEPRECATION")
class AwsConnector {
    lateinit var transferUtility: TransferUtility

    fun initializeAwsS3(context: Context) {
        val credentialProvider = CognitoCachingCredentialsProvider(
            context,
            "ap-northeast-2:049c24c6-5d77-43c8-b54d-f010698fe0f9",
            Regions.AP_NORTHEAST_2
        )
        val s3 = AmazonS3Client(credentialProvider)
        val transferUtility = TransferUtility(s3, context)
    }
//    var observer: TransferObserver = transferUtility.upload(
//        "hopekipuppy-bucket",  /* The bucket to upload to */
//        OBJECT_KEY,  /* The key for the uploaded object */
//        MY_FILE /* The file where the data to upload exists */
//    )
}