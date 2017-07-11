package com.batchmates.android.fragmenttesting;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private ImageView pdfView;
    private int currentPage=0;
    private int hight,width;
    private boolean show=false;
    private Dialog dia;

    //TaskStackBuilder task=TaskStackBuilder.create(this);
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        pdfView=(ImageView)findViewById(R.id.pdfViewer);

       // Render();

    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void previus(View view) {


        if(show==false)
        {
            Render();
            show=true;
        }
    }


//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Render(){
        Intent intent = new Intent();
        intent.setClassName("com.adobe.reader", "com.adobe.reader.AdobeReader");
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//        File file=new File("/sdcard/Download/pdf-sample.pdf");
        File file=new File(getFilesDir()+"pdf.pdf");

        try {
            InputStream in=getAssets().open("pdf.pdf");
            OutputStream out=openFileOutput(file.getName(),Context.MODE_WORLD_READABLE);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
//            startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Log.d("HERP", "Render: "+getFilesDir());
//        Uri uri= Uri.fromFile(file);
//        intent.setDataAndType(uri, "application/pdf");
        intent.setDataAndType(
                Uri.parse("file://" + getFilesDir() + "/pdf.pdf"),
                "application/pdf");

        startActivity(intent);
//
//        try{
//            pdfView=(ImageView)findViewById(R.id.pdfViewer);
//        hight=pdfView.getHeight()/2;
//        width=pdfView.getWidth()/2;
//
////        width=1449;
////        hight=2192;
//        Log.d("Width_Hight", "Render: "+width+ " "+hight);
//        Bitmap bitmap=Bitmap.createBitmap(width,hight,Bitmap.Config.ARGB_4444);
//
//        File file=new File("/sdcard/Download/pdf-sample.pdf");
//            //Log.d("Isempty", "Render: "+file);
//        PdfRenderer renderer= null;
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                renderer = new PdfRenderer(ParcelFileDescriptor.open(file,ParcelFileDescriptor.MODE_READ_ONLY));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
//
//        if(currentPage<0)
//            {
//                currentPage=0;
//            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if(currentPage>renderer.getPageCount())
//            {
//                currentPage=renderer.getPageCount()-1;
//            }
//        }
//
//            Matrix matrix=pdfView.getImageMatrix();
//
//            Rect rect=new Rect(0,0,width,hight);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                renderer.openPage(currentPage).render(bitmap,rect,matrix,PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//            }
//            pdfView.setImageMatrix(matrix);
//            pdfView.setImageBitmap(bitmap);
//            pdfView.invalidate();
//        }catch (Exception e){}
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }
    public void boom(View view) {

        dia  =new Dialog(this);
        dia.setContentView(R.layout.custom_dialog);
        dia.setTitle("BoomClicker");
        dia.show();


    }

    public void BoomChickaBoom(View view) {

        dia.dismiss();
    }

    public void defaultDialog(View view) {


        final AlertDialog.Builder diaAlert=new AlertDialog.Builder(this);
        diaAlert.setTitle("Default Dialog").setMessage("This is a default Dialog Box");
        diaAlert.setPositiveButton("Yep", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("Default", "onClick: Yep");
            }
        });
        diaAlert.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("Default", "onClick: Nope");
            }
        });
        AlertDialog show=diaAlert.create();
        show.show();
    }

    public void notifyMe(View view) {

        NotificationCompat.Builder notify= (NotificationCompat.Builder) new NotificationCompat.Builder(this).setContentTitle("Frag Test")
                .setContentText("Love us").setSmallIcon(R.drawable.life_choices);

        Intent headout= new Intent(this,Main2Activity.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(headout);
            PendingIntent pendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

            notify.setContentIntent(pendingIntent);
            notify.setAutoCancel(true);
        }

        NotificationManager notifyMan =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notifyMan.notify(1,notify.build());
    }
}
