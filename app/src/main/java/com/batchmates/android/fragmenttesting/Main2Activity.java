package com.batchmates.android.fragmenttesting;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.security.Permission;

public class Main2Activity extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener,BlankFragment2.OnFragmentInteractionListener{


    private static final String START_STOP = "StartStop";
    private static final String COUNT_UP = "CountUp";
    private EditText phoneNumber,smsText;
    private TextView fragtwoText;
    private View view;
    private boolean uri=true;
    BlankFragment2 fragmenttwo =new BlankFragment2();
    private int counter=0;
    private BlankFragment2 supportBlank;


    //threading information

    private CountThreader threader;
    private Thread theNewThread;

    LayoutInflater layout;
    PendingIntent sentSMS, recievedSMS;
    BroadcastReceiver sentRe,recieved;
    String sent="SENT";
    String delivered="DELIVERED";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        layout=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=layout.inflate(R.layout.fragment_blank_fragment2,null);
        phoneNumber=(EditText)findViewById(R.id.phoenNumebr);
        smsText=(EditText)findViewById(R.id.textSent);
        fragtwoText=view.findViewById(R.id.count);


        sentSMS=PendingIntent.getBroadcast(this,0,new Intent(sent),0);
        recievedSMS=PendingIntent.getBroadcast(this,0,new Intent(delivered),0);

        BlankFragment fragment=new BlankFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentOne,fragment,START_STOP).commit();

        //BlankFragment2 fragmenttwo =new BlankFragment2();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentTwo,fragmenttwo,COUNT_UP).commit();
        supportBlank =(BlankFragment2)getSupportFragmentManager().findFragmentByTag(COUNT_UP);

//        theNewThread.start();
//        theNewThread.interrupt();

    }

    @Override
    protected void onResume() {
        super.onResume();

        sentRe=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(Main2Activity.this,"OK sent",Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(Main2Activity.this,"Generic Failure",Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(Main2Activity.this,"Radio Off",Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(Main2Activity.this,"No Service",Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(Main2Activity.this,"Null PDU",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        recieved=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(Main2Activity.this,"OK delivered",Toast.LENGTH_SHORT).show();
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(Main2Activity.this,"Not Delivered",Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        };

        registerReceiver(sentRe,new IntentFilter(sent));
        registerReceiver(recieved,new IntentFilter(delivered));
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(sentRe);
        unregisterReceiver(recieved);
    }

    public void callNumber(View view) {

        String number=String.valueOf(phoneNumber.getText());
        String text=String.valueOf(smsText.getText());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},0);
        }
        else {
            SmsManager smsManager = SmsManager.getDefault();
            Log.d("Sent tEXT", "callNumber: Sent Txt: " + "To: " + String.valueOf(phoneNumber.getText()) + " Said: " + String.valueOf(smsText.getText()));
            smsManager.sendTextMessage(number,null,text,sentSMS,recievedSMS);
            //smsManager.sendTextMessage("+12513214263", null, "Code sent", sentSMS, recievedSMS);
        }
    }


    //fragment one interaction
    @Override
    public void onFragmentInteraction(boolean uri) {


        if(uri==true)
        {
            Log.d("FragCount", "onFragmentInteraction: True "+fragtwoText);
            Toast.makeText(this,"start",Toast.LENGTH_SHORT).show();

//            BlankFragment2 supportBlank =(BlankFragment2)getSupportFragmentManager().findFragmentByTag(COUNT_UP);
//            supportBlank.change(counter);
//            CountThreader threader= new CountThreader(fragtwoText,uri,this);
//            Thread theNewThread=new Thread(threader);
//            if (theNewThread.isInterrupted())
//            {
            if (theNewThread!=null)
            {

            }
            else {
                threader = new CountThreader(fragtwoText, uri, this,counter);
                theNewThread = new Thread(threader);
                theNewThread.start();
            }
//            }

        }
        else
        {
            threader.changeRunning();
            theNewThread=null;
            Toast.makeText(this,"stop",Toast.LENGTH_SHORT).show();
        }
    }

    public void update(int newCount)
    {
        counter=newCount;
        supportBlank =(BlankFragment2)getSupportFragmentManager().findFragmentByTag(COUNT_UP);
        supportBlank.change(newCount);
    }
    //Fragment two interaction
    @Override
    public void onFragmentInteraction(Uri uri) {


    }
}
