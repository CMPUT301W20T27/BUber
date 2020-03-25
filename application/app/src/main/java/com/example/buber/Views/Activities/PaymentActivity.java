package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Trip;
import com.example.buber.Model.User;
import com.example.buber.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.WriterException;

import java.util.Observable;
import java.util.Observer;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class PaymentActivity extends AppCompatActivity implements Observer, ZXingScannerView.ResultHandler {

    private String TAG = "GenerateQRCode";
    private ImageView qrImage;
    private String qrData;
    //private String savePath;
    private Bitmap bitmap;
    private QRGEncoder qrEncoder;
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        App.getModel().addObserver(this);

        if(App.getModel().getSessionUser().getType() == User.TYPE.RIDER){
            generateQRCode();
        }
        else if(App.getModel().getSessionUser().getType() == User.TYPE.DRIVER){
            scanQRCode();
        }

    }

    public void generateQRCode() {
        //initializations, turn trip data into bitmap
        Trip currentTrip = App.getModel().getSessionTrip();
        qrData = currentTrip.getRiderID();
        qrData = qrData + "," + currentTrip.getDriverID();
        qrData = qrData + "," + currentTrip.getFareOffering();
        qrImage = this.findViewById(R.id.imageViewQRCode);
        int smallerDimension = 200;  //check this later
        qrEncoder = new QRGEncoder(qrData, null, QRGContents.Type.TEXT, smallerDimension);

        //turn the bitmap into a qr image
        try {
            bitmap = qrEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            //TODO: better error handling
            Log.v(TAG, e.toString());
        }

        //optional: save the image to phone
        //savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    }

    public void scanQRCode(){
        Log.d("SCANNER","trying to scan");
        mScannerView = new ZXingScannerView(this);
        // Set the scanner view as the content view
        setContentView(mScannerView);
    }


    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Log.d("SCANNER",result.getText());
        qrData = result.getText();
        finish();
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    /** onDestroy method destructs activity if it is closed down **/
    @Override
    public void onDestroy() {
        super.onDestroy();
        // THIS CODE SHOULD BE IMPLEMENTED IN EVERY VIEW
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);
    }

}
