//Liam Salass #20229595
package com.example.guessmaster;

import java.util.Random;

import com.example.guessmaster.R;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.DialogInterface;



public class GuessMasterActivity extends AppCompatActivity {
    //Setting up access to layout
    private TextView entityName;
    private TextView ticketsum;
    private Button guessButton;
    private EditText userIn;
    private Button btnclearContent;
    private String user_input;
    private ImageView entityImage;
    private String answer;

    //Info on current Entity in use
    private int entityId;
    private String entName;
    private Entity currentEntity;

    //used to store all entities and ticket info
    private int numOfEntities;
    private final Entity[] entities;
    private int ticketsWon = 0;
    private int totaltik = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_activity);

        //Further setting up access to layout
        guessButton = (Button) findViewById(R.id.btnGuess);
        userIn = (EditText) findViewById(R.id.guessinput);
        ticketsum = (TextView) findViewById(R.id.ticket);
        entityName = (TextView) findViewById(R.id.entityName);
        btnclearContent = (Button) findViewById(R.id.btnClear);
        entityImage = (ImageView) findViewById(R.id.entityImage);

        //Loading in entities
        Politician jTrudeau = new Politician("Justin Trudeau", new Date("December", 25, 1971), "Male", "Liberal", 0.25);////
        Singer cDion = new Singer("Celine Dion", new Date("March", 30, 1961), "Female", "La voix du bon Dieu", new Date("November", 6, 1981), 0.5);////
        Person myCreator = new Person("My Creator", new Date("September", 1, 2000), "Female", 1);////
        Country usa = new Country("United States", new Date("July", 4, 1776), "Washinton D.C.", 0.1);////

        //Setting up game to run main activity
        new GuessMasterActivity();
        //Adding entities
        addEntity(jTrudeau);
        addEntity(cDion);
        addEntity(myCreator);
        addEntity(usa);
        //Picking an random entity and displaying the welcome message
        changeEntity();
        welcomeToGame(currentEntity);

        //Change entity button
        btnclearContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEntity();
            }
        });

        //Submit guess button
        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playGame(currentEntity);
            }
        });


    }

    //Changes current entity
    public void changeEntity() {
        userIn.getText().clear();
        entityId = genRandomentityId();
        Entity entity = entities[entityId];
        entName = entity.getName();
        entityName.setText(entName);
        ImageSetter();
        currentEntity = entity;
    }

    //Changes image to match current entity
    public void ImageSetter() {
        String name = entName;
        switch (name) {
            case "Justin Trudeau":
                entityImage.setImageResource(R.drawable.justint);
                break;
            case "Celine Dion":
                entityImage.setImageResource(R.drawable.celidion);
                break;
            case "My Creator":
                entityImage.setImageResource(R.drawable.ic_check_circle_black_24dp);
                break;
            case "United States":
                entityImage.setImageResource(R.drawable.usaflag);
                break;
        }
    }

    //provides welcome message to game
    public void welcomeToGame(Entity entity) {
        AlertDialog.Builder welcomealert = new AlertDialog.Builder(GuessMasterActivity.this);
        welcomealert.setTitle("GuessMaster_Game_v3");
        welcomealert.setMessage(entity.welcomeMessage());
        welcomealert.setCancelable(false);
        welcomealert.setNegativeButton("START_GAME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Game_is_Starting...Enjoy", Toast.LENGTH_SHORT).show();
            }
        });
        welcomealert.show();
    }

    //Sets up initial variables when starting game
    public GuessMasterActivity() {
        numOfEntities = 0;
        entities = new Entity[10];
        totaltik = 0;
    }

    //Adds entities to entities array
    public void addEntity(Entity entity) {
//		entities[numOfEntities++] = new Entity(entity);
//		entities[numOfEntities++] = entity;//////
        entities[numOfEntities++] = entity.clone();
    }

    //Calls playgame if only entitId passed
    public void playGame(int entityId) {
        Entity entity = entities[entityId];
        playGame(entity);
    }

    //Main play game code
    public void playGame(Entity entity) {
        //Takes in user input from userIn editText
        entityName.setText(entity.getName());
        answer = userIn.getText().toString();
        answer = answer.replace("\n", "").replace("\r","");
        Date date = new Date(answer);


        if (date.precedes(entity.getBorn())) {
            //Alert Dialog for early guess
            AlertDialog.Builder alert = new AlertDialog.Builder(GuessMasterActivity.this);
            alert.setTitle("Incorrect");
            alert.setMessage("Try a later date.");
            alert.setNegativeButton("Ok", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.show();
        } else if (entity.getBorn().precedes(date)) {
            //Alert Dialog for late guess
            AlertDialog.Builder alert = new AlertDialog.Builder(GuessMasterActivity.this);
            alert.setTitle("Incorrect");
            alert.setMessage("Try an earlier date.");
            alert.setNegativeButton("Ok", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.show();
        } else { //In case of correct guess
            //Update tickets won
            ticketsWon = entity.getAwardedTicketNumber();
            totaltik += ticketsWon;
            String total = (new Integer(totaltik)).toString();

            //AlertDialog for if you had a correct guess
            AlertDialog.Builder alert = new AlertDialog.Builder(GuessMasterActivity.this);
            alert.setTitle("You won");
            alert.setMessage("BINGO! \n" + entity.closingMessage());
            alert.setCancelable(false);
            alert.setNegativeButton("Continue", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getBaseContext(), "You got" + total, Toast.LENGTH_SHORT).show();
                    continueGame(); //Changes entity
                }
            });
            ticketsum.setText("Total Tickets" + total);
            alert.show();
        }
    }

    public void continueGame(){ //Changes current entity and clears userIn
        userIn.getText().clear();
        changeEntity();
    }

    // Picks random entity and then calls main playgame method
    public void playGame() {
        int entityId = genRandomentityId();
        playGame(entityId);
    }

    //Returns Id of entity in arr not out of bounds
    public int genRandomentityId() {
        Random randomNumber = new Random();
        return randomNumber.nextInt(numOfEntities);
    }

}
