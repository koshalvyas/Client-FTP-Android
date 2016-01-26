package com.cdac.mnsk.client;

import java.io.*;
import java.net.*;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {


    String ipAddr;
    TextView textResponse;
    EditText ftpCommand;
    ImageButton sendButton;

    String command,output;
    Socket socket;
    Socket dataSocket = new Socket();
    Handler Updateconv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textResponse = (TextView) findViewById(R.id.output);
        ftpCommand = (EditText) findViewById(R.id.ftp_command);
        sendButton = (ImageButton) findViewById(R.id.send_button);

        socket = MainActivity.Sockethand.getSocket();
        Updateconv = new Handler();

        Bundle bu = getIntent().getExtras();
        output = bu.getString("_response");
        ipAddr = bu.getString("_ip");

        textResponse.setText(output);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command = ftpCommand.getText().toString();
                new Thread(new CommunicationThread(socket,command)).start();
            }
        });

    }

    class CommunicationThread implements Runnable {

        Socket controlSocket;
        String method = "";

        BufferedReader controlSocketIn;
        PrintWriter controlSocketOut;
        String controlResponse = "";

        public CommunicationThread(Socket clientSocket, String comnd) {

            this.controlSocket = clientSocket;
            this.method = comnd;

            try {

                //this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
                controlSocketIn = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));

                controlSocketOut = new PrintWriter(controlSocket.getOutputStream(), true);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {

            if(!(method.contains("USER")  || method.contains("PASS")  ||  method.contains("SYST")  || method.contains("STOR")
                    || method.contains("PORT")   || method.contains("PASV")   ||  method.contains("RETR")   ||  method.contains("CWD")
                    || method.contains("LIST")   || method.contains("PWD")    || method.contains("MKD")     || method.contains("QUIT")))
            {   controlSocketOut.println(method);
                try {
                    controlResponse = controlSocketIn.readLine();
                    controlResponse += "\n"+controlSocketIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Updateconv.post(new updateUIThread(controlResponse));
            }
//
//            try {
//
//                controlResponse = controlSocketIn.readLine();
//                controlResponse += "\n"+controlSocketIn.readLine();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Updateconv.post(new updateUIThread(controlResponse));

            if (method.contains("USER")) {
                //String[] inpToken = method.split(" ");

                controlSocketOut.println(method);
                try {
                    controlResponse = controlSocketIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Updateconv.post(new updateUIThread(controlResponse));

            }
            if (method.contains("PASS")) {
                //String[] inpToken = method.split(" ");
                controlSocketOut.println(method);
                try {
                    controlResponse = controlSocketIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Updateconv.post(new updateUIThread(controlResponse));

            }
            if (method.contains("SYST")) {
                //String[] inpToken = method.split(" ");
                controlSocketOut.println(method);
                try {
                    controlResponse = controlSocketIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Updateconv.post(new updateUIThread(controlResponse));
            }
            if (method.contains("PASV")) {
                controlSocketOut.println(method);
                try {
                    controlResponse = controlSocketIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Updateconv.post(new updateUIThread(controlResponse));

                try {
                    dataSocket.connect(new InetSocketAddress(ipAddr, 1220));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BufferedReader dataSocketIn = null;
                try {
                    dataSocketIn = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    controlResponse += "\n"+dataSocketIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Updateconv.post(new updateUIThread(controlResponse));

            }
            if (method.contains("PWD")) {
                //String[] inpToken = method.split(" ");
                controlSocketOut.println(method);
                try {
                    controlResponse = controlSocketIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //controlSocketOut.println(method);
                BufferedReader datapSocketIn = null;
                try {
                    datapSocketIn = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    controlResponse = datapSocketIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Updateconv.post(new updateUIThread(controlResponse));
            }
            if (method.contains("LIST")) {
                //String[] inpToken = method.split(" ");
                controlSocketOut.println(method);
                try {
                    controlResponse = controlSocketIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                BufferedReader reader = null;
//                try {
//                    reader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                StringBuilder out = new StringBuilder();
                byte[] bytes = new byte[16 * 1024];
//                String line;
//                try {
//                    while ((line = reader.readLine()) != null) {
//                        out.append(line).append("\n");
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                InputStream in = null;
                try {
                    in = dataSocket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int count;
                try {
                    while ((count = in.read(bytes)) > 0) {
                        if (bytes[count] == '\0')
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String list = new String(bytes);
                controlResponse+="\n"+list;
                Updateconv.post(new updateUIThread(controlResponse));

            }
            if(method.contains("CWD"))
            {
                //String[] inpToken = method.split(" ");
                controlSocketOut.println(method);
                try {
                    controlResponse = controlSocketIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Updateconv.post(new updateUIThread(controlResponse));
            }
            if(method.contains("MKD"))
            {
                //String[] inpToken = method.split(" ");
                controlSocketOut.println(method);
                try {
                    controlResponse = controlSocketIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Updateconv.post(new updateUIThread(controlResponse));
            }

            if (method.contains("RETR")) {
                controlSocketOut.println(method);
                try {
                    controlResponse = controlSocketIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Updateconv.post(new updateUIThread(controlResponse));
                //Socket dataSocket = new Socket(serverAddress, 1220);
                //System.out.println(dataSocket);
                // BufferedReader SocketIn = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                //this.socketIn = new BufferedReader(new InputStreamReader(this.dataSocket.getInputStream()));
                String[] responseToken = method.split(" ");
                String fileName = responseToken[1];

                String fpath = "/sdcard/"+fileName;

                File file = new File(fpath);
                // Get the size of the file
                //if (!file.exists())
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] bytes = new byte[16 * 1024];
                InputStream in = null;
                try {
                    in = dataSocket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                OutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                int count;
                try {
                    while ((count = in.read(bytes)) > 0) {
                        out.write(bytes, 0, count);
                        if (bytes[count] == '\0')// || count == 0 || (System.currentTimeMillis()-timeOut) > 5)
                            break;

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (method.contains("STOR")){
                controlSocketOut.println(method);
                try {
                    controlResponse = controlSocketIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //Socket dataSocket = new Socket(serverAddress, 1220);
                //System.out.println(dataSocket);
                // BufferedReader SocketIn = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                //this.socketIn = new BufferedReader(new InputStreamReader(this.dataSocket.getInputStream()));
                String[] responseToken = method.split(" ");
                String fileName = responseToken[1];

                //  File file = new File(fileName);
                // Get the size of the file
                //if (!file.exists())
                //file.createNewFile();
                String fpath = "/sdcard/"+fileName;
                File myFile = new File(fpath);

                //Socket socket = server_socket.accept();
                int count;
                byte[] buffer = new byte[16 * 1024];

                OutputStream out = null;
                try {
                    out = dataSocket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedInputStream in = null;
                try {
                    in = new BufferedInputStream(new FileInputStream(myFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    while ((count = in.read(buffer)) > 0) {
                        out.write(buffer, 0, count);
                        out.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Updateconv.post(new updateUIThread(controlResponse));
            }
            if(method.contains("QUIT"))
            {	controlSocketOut.println(method);

                try {
                    controlResponse = controlSocketIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(controlResponse);
                try {
                    controlResponse += "\n"+controlSocketIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Updateconv.post(new updateUIThread(controlResponse));

                try {
                    dataSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    controlSocketIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                controlSocketOut.close();
                try {
                    controlSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
            else
            {
                
            }

        }

        class updateUIThread implements Runnable {
            private String msg;

            public updateUIThread(String str) {
                this.msg = str;
            }

            @Override
            public void run() {
                textResponse.setText(msg);

            }

        }

    }


}


