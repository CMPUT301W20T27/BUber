package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.buber.App;
import com.example.buber.Controllers.ApplicationController;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Trip;
import com.example.buber.Model.User;
import com.example.buber.R;
import com.example.buber.Views.UIErrorHandler;
import com.google.zxing.Result;
import com.google.zxing.WriterException;

import java.util.Observable;
import java.util.Observer;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class PaymentActivity extends AppCompatActivity implements Observer, ZXingScannerView.ResultHandler, UIErrorHandler {

    private String TAG = "GenerateQRCode Failed";
    private ImageView qrImage;
    private String qrData;
    //private String savePath;
    private Bitmap bitmap;
    private QRGEncoder qrEncoder;
    private ZXingScannerView mScannerView;
    private String driverID;  //stored locally before trip is deleted from db
    private static final int PERMISSIONS_REQUEST_ACCESS_CAMERA = 1233;

    /**
     * If we are a rider, generate the qr code and render a view to show that code
     * If we are a driver, open a scanner view(camera) to scan for a qr code
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        App.getModel().addObserver(this);
        driverID = App.getModel().getSessionTrip().getDriverID();  //do this before driver deletes trip
        mScannerView = new ZXingScannerView(this);  //do not move this line
        if(App.getModel().getSessionUser().getType() == User.TYPE.RIDER){
            generateQRCode();
        }
        else if(App.getModel().getSessionUser().getType() == User.TYPE.DRIVER){
            getCameraPermission();
            scanQRCode();
        }

    }

    /**
     * If camera is not enabled for the app. ask user for camera permissions
     *
     */
    private void getCameraPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},PERMISSIONS_REQUEST_ACCESS_CAMERA );
        }
    }

    /**
     * Method is called when rider opens this View.
     * Grabs the current trip session, encodes the rider id, driver id, and fair amount of the trip
     * into a bitmap, and converts that bitmap into an imageView to be rendered in
     * activity_payment.xml
     * The qr code stores data as a string, in the format:
     * String str = "riderID,driverID,QRBucksAmount"
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
        mScannerView.setResultHandler(this);
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
     * and receive the money. Drip then deletes from db, and the rider calls update()
     * String[] strData = [riderID, driverId, fairAmount]
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

    /**
     * After a trip gets deleted, rider calls update, and transitions them to the
     * rating activty to rate their driver
     *
     * @param observable
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {
        Trip sessionTrip = App.getModel().getSessionTrip();
        User.TYPE currentUserType = App.getModel().getSessionUser().getType();

        if(currentUserType == User.TYPE.RIDER){
            if(sessionTrip == null){
                Intent intent = new Intent(this, RatingActivity.class);// New activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("driverID",driverID);
                startActivity(intent);
                finish();
                //this.finish();
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
