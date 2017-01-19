package com.example.priyamkumar.mcq;

import android.graphics.Color;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    Button fetch,submit;
    CheckBox cb1,cb2,cb3,cb4;
    TextView tv,ans;
    String questionId;
    RequestQueue requestQueue;
    JSONArray students;
    JSONObject student;
    String a,b,c,d;
    static int i=0;
    String insertUrl="http://192.168.1.4/mcqapi/getanswers.php";
    String showUrl="http://192.168.1.4/mcqapi/getquestions.php";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Online Quiz");
       // getActionBar().setIcon(R.drawable.download);
        tv= (TextView) findViewById(R.id.textView);
        ans= (TextView) findViewById(R.id.textView2);
        tv.setMovementMethod(new ScrollingMovementMethod());
        ans.setMovementMethod(new ScrollingMovementMethod());
        fetch= (Button) findViewById(R.id.button);
        submit= (Button) findViewById(R.id.answer);
        cb1= (CheckBox) findViewById(R.id.checkBox);
        cb2= (CheckBox) findViewById(R.id.checkBox2);
        cb3= (CheckBox) findViewById(R.id.checkBox3);
        cb4= (CheckBox) findViewById(R.id.checkBox4);




        requestQueue= Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, showUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    students = response.getJSONArray("questions");
                    //  for(int i=0;i<students.length();i++){       your code}
                     student = students.getJSONObject(0);
                    questionId = student.getString("id");
                    String ques = student.getString("ques");
                    a = student.getString("a");
                   b = student.getString("b");
                 c = student.getString("c");
                    d = student.getString("d");
                    //  Log.d(Tag, "onResponse: chkl raha");

                    tv.setText(ques);
                    cb1.setText(a);
                    cb2.setText(b);
                    cb3.setText(c);
                    cb4.setText(d);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ans.setText("");

                    cb1.setChecked(false);
                cb2.setChecked(false);
                cb3.setChecked(false);
                cb4.setChecked(false);
                if(i<students.length()-1){
                i++;

                }
                else i=0;
                try {

                    //  for(int i=0;i<students.length();i++){       your code}
                    student = students.getJSONObject(i);
                    questionId = student.getString("id");
                    String ques = student.getString("ques");
                     a = student.getString("a");
                     b = student.getString("b");
                     c = student.getString("c");
                     d = student.getString("d");
                    //  Log.d(Tag, "onResponse: chkl raha");

                    tv.setText(ques);
                    cb1.setText(a);
                    cb2.setText(b);
                    cb3.setText(c);
                    cb4.setText(d);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ans.setText("");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject person = null;
                        try {
                            person = new JSONObject(response);
                            String a1= person.getString("a");
                            String b1 = person.getString("b");
                            String c1 = person.getString("c");
                            String d1 = person.getString("d");
                            StringBuffer sb = new StringBuffer("");
                           // ans.setText(a+" "+b+" "+c+" "+d);
                            if(cb1.isChecked())
                                sb.append("1");
                            else
                            sb.append("0");
                            if(cb2.isChecked())
                                sb.append("1");
                            else
                                sb.append("0");
                            if(cb3.isChecked())
                                sb.append("1");
                            else
                                sb.append("0");
                            if(cb4.isChecked())
                                sb.append("1");
                            else
                                sb.append("0");


                               if(sb.toString().equals(a1+b1+c1+d1))
                               {    ans.append("your answer is right"+"\n");
                                   ans.setTextColor(Color.parseColor("#0000ff"));
                               }

                            else {

                                   ans.append("your answer is wrong"+"\n");
                                   //  Toast.makeText(MainActivity.this,sb+":"+a1+b1+c1+d1,Toast.LENGTH_SHORT).show();
                                   ans.append("The correct answer is:" + "\n");
                                   if (a1.equals("1"))
                                       ans.append(a + "\n");
                                   if (b1.equals("1"))
                                       ans.append(b + "\n");
                                   if (c1.equals("1"))
                                       ans.append(c + "\n");
                                   if (d1.equals("1"))
                                       ans.append(d + "\n");
                                   ans.setTextColor(Color.parseColor("#FF0000"));
                               }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("questionid",questionId);

                        return parameters;
                    }
                };
                requestQueue.add(stringRequest);
            }

        });






    }
}
