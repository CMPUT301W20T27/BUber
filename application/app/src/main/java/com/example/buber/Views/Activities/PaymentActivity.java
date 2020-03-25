package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.buber.R;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class PaymentActivity extends AppCompatActivity {

    private String TAG = "GenerateQRCode";
    private ImageView qrImage;
    private Button start, save;
    private String inputValue;
    private String savePath;
    private Bitmap bitmap;
    private QRGEncoder qrEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //initializations
        qrImage = this.findViewById(R.id.imageViewQRCode);
        savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
        int smallerDimension = 200;  //check this later
        inputValue = "Testing qr generation";
        qrEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);

        //generate the qrCode
        try {
            bitmap = qrEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }

    }

    private void generateQRCode(String data) {

    }
}
