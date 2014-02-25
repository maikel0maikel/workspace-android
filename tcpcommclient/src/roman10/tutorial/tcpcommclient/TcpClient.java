package roman10.tutorial.tcpcommclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TcpClient extends Activity {
    /** Called when the activity is first created. */
	Button send;
	EditText chat;
	TextView tv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //binding
        
        send=(Button)findViewById(R.id.button_send);
        chat=(EditText)findViewById(R.id.editText_chat);
        tv=(TextView)findViewById(R.id.textView_nhan);
        //set listener
        send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//send msg
				//...
				
				//send and receive
				
				
					Thread tk=new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							String msg=chat.getText().toString()+Calendar.getInstance().get(Calendar.MILLISECOND);
							
							final String nhan = runTcpClient(msg);
							tv.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									tv.setText(nhan);
								}
							});
							Log.w("qd", "Display text on client: " + nhan);
						}
					});
					
					tk.start();
					tk=null;
					Log.w("qd", "Stop thread: ");
					tv.setText("Client thread stop");
					
				}
			
		});
        
    }
    int i=0;
    private static final int TCP_SERVER_PORT = 21111;
	private String runTcpClient(String msg) {
		Log.w("qd", "Start new client instance");
		try {
    		Socket s = new Socket("192.168.0.21", TCP_SERVER_PORT);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			//send output msg
			String outMsg = msg; 
			out.write(outMsg);
			out.flush();
			s.shutdownOutput();
			Log.w("qd", "Client sent: " + outMsg);
			//accept server response
			String inMsg = in.readLine();
			Log.w("qd", "Client received: " + inMsg);
			//close connection
			s.shutdownInput();
			
			s.close();
			
			Log.w("qd", "Client closed " + inMsg);
			return inMsg;
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    	return "No Server found!";
    }
	//replace runTcpClient() at onCreate with this method if you want to run tcp client as a service
	private void runTcpClientAsService() {
		Intent lIntent = new Intent(this.getApplicationContext(), TcpClientService.class);
        this.startService(lIntent);
	}
}