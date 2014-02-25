package com.example.hour8app;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
//register OnAnswerSelectedListener for PIPELINE that Fragment can interact with...
public class MainActivity extends Activity implements OnAnswerSelectedListener  {
	String cauhoi[] = {"1. Ăn tối chưa ?", "2. Thức dậy chưa", "3. Bạn bao nhiêu tuổi ?"
			,"4. Đi học rồi hả ?"		
	};//QS List for Load next QS
	int index=0;//current QS pointer
	private void DisplayQS(String input)//call to update Fragment
	{
		//replace fragment
		FragmentYesNo fr=new FragmentYesNo();
		//set question
		Bundle data=new Bundle();
		data.putString("question", input);
		fr.setArguments(data);
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.place_holder, fr);
		ft.addToBackStack("example");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Hour8App");
		
		//click load qs
		Button load_qs=(Button) findViewById(R.id.button_load_question);
		load_qs.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//load new qs
				DisplayQS(cauhoi[index]);
				index++;
				if(index>=cauhoi.length) index=0;
				
			}
		});
		//hiển thị câu hỏi lần đầu khi Activity được load
		load_qs.performClick();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void OnAnswerSelected(String answer) {//called by Fragment to pass data to this Activity
		// TODO Auto-generated method stub
		Toast.makeText(this, answer, 1000).show();
	}
}
