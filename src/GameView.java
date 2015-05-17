/**
 * 
 */
package local.orenchi.appsdk_flyingdroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

/**
 * @author mac
 *
 */
@SuppressLint("NewApi")
public class GameView extends SurfaceView implements Callback {

	class GameThread extends Thread {
		SurfaceHolder surfaceHolder;
		boolean shouldContinue = true;
		
		int width;
		int height;
		
		Droid droid;
		static final int droidSize = 200;
		long frameNo = 0;
		long nextGenFrame = 100;
		
		Context context;
		
		static final int EnemyNum = 5;
		Enemy[] enemys;
		static final int enemySize = 200;
		
		public GameThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
			this.surfaceHolder = surfaceHolder;
			droid = new Droid(context, droidSize, droidSize);
			droid.setInitalPosition(100, 0);
			
			this.context = context;
			enemys = new Enemy[EnemyNum];
			enemys[0] = new Enemy(context, enemySize, enemySize);
			
		}
		
		public void setViewSize(int width, int height) {
			this.width = width;
			this.height = height;
			
			droid.setMovingBoundary(0, 0, width, height);
			for (int i = 0; i< EnemyNum; i++) {
				if (enemys[i] != null) {
					enemys[i].setMovingBoundary(0, 0, width, height);
				}
			}
		}
		
		public void upliftDroid(boolean on) {
			droid.uplift(on);
		}
		
		@Override
		public void run() {
			while(shouldContinue) {
				Canvas c = surfaceHolder.lockCanvas();
				draw(c);
				surfaceHolder.unlockCanvasAndPost(c);
			}
		}
		
		public void draw(Canvas c) {
			for (int i = 0; i < EnemyNum; i++) {
				if (enemys[i] != null && enemys[i].isHit(droid)) {
					droid.setImageResourceId(R.drawable.andou_die01);
				}
			}
			
			c.drawARGB(255, 0, 0, 0);
			droid.draw(c);
			
			for (int i = 0; i < EnemyNum; i++) {
				if (enemys[i] != null) {
					enemys[i].draw(c);
				}
			}
			
			if (frameNo == nextGenFrame) {
				for (int i = 0; i < EnemyNum; i++) {
					if (enemys[i] == null) {
						enemys[i] = new Enemy(context, enemySize, enemySize);
						enemys[i].setMovingBoundary(0, 0, width, height);
						nextGenFrame += 100;
						break;
					}
				}
			}
			frameNo++;
		}
	}
	
	GameThread gameThread;
	/**
	 * @param context
	 */
	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		
		gameThread = new GameThread(holder, context, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
			}
		});
		
		setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return dispatchEvent(event);
			}
		});
	}

	private boolean dispatchEvent(MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			gameThread.upliftDroid(true);
			return true;
		case MotionEvent.ACTION_UP:
			gameThread.upliftDroid(false);
			return false;
			default:
				return false;
		}
	}
	/**
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 * @param defStyleRes
	 */
	public GameView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		gameThread.setViewSize(width, height);
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		gameThread.start();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		gameThread = null;
		
	}

}
