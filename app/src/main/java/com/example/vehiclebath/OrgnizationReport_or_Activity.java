package com.example.vehiclebath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vehiclebath.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OrgnizationReport_or_Activity extends AppCompatActivity {

    private TextView name_OR,phone_OR,password_OR,email_OR;
    private Button organization_OR;
    private String userPhone ="";
    private ProgressDialog loadingBar;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orgnization_report_or_);

        userPhone = getIntent().getStringExtra("UserPhone");

        name_OR = (TextView) findViewById(R.id.user_name_OR);
        phone_OR = (TextView) findViewById(R.id.user_phone_OR);
        password_OR = (TextView) findViewById(R.id.user_password_OR);
        email_OR = (TextView) findViewById(R.id.user_email_OR);

        organization_OR = (Button) findViewById(R.id.btnReportSubmit_OR);

        loadingBar = new ProgressDialog(this);

        getUserDetailsToDisplay(userPhone);


        //-------------------------------Alert dialogue--------------------------------------//
//        builder = new AlertDialog.Builder(this);
        organization_OR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateReport();
//
//                //Setting message manually and performing action on button click
//                builder.setMessage("Do you want to close this application ?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                finish();
//                               // CreateReport();
//                                Toast.makeText(getApplicationContext(), "you choose yes action for alertbox",
//                                        Toast.LENGTH_SHORT).show();
//
//
//
//                                //--------------------------Notification---------------------------//
//
//
//                                String messsage1 = "Vehicle Bath have submitted a report";
//                                String messsage2 = "You have submitted a report on customer";
//                                NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                                        OrgnizationReport_or_Activity.this
//                                )
//                                        .setSmallIcon(R.drawable.logo1)
//                                        .setContentTitle("Vehicle Bath")
//                                        .setContentText(messsage1)
//                                        .setContentText(messsage2)
//                                        .setAutoCancel(true);
//
//                                Intent intent = new Intent(OrgnizationReport_or_Activity.this,OrgnizationReport_or_Activity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                intent.putExtra("message",messsage1);
//                                intent.putExtra("message",messsage2);
//
//                                PendingIntent pendingIntent = PendingIntent.getActivity(OrgnizationReport_or_Activity.this,
//                                        0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//
//                                builder.setContentIntent(pendingIntent);
//                                NotificationManager notificationManager = (NotificationManager)getSystemService(
//                                        Context.NOTIFICATION_SERVICE
//                                );
//                                notificationManager.notify(0,builder.build());
//
//
//                                //--------------------------Notification---------------------------//
//
//
//
//
//
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                //  Action for 'NO' Button
//                                dialog.cancel();
//                                Toast.makeText(getApplicationContext(), "you choose no action for alertbox",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                //Creating dialog box
//                AlertDialog alert = builder.create();
//                //Setting the title manually
//                alert.setTitle("Alert!!!");
//                alert.show();


            }
        });

        //-------------------------------Alert dialogue--------------------------------------//


        //---------------------------------------------------------------------//
//        organization_OR.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openDialog();
//
//            }
//        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
        //---------------------------------------------------------------------//


//    private void openDialog() {
//        Dialog2 dialog2 = new Dialog2();
//        dialog2.show(getSupportFragmentManager(),"Example Dialogue");
//
//    }

    //---------------------------------------------------------------------//
    private void CreateReport()
    {
        String userName =name_OR.getText().toString();
        String userPhone =phone_OR.getText().toString();
        String userPassword =password_OR.getText().toString();
        String userEmail =email_OR.getText().toString();


        if (TextUtils.isEmpty(userName)){
            Snackbar.make(getWindow().getDecorView().getRootView(), "User Name empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else if (TextUtils.isEmpty(userPhone)){
            Snackbar.make(getWindow().getDecorView().getRootView(), "User Phone empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else if (TextUtils.isEmpty(userPassword)){
            Snackbar.make(getWindow().getDecorView().getRootView(), "User Password empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else if (TextUtils.isEmpty(userEmail)){
            Snackbar.make(getWindow().getDecorView().getRootView(), "User Email empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else{
            loadingBar.setTitle("Report Submission Progressing");
            loadingBar.setMessage("Please wait we are checking for you credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            validate(userName,userPhone,userPassword,userEmail);
        }


    }

    private void validate(final String userName,final  String userPhone,final  String userPassword,final  String userEmail)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(!(snapshot.child("UsersReport").child(userPhone).exists()))
                {
                    HashMap<String,Object> userDataMap = new HashMap<>();

                    userDataMap.put("Phone",userPhone);
                    userDataMap.put("Password",userPassword);
                    userDataMap.put("Name",userName);
                    userDataMap.put("Email",userEmail);

                    RootRef.child("UsersReport").child(userPhone).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(OrgnizationReport_or_Activity.this, "Report has been submitted successfully", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        RootRef.child("Users").child(userPhone).removeValue();

                                         Intent intent = new Intent(OrgnizationReport_or_Activity.this,admin_main.class);
                                         startActivity(intent);

                                    }
                                    else{
                                        loadingBar.dismiss();
                                        Toast.makeText(OrgnizationReport_or_Activity.this, "Network Error : Please try again after some time", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(OrgnizationReport_or_Activity.this,admin_main.class);
                                        startActivity(intent);
                                    }
                                }
                            });

                }
                else
                {
                    Toast.makeText(OrgnizationReport_or_Activity.this, "Already there is a report been submitted on this number "+userPhone+" before", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();


                    Intent intent = new Intent(OrgnizationReport_or_Activity.this,admin_main.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //---------------------------------------------------------------------//
    private void getUserDetailsToDisplay(String userPhone)
    {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.child(userPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Users users = dataSnapshot.getValue(Users.class);
                    name_OR.setText(users.getName());
                    phone_OR.setText(users.getPhone());
                    password_OR.setText(users.getPassword());
                    email_OR.setText(users.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
