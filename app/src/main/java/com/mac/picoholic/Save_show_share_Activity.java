package com.mac.picoholic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import io.alterac.blurkit.BlurLayout;
import yuku.ambilwarna.AmbilWarnaDialog;

public class Save_show_share_Activity extends AppCompatActivity {
    private ImageView share_Image;
    private ImageButton share_Btn, rate_us_Btn,setWhatsappDp_Btn;
    private String pathUri,fileName;
    private Toolbar toolbar;
    private BlurLayout blurLayout;
    private int defaultColor;
    private RelativeLayout panel;
    private Bitmap bitmap;
    private ImageView whatsappImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_show_share);


        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        fileName=getIntent().getStringExtra("fileName");

        SharedPreferences getShared = getSharedPreferences("image", MODE_PRIVATE);
        String newImage = getShared.getString("encoded", "");
        share_Image = findViewById(R.id.share_Image);
        share_Btn=findViewById(R.id.share_Btn);
        toolbar=findViewById(R.id.toolbar);
        rate_us_Btn=findViewById(R.id.rate_us_Btn);
        blurLayout=findViewById(R.id.blurLayout);
        setWhatsappDp_Btn=findViewById(R.id.setWhatsappDp_Btn);
        whatsappImage=findViewById(R.id.whatsappImage);
        panel=findViewById(R.id.panel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        blurLayout.startBlur();






        if (!newImage.equalsIgnoreCase("")) {
            byte[] b = Base64.decode(newImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            share_Image.setImageBitmap(bitmap);
        }

        share_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                share_Image.buildDrawingCache();
//                Bitmap finalImage = share_Image.getDrawingCache();
//
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                finalImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//                String path = MediaStore.Images.Media.insertImage(getContentResolver(), finalImage, "Title"+System.currentTimeMillis(), null);
//                Uri uri= Uri.parse(path);
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("image/*");
//                share.putExtra(Intent.EXTRA_STREAM, uri);
//                startActivity(Intent.createChooser(share,"Share via"));
                Drawable drawable=share_Image.getDrawable();
                Bitmap finalImage=((BitmapDrawable)drawable).getBitmap();

                try {
                    File file = new File(getApplicationContext().getExternalCacheDir(), File.separator +"change_back.png");
                    FileOutputStream fOut = new FileOutputStream(file);

                    finalImage.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID +".provider", file);

                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/*");

                    startActivity(Intent.createChooser(intent, "Share image via"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Save_show_share_Activity.this, "file not found", Toast.LENGTH_SHORT).show();
                }

            }
        });

        rate_us_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName()));
                startActivity(intent4);
            }
        });

        setWhatsappDp_Btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

//                Toast.makeText(Save_show_share_Activity.this, "Choose background for whatsapp dp", Toast.LENGTH_LONG).show();
//
//                AmbilWarnaDialog coloPicker = new AmbilWarnaDialog(Save_show_share_Activity.this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
//                    @Override
//                    public void onCancel(AmbilWarnaDialog dialog) {
//
//                    }
//
//                    @Override
//                    public void onOk(AmbilWarnaDialog dialog, int color) {
//                        defaultColor=color;
//                        share_Image.setBackgroundColor(defaultColor);
//                        share_Image.buildDrawingCache();
//                        Bitmap finalImage = share_Image.getDrawingCache();
//
//
//
//                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                        finalImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//                        String path = MediaStore.Images.Media.insertImage(getContentResolver(), finalImage, "Title"+System.currentTimeMillis(), null);
//                        Uri uri= Uri.parse(path);
//                        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
//                        intent.setDataAndType(uri, "image/*");
//                        intent.putExtra("mimeType", "image/*");
//                        startActivity(intent);
//
//                    }
//                });
//                coloPicker.show();
//

                panel.setDrawingCacheEnabled(true);
                panel.buildDrawingCache(true);

                bitmap = Bitmap.createBitmap(panel.getDrawingCache());
                Canvas canvas = new Canvas();
                panel.draw(canvas);
                panel.setDrawingCacheEnabled(false);
                whatsappImage.setImageBitmap(bitmap);

                Drawable drawable=whatsappImage.getDrawable();
                Bitmap finalImage=((BitmapDrawable)drawable).getBitmap();



                try {
                    File file = new File(getApplicationContext().getExternalCacheDir(), File.separator +"color_filter.png");
                    FileOutputStream fOut = new FileOutputStream(file);


                    finalImage.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    Intent intent = new Intent(android.content.Intent.ACTION_ATTACH_DATA);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID +".provider", file);

                        intent.setDataAndType(photoURI, "image/*");
                        intent.putExtra("mimeType", "image/*");
                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);

                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Save_show_share_Activity.this, "file not found", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
