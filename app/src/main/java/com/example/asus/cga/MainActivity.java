package com.example.asus.cga;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asus.cga.Entity.Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button ajout;
    ListView lv;
    EditText ide;
    List<Map<String,String>> listitem=new ArrayList<>();
    Map<String,String> m;
    Map<String,String> mx;

    Map<String,String> mw;
    SimpleAdapter simpleAdapter;
    Button recherche;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("test");
        lv=(ListView) findViewById(R.id.listv);
        ajout=(Button) findViewById(R.id.ajout);
        recherche = (Button) findViewById(R.id.rech);
        ide=(EditText) findViewById(R.id.idrech);
        ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,AddContract.class);
                startActivity(i);
            }
        });
        recherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, ide.getText().toString(), Toast.LENGTH_SHORT).show();
                JsonObjectRequest js = new JsonObjectRequest(Request.Method.GET, "https://13a775b4.ngrok.io/cga-web/rest/contract/get/"+ide.getText().toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            listitem.clear();

                            String id=response.getString("id");
                            m=new HashMap<>();

                            //Iterator<?> key=j.keys();
                            m.put("etat",response.getString("etat"));

                            m.put("id",id);
                            Calendar calendar = Calendar.getInstance();
                            Long timeInMillis = Long.valueOf(response.getString("dateSending"));
                            calendar.setTimeInMillis(timeInMillis);
                            String s = (calendar.getTime().toGMTString());
                            m.put("dateSending",s);
                            Calendar calenda = Calendar.getInstance();

                            Long timeInMilli = Long.valueOf(response.getString("startDate"));
                            calenda.setTimeInMillis(timeInMilli);
                            String start = (calenda.getTime().toGMTString());
                            m.put("startDate",start);
                            Calendar calend = Calendar.getInstance();

                            Long timeInMill = Long.valueOf(response.getString("endDate"));
                            calend.setTimeInMillis(timeInMill);
                            String end = (calend.getTime().toGMTString());
                            m.put("endDate",end);
                            m.put("costContract",response.getString("costContract"));
                            listitem.add(m);
                            simpleAdapter=new SimpleAdapter(getApplicationContext(),listitem,R.layout.row_item,new String[]{"id","etat"},new int[]{R.id.lv_id,R.id.lv_etat});
                            lv.setAdapter(simpleAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
error.printStackTrace();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("Content-type", "application/json; charset=utf-8");
                        return map;
                    }

                };
                RequestQueue queuex= Volley.newRequestQueue(MainActivity.this);
                queuex.add(js);
            }
        });


        JsonArrayRequest stringRequest=new JsonArrayRequest(Request.Method.GET, "https://13a775b4.ngrok.io/cga-web/rest/contract/1",null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    for(int i=0;i<response.length();i++){
                        m=new HashMap<>();
                        JSONObject j=response.getJSONObject(i);
                        Iterator<?> key=j.keys();
                        m.put("etat",j.getString("etat"));

                        m.put("id",j.getString("id"));
                  //    m.put("wording",j.getJSONObject("policy").getString("wording"));
                       Calendar calendar = Calendar.getInstance();
                      Long timeInMillis = Long.valueOf(j.getString("dateSending"));
                        calendar.setTimeInMillis(timeInMillis);
                      String s = (calendar.getTime().toGMTString());
                      m.put("dateSending",s);
                        Calendar calenda = Calendar.getInstance();

                        Long timeInMilli = Long.valueOf(j.getString("startDate"));
                        calenda.setTimeInMillis(timeInMilli);
                        String start = (calenda.getTime().toGMTString());
                        m.put("startDate",start);
                        Calendar calend = Calendar.getInstance();

                        Long timeInMill = Long.valueOf(j.getString("endDate"));
                        calend.setTimeInMillis(timeInMill);
                        String end = (calend.getTime().toGMTString());
                        m.put("endDate",end);
                        m.put("costContract",j.getString("costContract"));

                        listitem.add(m);

                    }
                     simpleAdapter=new SimpleAdapter(getApplicationContext(),listitem,R.layout.row_item,new String[]{"id","etat"},new int[]{R.id.lv_id,R.id.lv_etat});
                    lv.setAdapter(simpleAdapter);
                    simpleAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                error.getMessage();

            }
        });
        RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
        queue.add(stringRequest);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(MainActivity.this, "more...", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,Contractdet.class);
                Map<String,String> h=listitem.get(i);
                intent.putExtra("id",h.get("id"));
                intent.putExtra("etat",h.get("etat"));
               intent.putExtra("wording",h.get("wording"));
               intent.putExtra("dateSending",h.get("dateSending"));
                intent.putExtra("startDate",h.get("startDate"));
                intent.putExtra("endDate",h.get("endDate"));
                intent.putExtra("costContract",h.get("costContract"));

                startActivity(intent);
            }
        });


    }

}
