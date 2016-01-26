package com.cdac.mnsk.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Socket socket;

    EditText ipAddress,portNum;
    Button connectButton;

    String ipAddr="";
    int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipAddress = (EditText) findViewById(R.id.ip_address);
        portNum = (EditText) findViewById(R.id.port);
        connectButton = (Button) findViewById(R.id.connect_Button);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 1;
                if(ipAddress.getText().toString().equals("")) {
                    flag = 0;
                    Toast.makeText(MainActivity.this, "Please enter IP Address", Toast.LENGTH_SHORT).show();
                }else {
                    ipAddr = ipAddress.getText().toString().trim();
                }

                if(portNum.getText().toString().equals("")) {
                    flag = 0;
                    Toast.makeText(MainActivity.this, "Please enter Port", Toast.LENGTH_SHORT).show();
                }else {
                    port = Integer.parseInt(portNum.getText().toString());
                }
//
//                 if(flag == 1) {
//                     Intent i = new Intent(MainActivity.this, SecondActivity.class);
//                     i.putExtra("_ip", ipAddr);
//                     i.putExtra("_port", port);
//                     startActivity(i);
//                 }
                new Thread(new ClientThread()).start();
            }
        });
    }


    public static class Sockethand {


        private static Socket sockettran;

        public static synchronized Socket getSocket(){
            return sockettran;
        }


        public static synchronized void setSocket(Socket socket){
            Sockethand.sockettran = socket;
        }
    }
    class ClientThread implements Runnable {
        String response;

        @Override
        public void run() {

            try {


                socket = new Socket(ipAddr, port);			// Connecting to a Socket with Address and port number

                BufferedReader controlSocketIn0 = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                response = controlSocketIn0.readLine();
                response += "\n"+controlSocketIn0.readLine();

                Sockethand.setSocket(socket);

                Intent intent=new Intent(MainActivity.this,SecondActivity.class);	// Making a new Intent to Start Another Activity
                intent.putExtra("_response",response);
                intent.putExtra("_ip",ipAddr);
                startActivity(intent);										// Starting new Activity

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }


    }
}
