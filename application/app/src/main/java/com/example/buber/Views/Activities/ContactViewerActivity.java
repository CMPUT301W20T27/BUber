package com.example.buber.Views.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.User;
import com.example.buber.R;
import com.example.buber.Views.UIErrorHandler;

import java.util.Observer;

public class ContactViewerActivity extends AppCompatActivity {
    private String email;
    private String phoneNumber;
    private String userID;
    private String userName;
    private TextView userEmailTextView;
    private TextView userPhoneNumberTextView;
    private TextView userNameTextView;
    private Button phoneButton;
    private Button emailButton;
    private static final int PERMISSIONS_REQUEST_ACCESS_CALL_PHONE = 1232;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_viewer_activity);
        Intent i = this.getIntent();
        email = i.getStringExtra("email");
        phoneNumber = i.getStringExtra("phoneNumber");
        userID = i.getStringExtra("ID");
        userName = i.getStringExtra("username");
        userEmailTextView = findViewById(R.id.userEmailTextView);
        userPhoneNumberTextView = findViewById(R.id.userPhoneTextView);
        userNameTextView = findViewById(R.id.userNameTextView);
        phoneButton = findViewById(R.id.phoneButton);
        emailButton = findViewById(R.id.emailBtn);

        // Only show contact information if userId == trip.driverId
        ApplicationModel m = App.getModel();
        if (m.getSessionTrip() != null) {
            if (m.getSessionTrip().getDriverID() == userID) {
                phoneButton.setVisibility(View.VISIBLE);
                emailButton.setVisibility(View.VISIBLE);
            }
        } else {
            phoneButton.setVisibility(View.INVISIBLE);
            emailButton.setVisibility(View.INVISIBLE);
        }

        userNameTextView.setText(userName);
        userEmailTextView.setText(email);
        userPhoneNumberTextView.setText(phoneNumber);

        phoneButton.setOnClickListener(v -> {this.getPhonePermission(); this.handlePhoneRequest();});
        emailButton.setOnClickListener(v -> this.handleEmailRequest());
    }

    public void handlePhoneRequest() {
        new AlertDialog.Builder(this)
                .setTitle("Phone User")
                .setMessage("Do you want to phone this driver?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phoneNumber));
                    if (ActivityCompat.checkSelfPermission(ContactViewerActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(ContactViewerActivity.this,
                                "Unable to make a call at this time. You may not have permissions enabled",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    startActivity(callIntent);
                })
                .setNegativeButton(android.R.string.no, null).show();
    }


    public void handleEmailRequest() {
        new AlertDialog.Builder(this)
                .setTitle("Email User")
                .setMessage("Do you want to email this driver?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        String[] TO = {email};
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "From your Rider");
                        if (emailIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(emailIntent);
                        }
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }


    private void getPhonePermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            //TODO: what happens if they click no
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CALL_PHONE},PERMISSIONS_REQUEST_ACCESS_CALL_PHONE );
        }
    }
}
