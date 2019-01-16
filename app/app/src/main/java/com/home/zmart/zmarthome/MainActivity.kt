package com.home.zmart.zmarthome

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Base64.NO_WRAP
import com.android.volley.*
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
//    val client = OkHttpClient()
    private var handler: Handler? = null
    val url = "http://192.168.0.15:8082"
    var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        queue = Volley.newRequestQueue(this)
        handler = Handler()

        handler!!.postDelayed(makeToast, 1800)


        setButtonListener(url)
    }

    private val makeToast = object : Runnable {
        override fun run(){
            toast("Test toast")
            sendRequestAboutStatus(url+"/status")
            handler!!.postDelayed(this, 1000)

        }
    }



    private fun setButtonListener(url: String) {
        button.setOnClickListener {
            toggle(url+"/toggle")
        }
    }

    private fun toggle(url: String) {
        val stringRequest = object : StringRequest(Method.GET, url,
                Response.Listener { response ->
                    Log.d("A", "Response is: " + response)
                    var strResp = response.toString()
                    textView_status.text = strResp
                    if(strResp.equals("on")) {
                        button.text = "Turn off"
                    }else{
                        button.text = "Turn on"

                    }
                },
                Response.ErrorListener { error ->

                    textView_status.text = "ERROR: %s".format(error.toString())
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                val credentials = "TheBestTeam:WiesioKiller"
                val auth = "Basic " + Base64.encodeToString(credentials.toByteArray(), NO_WRAP)
                headers.put("Content-Type", "application/json")
                headers.put("Authorization", auth)
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    private fun sendRequestAboutStatus(url: String) {
        val stringRequest = object : StringRequest(Method.GET, url,
                Response.Listener { response ->

                    Log.d("A", "Response is: " + response)
                    var strResp = response.toString()
                   // val jsonObj: JSONObject = JSONObject(strResp)
                    //var res = jsonObj.get("status")
                    textView_status.text = strResp
                    if(strResp.equals("on")) {
                        button.text = "Turn off"
                    }else{
                        button.text = "Turn on"

                    }
                },
                Response.ErrorListener { error ->

                    textView_status.text = "ERROR: %s".format(error.toString())
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                val credentials = "TheBestTeam:WiesioKiller"
                val auth = "Basic " + Base64.encodeToString(credentials.toByteArray(), NO_WRAP)
                headers.put("Content-Type", "application/json")
                headers.put("Authorization", auth)
                return headers
            }
        }
        queue!!.add(stringRequest)
    }
}
