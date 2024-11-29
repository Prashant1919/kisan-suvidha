package com.example.greenlifeuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.greenlifeuser.models.ContactUsModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactUs extends AppCompatActivity {

    EditText contactName, contactEmail, contactMsg;
    Button sendMsg;

    DatabaseReference contactRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        contactName = findViewById(R.id.contact_name);
        contactEmail = findViewById(R.id.contact_email);
        contactMsg = findViewById(R.id.contact_message);
        sendMsg = findViewById(R.id.send_message);

        contactRef = FirebaseDatabase.getInstance().getReference().child("ContactUs");

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

    }

    private void insertData() {
        String name = contactName.getText().toString();
        String email = contactEmail.getText().toString();
        String message = contactMsg.getText().toString();

        if (name.isEmpty() || email.isEmpty() || message.isEmpty()){
            Toast.makeText(this, "Please fill all the fields!!", Toast.LENGTH_SHORT).show();
        }else {
            ContactUsModel contactUsModel = new ContactUsModel(name, email, message);
            contactRef.push().setValue(contactUsModel);
            Toast.makeText(this, "Your message is saved now, Thank you!!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ContactUs.this, HomeActivity.class));
            finish();
        }
    }
}