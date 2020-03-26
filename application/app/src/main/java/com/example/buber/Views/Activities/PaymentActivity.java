package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.buber.App;
import com.example.buber.Controllers.ApplicationController;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Rider;
import com.example.buber.Model.Trip;
import com.example.buber.Model.User;
import com.example.buber.R;
import com.example.buber.Views.UIErrorHandler;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.WriterException;

import java.util.Observable;
import java.util.Observer;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class PaymentActivity extends AppCompatActivity implements Observer, ZXingScannerView.ResultHandler, UIErrorHandler {

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
        mScannerView = new ZXingScannerView(this);  //do not move this line
        if(App.getModel().getSessionUser().getType() == User.TYPE.RIDER){
            generateQRCode();
        }
        else if(App.getModel().getSessionUser().getType() == User.TYPE.DRIVER){
            scanQRCode();
        }

    }

    /**
     * Method is called when rider opens this View.
     * Grabs the current trip session, encodes the rider id, driver id, and fair amount of the trip
     * into a bitmap, and converts that bitmap into an imageView to be rendered in
     * activity_payment.xml
     */
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

    /**
     * Called when driver opens this activity. Opens a ZXingScannerView that turns
     * on the users camera and waits for a QR Code to be detected.
     * After Qr Code is detected, run handleResult(Result result)
     */
    public void scanQRCode(){
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

    /**
     * Once a qr code is detected from the camera, grab the data,
     * and receive the money.
     * String[] strValues = [riderID, driverId, fairAmount]
     * @param result is the resulting bytes grabbed from the QR Code
     */
    @Override
    public void handleResult(Result result) {
        qrData = result.getText();
        String[] strData = qrData.split(",");
        Toast.makeText(this, "You have received " + strData[2] + " QR Bucks" ,
                Toast.LENGTH_SHORT).show();
        ApplicationController.deleteRiderCurrentTrip(this);
        finish();
    }

    @Override
    public void update(Observable observable, Object o) {
        Trip sessionTrip = App.getModel().getSessionTrip();
        User.TYPE currentUserType = App.getModel().getSessionUser().getType();

        if(currentUserType == User.TYPE.RIDER){
            if(sessionTrip == null){
                
                this.finish();
            }
        }
    }

    /** onDestroy method destructs activity if it is closed down **/
    @Override
    public void onDestroy() {
        super.onDestroy();
        // THIS CODE SHOULD BE IMPLEMENTED IN EVERY VIEW
        ApplicationModel m = App.getModel();
        m.deleteObserver(this);

    }

    @Override
    public void onError(Error e) {

    }
}
