/**
 * 
 */
package local.orenchi.appsdk_flyingdroid;

import android.content.Context;
import android.graphics.Canvas;

/**
 * @author mac
 *
 */
public class Droid extends AbstractGameObject {
	
	private int defaultX;
	private int defaultY;
	private static final float DefaultVelocity = 2;
	private float velocity = DefaultVelocity;
	/**
	 * @param context
	 * @param resourceId
	 * @param width
	 * @param height
	 */
	public Droid(Context context, int width, int height) {
		super(context, R.drawable.andou_diag01, width, height);
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see local.orenchi.appsdk_flyingdroid.AbstractGameObject#setMovingBoundary(int, int, int, int)
	 */
	@Override
	public void setMovingBoundary(int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super.setMovingBoundary(left, top, right, bottom);
		this.bottom -= height;
	}

	public void uplift(boolean on) {
		if (on) {
			velocity = -DefaultVelocity;
		} else {
			velocity = DefaultVelocity;
		}
	}
	public void setInitalPosition(int x, int y) {
		defaultX = x;
		defaultY = y;
		this.x = defaultX;
		this.y = defaultY;
	}
	
	public void draw(Canvas c) {
		draw(c, defaultX, y);
		y += velocity;
		
		if (y < top) {
			y = top;
		} else if (y > bottom) {
			y = bottom;
		}
	}
}
