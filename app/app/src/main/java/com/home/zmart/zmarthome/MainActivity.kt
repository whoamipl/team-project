package com.home.zmart.zmarthome

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import com.android.volley.AuthFailureError
import android.util.Base64.NO_WRAP
import com.android.volley.VolleyError
import org.json.JSONArray
import org.json.JSONObject
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RetryPolicy




class MainActivity : AppCompatActivity() {
//    val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "http://192.168.0.15:8081/"
        val queue = Volley.newRequestQueue(this)

        button.setOnClickListener {
            val stringRequest = object: StringRequest(Request.Method.GET, url,
                    Response.Listener { response ->

                        Log.d("A", "Response is: " + response)
                        var strResp = response.toString()
                        val jsonObj: JSONObject = JSONObject(strResp)
                        var res = jsonObj.get("status")
                        textView_status.text=res.toString()
                    },
                    Response.ErrorListener {error ->

                        textView_status.text = "ERROR: %s".format(error.toString())
                    })
            {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val credentials = "TheBestTeam:WiesioKiller"
                    val auth = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
                    headers.put("Content-Type", "application/json")
                    headers.put("Authorization", auth)
                    return headers
                }
            }
            queue.add(stringRequest)
        }
    }
}
