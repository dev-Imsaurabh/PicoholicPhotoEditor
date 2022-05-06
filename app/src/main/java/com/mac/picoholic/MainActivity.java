package com.mac.picoholic;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import io.alterac.blurkit.BlurLayout;
import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {
    private ImageView showImageView, panelImageView;
    private RelativeLayout panel;
    private ImageButton showBtn, chooseBackgroundImageBtn, addImageBtn, addText, addTextColor, colorFilterBtn, choose_background_from_cam, deleteElement_Btn, change_background_Btn,infoBtn;
    private ImageView addImageView;
    private Bitmap bitmap, bitmap3, bitmap4;
    private BitmapDrawable bitmapDrawable;
    private Canvas canvas;
    private TextView movableTextView;
    private Toolbar toolbar;
    private SeekBar blendSeekBar, filterSeekbar;
    private final int REQ = 1;
    private float blendValue = 0;
    private int defaultColor;
    private ImageView blurImage;
    private BlurLayout blurLayout;
    private View filter, background;
    private Uri ImageUri;
    private int toggle = 0;
    private Dialog dialog;


    /////// movable Text code initialize
    ///////movable images code initialize
    float[] lastEvent = null;
    float d = 0f;
    float newRot = 0f;
    private boolean isZoomAndRotate;
    private boolean isOutSide;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    float oldDist = 1f;
    private float xCoOrdinate, yCoOrdinate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);

        SharedPreferences getShared = getSharedPreferences("Tour", MODE_PRIVATE);
        String tourValue = getShared.getString("tourValue", "0");

        if(!tourValue.equals("1")){

            dialog=new Dialog(MainActivity.this);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.customize_tour_layout);
            dialog.setCancelable(false);
            Button okBtn = dialog.findViewById(R.id.okBtn);
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = getSharedPreferences("Tour", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tourValue", "1");
                    editor.apply();

                    startActivity(new Intent(MainActivity.this,Info_and_working_Activity.class));
                    dialog.dismiss();

                }
            });

            Button noBtn = dialog.findViewById(R.id.noBtn);
            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = getSharedPreferences("Tour", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tourValue", "1");
                    editor.apply();

                    dialog.dismiss();

                }
            });


            dialog.show();


        }





        panel = (RelativeLayout) findViewById(R.id.panel);
        showImageView = (ImageView) findViewById(R.id.showImageView);
        showBtn = findViewById(R.id.showBtn);
        chooseBackgroundImageBtn = findViewById(R.id.chooseBackgroundImageBtn);
        choose_background_from_cam = findViewById(R.id.choose_background_from_cam);
        addImageBtn = findViewById(R.id.addImageBtn);
        toolbar = findViewById(R.id.toolbar);
        addImageView = findViewById(R.id.addImageView);
        panelImageView = findViewById(R.id.panelImageView);
        addText = findViewById(R.id.addText);
        movableTextView = findViewById(R.id.movableTextView);
        blurImage = findViewById(R.id.blurImage);
        blurLayout = findViewById(R.id.blurLayout);
        blendSeekBar = findViewById(R.id.blendSeekBar);
        addTextColor = findViewById(R.id.addTextColor);
        infoBtn=findViewById(R.id.infoBtn);
        filter = findViewById(R.id.filter);
        colorFilterBtn = findViewById(R.id.colorFilterBtn);
        filterSeekbar = findViewById(R.id.filterSeekBar);
        defaultColor = ContextCompat.getColor(MainActivity.this, R.color.purple_200);
        blendSeekBar.setProgress(100);
        filterSeekbar.setProgress(40);
        deleteElement_Btn = findViewById(R.id.deleteElement_Btn);
        background = findViewById(R.id.background);
        change_background_Btn = findViewById(R.id.change_background_Btn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Info_and_working_Activity.class));
            }
        });


        chooseBackgroundImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle = 1;
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ);

            }
        });

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle = 2;
                addImageView = new ImageView(getApplicationContext());
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(250, 250);
                lp.addRule(RelativeLayout.BELOW);
                addImageView.setLayoutParams(lp);
                addImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                addImageView.setVisibility(View.GONE);
                addImageView.setBackground(getDrawable(R.drawable.border1));
                panel.addView(addImageView);
                addImageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v2, MotionEvent event) {

                        ImageView v = (ImageView) v2;
                        v.bringToFront();
                        onTouch2(v, event);

                        return true;
                    }
                });


                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ);

            }

        });


        choose_background_from_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle = 3;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(MainActivity.this);
            }
        });


        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                show();


            }
        });

        change_background_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog colorPicker2 = new AmbilWarnaDialog(MainActivity.this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {


                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        defaultColor = color;

                        background.setBackgroundColor(defaultColor);


                    }
                });
                colorPicker2.show();

            }
        });

        colorFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AmbilWarnaDialog colorPicker1 = new AmbilWarnaDialog(MainActivity.this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {


                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        defaultColor = color;

                        filter.setBackgroundColor(defaultColor);
                        filter.setAlpha(0.25f);


                    }
                });
                colorPicker1.show();
            }
        });

        filterSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                float alphaValue = i / 100f;

                filter.setAlpha(alphaValue);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View add_custom_text_layout = getLayoutInflater().inflate(R.layout.add_custom_text_lauout, null);
                builder.setView(add_custom_text_layout);
                builder.setTitle("Add text");
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        EditText customText = add_custom_text_layout.findViewById(R.id.customText);
                        movableTextView = new TextView(MainActivity.this);
                        RelativeLayout.LayoutParams textalignment = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        textalignment.addRule(RelativeLayout.CENTER_IN_PARENT);
                        movableTextView.bringToFront();
                        movableTextView.setLayoutParams(textalignment);
                        movableTextView.setTextSize(20);
                        movableTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        movableTextView.setMaxLines(3);
                        movableTextView.setBackground(getDrawable(R.drawable.border1));
                        panel.addView(movableTextView);
                        movableTextView.setText(customText.getText().toString());
                        movableTextView.setTextColor(Color.WHITE);
                        movableTextView.setVisibility(View.VISIBLE);
                        movableTextView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {

                                TextView v = (TextView) view;
                                v.bringToFront();
                                viewTransformation(v, motionEvent);

                                return true;
                            }
                        });


                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        movableTextView.setVisibility(View.GONE);

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


    }


    private void show() {


        panel.setDrawingCacheEnabled(true);
        panel.buildDrawingCache(true);

        bitmap3 = Bitmap.createBitmap(panel.getDrawingCache());
        canvas = new Canvas();
        panel.draw(canvas);
        panel.setDrawingCacheEnabled(false);
        showImageView.setImageBitmap(bitmap3);


        saveImage();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ImageUri = uri;
//            try {
//                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            panelImageView.setImageBitmap(bitmap);
//            blurImage.setImageBitmap(bitmap);
//
//            blurLayout.startBlur();
            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(ImageUri)
                    .start(this);


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (toggle == 1) {
                    panelImageView.setImageBitmap(bitmap);
                    blurImage.setImageBitmap(bitmap);

                    blurLayout.startBlur();
                    blurLayout.setBackgroundColor(0);
                    toggle = 0;

                } else if (toggle == 2) {

                    addImageView.setImageBitmap(bitmap);
                    addImageView.setVisibility(View.VISIBLE);
                    toggle = 0;

                } else if (toggle == 3) {

                    panelImageView.setImageBitmap(bitmap);
                    blurImage.setImageBitmap(bitmap);

                    blurLayout.startBlur();
                    toggle = 0;

                }

            }
        }

    }


    private void saveImage() {

        bitmapDrawable = (BitmapDrawable) showImageView.getDrawable();
        bitmap4 = bitmapDrawable.getBitmap();
        saveImageToGallery(bitmap4);

    }

    private void saveImageToGallery(Bitmap bitmap4) {

        OutputStream fos;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                String fileName = "Image_" + System.currentTimeMillis() + ".jpg";
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "images/jpg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + File.separator + "Picoholic Photo Editor");
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bitmap4.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Objects.requireNonNull(fos);

                sendImagetoshareActivity(bitmap4,imageUri,fileName);


                Toast.makeText(MainActivity.this, "saved in > Internal storage/DCIM/Picoholic Photo Editor", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {

            Toast.makeText(MainActivity.this, "Error\n" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void sendImagetoshareActivity(Bitmap bitmap4, Uri imageUri, String fileName) {
        String encodedImage = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap4.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();

            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }


        SharedPreferences sharedPreferences = getSharedPreferences("image", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("encoded", encodedImage);
        editor.apply();

        Intent intent = new Intent(MainActivity.this, Save_show_share_Activity.class);
        intent.putExtra("pathUri",imageUri.getPath());
        intent.putExtra("imageName",fileName);
        startActivity(intent);

    }

    ////code started movable textview and imageView/////////////////////////////////////////////////////////////

    private void viewTransformation(TextView textView, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xCoOrdinate = textView.getX() - event.getRawX();
                yCoOrdinate = textView.getY() - event.getRawY();

                start.set(event.getX(), event.getY());
                isOutSide = false;
                mode = DRAG;
                lastEvent = null;
                textView.setBackground(getDrawable(R.drawable.border1));
                blendSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        blendValue = i / 100f;
                        textView.setAlpha(blendValue);


                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                ////////for color changing

                addTextColor.setOnClickListener(view -> {
                    AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(MainActivity.this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                        @Override
                        public void onCancel(AmbilWarnaDialog dialog) {


                        }

                        @Override
                        public void onOk(AmbilWarnaDialog dialog, int color) {

                            defaultColor = color;
                            textView.setTextColor(defaultColor);

                        }
                    });
                    colorPicker.show();
                });
                ////color changing finish

                deleteElement_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        panel.removeView(textView);
                    }
                });


                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                }

                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
                isZoomAndRotate = false;
                if (mode == DRAG) {
                    float x = event.getX();
                    float y = event.getY();
                    textView.setBackground(null);

                }
            case MotionEvent.ACTION_OUTSIDE:
                isOutSide = true;
                mode = NONE;
                lastEvent = null;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isOutSide) {
                    if (mode == DRAG) {
                        isZoomAndRotate = false;
                        textView.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                    }
                    if (mode == ZOOM && event.getPointerCount() == 2) {
                        float newDist1 = spacing(event);
                        if (newDist1 > 10f) {
                            float scale = newDist1 / oldDist * textView.getScaleX();
                            textView.setScaleX(scale);
                            textView.setScaleY(scale);

                        }
                        if (lastEvent != null) {
                            newRot = rotation(event);
                            textView.setRotation((float) (textView.getRotation() + (newRot - d)));
                        }
                    }
                }
                break;
        }
    }


    //////////////////////////////////////////////////////////////////////////code End for movable text and image

    private void onTouch2(ImageView imageView, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xCoOrdinate = imageView.getX() - event.getRawX();
                yCoOrdinate = imageView.getY() - event.getRawY();

                start.set(event.getX(), event.getY());
                isOutSide = false;
                mode = DRAG;
                lastEvent = null;
                imageView.setBackground(getDrawable(R.drawable.border1));

                blendSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        blendValue = i / 100f;
                        imageView.setAlpha(blendValue);


                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                deleteElement_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        panel.removeView(imageView);
                    }
                });


                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                }

                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
                isZoomAndRotate = false;
                if (mode == DRAG) {
                    float x = event.getX();
                    float y = event.getY();
                    imageView.setBackground(null);

                }
            case MotionEvent.ACTION_OUTSIDE:
                isOutSide = true;
                mode = NONE;
                lastEvent = null;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;

                break;
            case MotionEvent.ACTION_MOVE:
                if (!isOutSide) {
                    if (mode == DRAG) {
                        isZoomAndRotate = false;
                        imageView.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                    }
                    if (mode == ZOOM && event.getPointerCount() == 2) {
                        float newDist1 = spacing(event);
                        if (newDist1 > 10f) {
                            float scale = newDist1 / oldDist * imageView.getScaleX();
                            imageView.setScaleX(scale);
                            imageView.setScaleY(scale);


                        }
                        if (lastEvent != null) {
                            newRot = rotation(event);
                            imageView.setRotation((float) (imageView.getRotation() + (newRot - d)));


                        }
                    }
                }

                break;
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (int) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}