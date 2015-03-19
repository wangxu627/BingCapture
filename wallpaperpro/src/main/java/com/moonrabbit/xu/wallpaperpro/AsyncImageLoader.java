package com.moonrabbit.xu.wallpaperpro;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class AsyncImageLoader extends AsyncTask<String, Void, Bitmap> 
{
	public interface FinishCallback
	{
		public void onFinishCallback(Bitmap bitmap);
	}
	
	private ImageView mImageView;
	private FinishCallback mCallback;
	
	public AsyncImageLoader(ImageView view, FinishCallback callback)
	{
		mImageView = view;
		mCallback = callback;
	}
	
	@Override
	protected Bitmap doInBackground(String... params) 
	{
   		Bitmap bitmap = null;   
   		try 
   		{   
			HttpClient client = new DefaultHttpClient();
			URI website = new URI(params[0]);
			HttpGet request = new HttpGet();
			request.setURI(website);
			HttpResponse response = client.execute(request);
			response.getStatusLine().getStatusCode();
			HttpEntity httpEntity = response.getEntity();
		    String content = EntityUtils.toString(httpEntity);
		    int jpgTagIdx = content.indexOf(".jpg");
		    int urlTagIdx = content.lastIndexOf("url:", jpgTagIdx);
		    
		    String url = content.substring(urlTagIdx + 5, jpgTagIdx + 4);
		    String fileName;
		    if(url.startsWith("http"))
		    {
		    	fileName = url;
		    }
		    else
		    {
		    	fileName = params[0] + url;
		    }
		    
		    Log.i("KKKK", fileName);
		    URL myFileUrl = null;   
		    try 
	   		{   
	    		myFileUrl = new URL(fileName);   
	   		} 
	   		catch (MalformedURLException e) 
	   		{   
	    		e.printStackTrace();   
	   		}   
		    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();   
		    conn.setDoInput(true);   
		    conn.connect();   
		    InputStream is = conn.getInputStream();  
		    BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inPreferredConfig = Bitmap.Config.RGB_565;   
		    options.inPurgeable = true;  
		    options.inInputShareable = true; 
		    bitmap = BitmapFactory.decodeStream(is, null, options);   
		    is.close();   
		} 
		catch (IOException e) 
		{   
		    e.printStackTrace();   
		} 
   		catch (URISyntaxException e) 
   		{
			e.printStackTrace();
		}   
		return bitmap;  
	}
	
	 protected void onPostExecute(Bitmap result) 
	 {
		 mImageView.setImageBitmap(result);
		 mCallback.onFinishCallback(result);
     }

}
