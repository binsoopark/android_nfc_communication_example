package com.soobinpark.example.nfccommunication

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.*
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.UnsupportedEncodingException

class NfcActivity : AppCompatActivity() {
    companion object {
        val TAG: String = NfcActivity::class.java.simpleName
    }
    private var adapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)

        initNfcAdapter()
    }

    private fun initNfcAdapter() {
        val nfcManager = getSystemService(Context.NFC_SERVICE) as NfcManager
        adapter = nfcManager.defaultAdapter
    }

    override fun onResume() {
        super.onResume()
        enableNfcForegroundDispatch()
    }

    override fun onPause() {
        super.onPause()
        disableNfcForegroundDispatch()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // 아래처럼 tag data만 가져와도 쿠폰 코드 값은 가져올 수 있다.
        val ndefMessagesFromIntent = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        ndefMessagesFromIntent?.let {
            val ndefMessage = ndefMessagesFromIntent[0] as NdefMessage
            val cardRecord = ndefMessage.records[0]
            try {
                val tagData = ByteUtils.ReadRecordByCharEnc(cardRecord.payload)
                Log.d(TAG, "Tag Data: $tagData")
                Toast.makeText(this, "Tag Data: $tagData", Toast.LENGTH_LONG).show()
            } catch (e: UnsupportedEncodingException) {
                Log.e(TAG, "onNewIntent: Unsupported record exception", e)
                return
            }
        }

        // 추가정보 추출을 위한 코드
//        val tagFromIntent = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
//        try {
//            tagInfo = tagFromIntent?.let { NdefTagInfo(it) }
//
//            Log.d(TAG, "Tag tapped: ${tagInfo?.tagId}")
//        } catch (e: FormatException) {
//            Log.e(TAG, "Unsupported tag tapped", e)
//            return
//        }
    }

    fun getNdefTagInfo(tag: Tag) {
        tag.techList.filter {
            it == Ndef::class.java.canonicalName
        }
    }

    private fun enableNfcForegroundDispatch() {
        try {
            val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val nfcPendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
            adapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "enableNfcForegroundDispatch: 1", e)
        }
    }

    private fun disableNfcForegroundDispatch() {
        try {
            adapter?.disableForegroundDispatch(this)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "disableNfcForegroundDispatch: 1", e)
        }
    }
}