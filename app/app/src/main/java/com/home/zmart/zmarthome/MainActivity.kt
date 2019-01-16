package com.home.zmart.zmarthome

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Base64
import android.util.Log
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Base64.NO_WRAP
import com.android.volley.*


class MainActivity : AppCompatActivity() {
    //    val client = OkHttpClient()
    private var handler: Handler? = null
    val url = "http://192.168.0.15:8082"
    val username = "TheBestTeam"
    val password = "WiesioKiller"
    var queue: RequestQueue? = null
    var statusValue = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        queue = Volley.newRequestQueue(this)
        handler = Handler()

        handler!!.postDelayed(checkStatus, 1800)


        setButtonListener(url)
    }

    private val checkStatus = object : Runnable {
        override fun run() {
            sendRequestAboutStatus(url + "/status")
            handler!!.postDelayed(this, 1000)

        }
    }

    private fun setButtonListener(url: String) {
        button.setOnClickListener {
            setStatus()

            toggle(url + "/toggle")
        }
    }

    private fun toggle(url: String) {


        val stringRequest = object : StringRequest(Method.GET, url,
                Response.Listener { response ->
                    logResponse(response)
                    statusValue = response.toString()
                    //setStatus()
                },

                Response.ErrorListener { error ->
                    textView_status.text = "ERROR: "+ error.toString()
                })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                return setHeaders()
            }
        }
        queue!!.add(stringRequest)
    }

    private fun setHeaders(): HashMap<String, String> {
        val headers = HashMap<String, String>()
        val credentials = "$username:$password"
        val auth = "Basic " + Base64.encodeToString(credentials.toByteArray(), NO_WRAP)
        headers.put("Content-Type", "application/json")
        headers.put("Authorization", auth)
        return headers
    }

    private fun sendRequestAboutStatus(url: String) {
        val stringRequest = object : StringRequest(Method.GET, url,
                Response.Listener { response ->
                    logResponse(response)
                    statusValue = response.toString()
                    setStatus()
                },

                Response.ErrorListener { error ->
                    textView_status.text = "ERROR: ${error.toString()}"
                })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                return setHeaders()
            }
        }
        queue!!.add(stringRequest)
    }

    private fun logResponse(response: String?) {
        Log.d("RESPONSE", "Response is: " + response)
    }

    private fun setStatus() {
        if (statusValue.equals("on")) {
            textView_status.text = statusValue
            button.setImageResource(R.drawable.button_off)

        } else {
            textView_status.text = statusValue
            button.setImageResource(R.drawable.button_on)

        }
    }

}
