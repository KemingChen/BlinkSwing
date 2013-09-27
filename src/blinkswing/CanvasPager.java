package blinkswing;

import java.util.ArrayList;

import android.graphics.Rect;

public class CanvasPager
{
	private MyCanvas parentCanvas;
	private int nowPage;
	private ArrayList<ArrayList<Rect>> pointPages;
	public CanvasPager(MyCanvas canvas)
	{
		this.parentCanvas = canvas;
		init();
	}
	public void init()
	{
		nowPage = 0;
		pointPages = new ArrayList<ArrayList<Rect>>();
		pointPages.add(new ArrayList<Rect>());
	}

	public void nextPage()
	{
		nowPage = (nowPage + 1) % pointPages.size();
		parentCanvas.invalidate();
	}

	public void prePage()
	{
		nowPage = (nowPage + pointPages.size() - 1) % pointPages.size();
		parentCanvas.invalidate();
	}

	public void newPage()
	{
		ArrayList<Rect> points = new ArrayList<Rect>();
		if(nowPage==pointPages.size()-1)
		{
			pointPages.add(points);
		}
		else
		{
			pointPages.add(nowPage+1, points);
		}
		nowPage++;
		parentCanvas.invalidate();
	}

	public void delPage()
	{
		if (pointPages.size() > 1)
		{
			pointPages.remove(nowPage);
			parentCanvas.invalidate();
		}
	}

	public void clearPage()
	{
		pointPages.get(nowPage).clear();
		parentCanvas.invalidate();
	}
	
	public boolean isFirstPage()
	{
		return nowPage==0;
	}
	
	public boolean isLastPage()
	{
		return nowPage==pointPages.size()-1;
	}
	
	public void addPointInCurrentPage(Rect rect)
	{
		pointPages.get(nowPage).add(rect);
	}

	public ArrayList<Rect> getCurrentPoints()
	{
		return pointPages.get(nowPage);
	}
}
