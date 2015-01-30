package cn.edu.bjtu.fileexplorer.utils;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;
import cn.edu.bjtu.fileexploree.R;
import cn.edu.bjtu.fileexplorer.domain.CacheImage;

/**
 * 异步加载图片
 * 
 * @author dong
 * 
 */
public class AsycLoadImage {
	private Context context;
	private Handler handler;
	private Object lock = new Object();
	private boolean isAllow = true;
	private ConcurrentLinkedQueue<CacheImage> cacheImages;

	public AsycLoadImage(Context context, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
		cacheImages = new ConcurrentLinkedQueue<CacheImage>();
	}

	public void loadImage(ImageView imageView) {
		String path = (String) imageView.getTag();
		// 开启线程，加载图片
		new AsycThread(imageView, path).start();
	}

	public class AsycThread extends Thread {
		private ImageView imageView;
		private String path;
		private Bitmap bitmap;

		public AsycThread(ImageView imageView, String path) {
			super();
			this.imageView = imageView;
			this.path = path;
		}

		@Override
		public void run() {
			super.run();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			bitmap = BitmapFactory.decodeFile(path, options);
			handler.post(new Runnable() {
				@Override
				public void run() {
					imageView.setImageBitmap(bitmap);
				}
			});
		}
	}

	public void lock() {
		isAllow = false;
	}

	public void unLock() {
		isAllow = true;
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public void loadImage2(ImageView imageView) {
		String path = (String) imageView.getTag();
		// 开启线程，加载图片
		imageView.setImageResource(R.drawable.format_picture);
		new AsycThread2(imageView, path).start();
	}

	public class AsycThread2 extends Thread {
		private ImageView imageView;
		private String path;
		private Bitmap bitmap;

		public AsycThread2(ImageView imageView, String path) {
			super();
			this.imageView = imageView;
			this.path = path;
		}

		@Override
		public void run() {
			super.run();
			// 同步锁，isallow为false时，停止加载
			if (!isAllow) {
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			bitmap = BitmapFactory.decodeFile(path, options);
			handler.post(new Runnable() {
				@Override
				public void run() {
					imageView.setImageBitmap(bitmap);
				}
			});
		}
	}

	public void loadImage3(ImageView imageView) {
		String path = (String) imageView.getTag();
		// 开启线程，加载图片
		imageView.setImageResource(R.drawable.format_picture);

		// 从缓存中取
		for (CacheImage image : cacheImages) {
			if (path.equals(image.getPath())) {
				Bitmap bitmap = image.getBitmap();
				imageView.setImageBitmap(bitmap);
				return;
			}
		}
		new AsycThread3(imageView, path).start();
	}

	public class AsycThread3 extends Thread {
		private ImageView imageView;
		private String path;
		private Bitmap bitmap;

		public AsycThread3(ImageView imageView, String path) {
			super();
			this.imageView = imageView;
			this.path = path;
		}

		@Override
		public void run() {
			super.run();
			// 同步锁，isallow为false时，停止加载
			if (!isAllow) {
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			bitmap = BitmapFactory.decodeFile(path, options);
			CacheImage cacheImage = new CacheImage();
			cacheImage.setPath(path);
			cacheImage.setBitmap(bitmap);
			// 如果长度大于cacheimags的长度，先移除第一个，然后在加入，否则，直接加入
			if (cacheImages.size() >= 100) {
				cacheImages.peek();
			}
			cacheImages.add(cacheImage);

			handler.post(new Runnable() {
				@Override
				public void run() {
					imageView.setImageBitmap(bitmap);
				}
			});
		}
	}
}
