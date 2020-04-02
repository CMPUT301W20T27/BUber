package com.example.buber.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.buber.App;
import com.example.buber.Controllers.ApplicationController;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.R;

import java.util.Observable;
import java.util.Observer;

public class RatingActivity extends AppCompatActivity implements Observer {
    private Button btnThumbsUp;
    private Button btnThumbsDown;
    private Button btnSkip;
    private String driverID;

    /**
     * Renders a view with 3 buttons. One to rate thumbs up, one to rate thumbs down, and one
     * to skip.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        App.getModel().addObserver(this);
        driverID =  getIntent().getStringExtra("driverID");
        btnThumbsUp = this.findViewById(R.id.buttonThumbsUp);
        btnThumbsDown = this.findViewById(R.id.buttonThumbsDown);
        btnSkip = this.findViewById(R.id.buttonSkip);
    }

    /**
     * if we click thumbs up/down, call controller method to adjust rating to the driver
     * Otherwise(we clicked skip) go back to the map activity where the trip is finished
     * @param v The button view that is pressed
     */
    public void handleRatingClick(View v){
        if(v.getId()==R.id.buttonThumbsUp){
            ApplicationController.updateDriverRating(this,
                    driverID,true);
            Toast.makeText(this, "Driver rating updated!", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else if(v.getId()== R.id.buttonThumbsDown){
            ApplicationController.updateDriverRating(this,
                    driverID,false);
            Toast.makeText(this, "Driver rating updated!", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else{
            this.finish();
        }
    }

    @Override
    public void update(Observable o, Object arg) {

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
