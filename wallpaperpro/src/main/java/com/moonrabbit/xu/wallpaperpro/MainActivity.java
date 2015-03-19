package com.moonrabbit.xu.wallpaperpro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements AsyncImageLoader.FinishCallback{

    private ArrayList<Bitmap> mBitmapList = new ArrayList<Bitmap>();
    private TextView mTipView;
    private ProgressBar mProgress;
    private int mFinishCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path = "sdcard/BingCapture/";
        File file = new File(path);
        if (!file.exists())
        {
            boolean t = file.mkdirs();
        }

        Button btnSave = (Button)findViewById(R.id.button2);
        btnSave.setEnabled(false);

        mTipView = (TextView)findViewById(R.id.textView);
        mProgress = (ProgressBar)findViewById(R.id.progressBar1);
        mProgress.setVisibility(View.INVISIBLE);
        mFinishCount = 0;

    }

    public void onCaptureClicked(View view)
    {
        Log.i("AAAAAA","BBBBBBB");
        mFinishCount = 0;
        mBitmapList.clear();
        mTipView.setVisibility(View.INVISIBLE);
        mProgress.setVisibility(View.VISIBLE);

        ImageView view1 = (ImageView)findViewById(R.id.imageView1);
        ImageView view2 = (ImageView)findViewById(R.id.imageView2);

        String[] names = { "http://cn.bing.com", "http://bing.ca" };
        AsyncImageLoader loader1 = new AsyncImageLoader(view1, this);
        AsyncImageLoader loader2 = new AsyncImageLoader(view2, this);
        loader1.execute(names[0]);
        loader2.execute(names[1]);
    }

    public void onSaveClicked(View view)
    {
		/*
		if(mHasClickAd == false)
		{
			new AlertDialog.Builder(this)
			 	.setTitle("ÎÂÜ°ÌáÊ¾")
			 	.setMessage("¸ÐÐ»Äú¶Ô±¾Ó¦ÓÃµÄÖ§³Ö£¬ÈçÄúÐèÒª±£´æÍ¼Æ¬£¬ÇëÏÈµã»÷Ò»´Î¹ã¸æ¡£¸ÐÐ»Äú¶Ô¿ª·¢ÕßµÄÖ§³Ö£¬Ð»Ð»¡£")
			 	.setPositiveButton("È·¶¨", null)
			 	.show();
			return;
		}
		*/

        if(mBitmapList.get(0) != null)
        {
            saveFileToSDCard(mBitmapList.get(0));
        }
        Toast.makeText(getApplicationContext(), R.string.save_1_tips ,Toast.LENGTH_LONG).show();

        if(mBitmapList.size() < 2)
            return;

        if(mBitmapList.get(1) != null)
        {
            saveFileToSDCard(mBitmapList.get(1));
        }
        Toast.makeText(getApplicationContext(), R.string.save_2_tips ,Toast.LENGTH_LONG).show();
    }

    private void saveFileToSDCard(Bitmap bitmap)
    {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SS");//Ð¡Ð´µÄmm±íÊ¾µÄÊÇ·ÖÖÓ
            File imageFile = new File("/sdcard/BingCapture/" +  sdf.format(date) + ".jpg");
            try
            {
                imageFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(imageFile);
                bitmap.compress(CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFinishCallback(Bitmap bitmap)
    {
        // TODO Auto-generated method stub
        Button btnSave = (Button)findViewById(R.id.button2);
        btnSave.setEnabled(true);
        mBitmapList.add(bitmap);
        if(++mFinishCount == 2)
        {
            mProgress.setVisibility(View.INVISIBLE);
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
