package cn.edu.bjtu.fileexplorer.adapter;

import java.io.File;

import android.content.Context;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.edu.bjtu.fileexploree.R;
import cn.edu.bjtu.fileexplorer.utils.ApkUtils;
import cn.edu.bjtu.fileexplorer.utils.AsycLoadImage;

public class FileAdapter extends BaseAdapter implements OnScrollListener {
	private Context context;
	private File[] listFiles;
	private ListView listView;
	public static Parcelable state;
	public AsycLoadImage asycLoadImage;

	public FileAdapter(Context context, File[] listFiles, ListView listView) {
		this.context = context;
		this.listFiles = listFiles;
		this.listView = listView;
		listView.setOnScrollListener(this);
		asycLoadImage = new AsycLoadImage(context, new Handler());
	}

	/**
	 * 通知更新界面
	 * 
	 * @param listFiles
	 */
	public void updte(File[] listFiles) {
		this.listFiles = listFiles;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (listFiles != null) {
			return listFiles.length;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listFiles[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		View view;
		if (convertView != null) {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		} else {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.file_item, null);
			holder.iv_icon = (ImageView) view.findViewById(R.id.iv_file_icon);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_file_name);
			view.setTag(holder);
		}
		// 设置数据
		// 是目录
		if (listFiles[position].isDirectory()) {
			// 1.文件夹有两种情况，空文件夹和非空文件夹
			if (listFiles[position].listFiles().length == 0
					&& listFiles[position].listFiles() == null) {
				holder.iv_icon.setImageResource(R.drawable.folder);
				holder.tv_name.setText(listFiles[position].getName());
			} else {// 有文件的文件夹
				holder.iv_icon.setImageResource(R.drawable.folder_);
				holder.tv_name.setText(listFiles[position].getName());
			}
		} else {// 文件的处理
			String fileName = listFiles[position].getName().toLowerCase();

			if (listFiles[position].getName().endsWith(".txt")) {// 文本的显示

				holder.iv_icon.setImageResource(R.drawable.text);
				holder.tv_name.setText(fileName);

			} else if (fileName.endsWith(".html") || fileName.endsWith(".xml")) {
				holder.iv_icon.setImageResource(R.drawable.html);
				holder.tv_name.setText(fileName);
			} else if (fileName.endsWith(".xls")) {
				holder.iv_icon.setImageResource(R.drawable.excel);
				holder.tv_name.setText(fileName);
			} else if (fileName.endsWith(".3gp") || fileName.endsWith(".mp4")) {
				holder.iv_icon.setImageResource(R.drawable.format_media);
				holder.tv_name.setText(fileName);

			} else if (fileName.endsWith(".flv")) {
				holder.iv_icon.setImageResource(R.drawable.format_flash);
				holder.tv_name.setText(fileName);
				// } else if (fileName.endsWith(".apk")) {// 加载APK的图标
				// Drawable drawable = ApkUtil.loadApkIcon(context,
				// listFiles[position].getAbsolutePath());
				// holder.iv_icon.setImageDrawable(drawable);
				// holder.tv_name.setText(fileName);
			} else if (fileName.endsWith(".mp3") || fileName.endsWith(".ogg")
					|| fileName.endsWith(".wma") || fileName.endsWith(".ape")
					|| fileName.endsWith(".wav")) {

				holder.iv_icon.setImageResource(R.drawable.format_music);
				holder.tv_name.setText(fileName);

			} else if (fileName.endsWith(".apk")) {
				holder.iv_icon.setImageDrawable(ApkUtils.loadApkIcon(context,
						listFiles[position].getAbsolutePath()));
				holder.tv_name.setText(fileName);
			} else if (fileName.endsWith(".jpg")

			|| fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
				// asyncLoadImage.loadImage3(holder.fileIcon);
				// 根据图片设置图标
				// 1 直接设置图片，图片过多时可能造成内存溢出
				// Bitmap bitmap = BitmapFactory.decodeFile(listFiles[position]
				// .getAbsolutePath());
				// 2 把图片放缩,但是还可能造成内存溢出
				// BitmapFactory.Options options = new BitmapFactory.Options();
				// options.inSampleSize = 2;
				// Bitmap bitmap = BitmapFactory.decodeFile(
				// listFiles[position].getAbsolutePath(), options);
				// holder.iv_icon.setImageBitmap(bitmap);
				// 3 异步加载
				holder.iv_icon.setTag(listFiles[position].getAbsolutePath());
				// asycLoadImage.loadImage(holder.iv_icon);
				// asycLoadImage.loadImage2(holder.iv_icon);
				asycLoadImage.loadImage3(holder.iv_icon);
				holder.tv_name.setText(fileName);

			} else {
				holder.iv_icon.setImageResource(R.drawable.file);
				holder.tv_name.setText(listFiles[position].getName());
			}
		}
		return view;
	}

	private static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		// TODO Auto-generated method stub
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_FLING:// 这个滑动中
			// 锁住 isAllow = false
			asycLoadImage.lock();
			break;
		case OnScrollListener.SCROLL_STATE_IDLE:// 滑动停止
			// isAllow = true;
			state = listView.onSaveInstanceState();
			asycLoadImage.unLock();
			// Log.d(TAG, "state:" + state);
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 手指还在屏幕上
			// isAllow = false
			asycLoadImage.lock();
			break;
		default:
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
	}

}
