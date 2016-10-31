package com.example.memento;



import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button chooseDate, add, query;
	private EditText date, subject, body;
	private ListView result;
	private LinearLayout title;
	MyDatabaseHelper mydbHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		chooseDate = (Button) findViewById(R.id.chooseDate);
		add = (Button) findViewById(R.id.add);
		query = (Button) findViewById(R.id.query);
		date = (EditText) findViewById(R.id.date);
		subject = (EditText) findViewById(R.id.subject);
		body = (EditText) findViewById(R.id.body);
		result = (ListView) findViewById(R.id.result);
		title=(LinearLayout)findViewById(R.id.title);
		title.setVisibility(View.INVISIBLE);
		chooseDate.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Calendar c=Calendar.getInstance();
				new DatePickerDialog(MainActivity.this,new DatePickerDialog.OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int month,int day){
						
						// TODO Auto-generated method stub
						date.setText(year+"-"+(month+1)+"-"+day);
                     }
				},c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
			}
				});
	
		
				
			}
		
	
	private class MyOnClickListerner implements OnClickListener{
		public void onClick(View v){mydbHelper=new MyDatabaseHelper(MainActivity.this,"memento01.bd",null,1);
		SQLiteDatabase bd=mydbHelper.getReadableDatabase();
		String subStr=subject.getText().toString();
		String bodyStr=body.getText().toString();
		String dateStr=date.getText().toString();
		switch(v.getId()){
		case R.id.add:
			title.setVisibility(View.INVISIBLE);
			addMemento(bd,subStr,bodyStr,dateStr);
			Toast.makeText(MainActivity.this, "添加备忘录成功", 1000).show();
			result.setAdapter(null);
			break;
		case R.id.query:
			title.setVisibility(View.VISIBLE);
			Cursor cursor=queryMemento(bd,subStr,bodyStr,dateStr);
			SimpleCursorAdapter  resultAdapter=new SimpleCursorAdapter
					(MainActivity.this,R.layout.result,cursor,new String[]{"_id","subject","body","date"},
							new int[]{ R.id.memento01_num, R.id.memento01_subject,
							R.id.memento01_body, R.id.memento01_date });
			result.setAdapter(resultAdapter);
			  break;
			  default:
				  break;
			
		}
	}
		
}
  public void addMemento (SQLiteDatabase db,String subject,String body,String date){
	     db.execSQL("insert into memento_tb values(null,?,?,?)",new String[]{
	    		 subject,body,date});
	             this.subject.setText("");
	             this.body.setText("");
	             this.date.setText("");
	             }
   public Cursor queryMemento(SQLiteDatabase db,String subject,String body,String date){
	   Cursor cursor=db.rawQuery("select * from memento_tb where subject like ? and body like ? and date like ?",new String[]{"%"+subject+"%",
			   "%"+body+"%","%"+date+"%"});
	   return cursor;
	     
	    
  }

  protected void onDestroy(){
	  if (mydbHelper!=null){
		  mydbHelper.close();
	  }
  }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
