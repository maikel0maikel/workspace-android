package roman10.tutorial.tcpcommserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TcpServer extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tv = (TextView) this.findViewById(R.id.textView_nhan);
        bt = (Button) this.findViewById(R.id.button_send);
        et = (EditText) this.findViewById(R.id.editText_chatbox);
        //setlistener
        bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Thread tk=new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						while(true)
						{
							final String inc = runTcpServer();
							
					        tv.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									tv.setText(inc);
								}
							});
						}
					}
				});
				tk.start();
			}
		});
        //binding first
        
        
    }
    private TextView tv;
    private Button bt;
    private EditText et;
    private static final int TCP_SERVER_PORT = 21111;
    private String runTcpServer() {
    	Log.w("qd", "Start new Server instace");
    	ServerSocket ss = null;
    	String incomingMsg="fail";
    	try {
    		ss = new ServerSocket(TCP_SERVER_PORT);
			ss.setSoTimeout(0);
			//accept connections
			Log.w("qd", "binding... ");
			Socket s = ss.accept();//waiting here
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			//receive a message
			incomingMsg = in.readLine();
			Log.w("qd", "received: " + incomingMsg);
			//send a message
			String outgoingMsg = "Server is received!";
			out.write(outgoingMsg);
			out.flush();
			Log.w("TcpServer", "Server sent: " + outgoingMsg);
			//SystemClock.sleep(5000);
			s.close();
			Log.w("TcpServer", "Server Closed!");
			
		} catch (InterruptedIOException e) {
			//if timeout occurs
			e.printStackTrace();
    	} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ss != null) {
				try {
					ss.close();
					return incomingMsg;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	return incomingMsg;
    }
}