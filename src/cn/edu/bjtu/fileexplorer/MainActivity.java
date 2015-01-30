package cn.edu.bjtu.fileexplorer;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.bjtu.fileexploree.R;
import cn.edu.bjtu.fileexplorer.adapter.FileAdapter;
import cn.edu.bjtu.fileexplorer.utils.FileUtils;
import cn.edu.bjtu.fileexplorer.utils.MyFileFilter;

public class MainActivity extends Activity implements OnItemClickListener {

	private ImageButton ivb_copy;
	private ImageButton ivb_paste;
	private ImageButton ivb_multi_choose;
	private ImageButton ivb_show_type;
	private TextView tv_path;
	private ListView lv_folder;
	private FileAdapter adapter;
	private File[] listFiles;
	private File file;
	private File parendFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			file = Environment.getExternalStorageDirectory();
			listFiles = file.listFiles(new MyFileFilter());
			listFiles = FileUtils.sortFiles(listFiles);
			adapter = new FileAdapter(this, listFiles, lv_folder);

			lv_folder.setAdapter(adapter);

		} else {
			Toast.makeText(this, "SD卡状态异常，请检查SD卡状态", Toast.LENGTH_SHORT).show();
		}
		lv_folder.setOnItemClickListener(this);
		// lv_folder.setOnScrollListener(this);
	}

	/**
	 * 获得sdcard的路径
	 * 
	 * @return
	 */
	public String getSdpath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		ivb_copy = (ImageButton) findViewById(R.id.ivb_copy);
		ivb_paste = (ImageButton) findViewById(R.id.ivb_paste);
		ivb_multi_choose = (ImageButton) findViewById(R.id.ivb_multi_choose);
		ivb_show_type = (ImageButton) findViewById(R.id.ivb_show_type);
		tv_path = (TextView) findViewById(R.id.tv_path);
		lv_folder = (ListView) findViewById(R.id.lv_folder);
		tv_path.setText(getSdpath());
	}

	/**
	 * 条目点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// 是目录的话进入下一级目录
		if (listFiles[position].isDirectory()) {
			parendFile = listFiles[position].getParentFile();
			tv_path.append("/" + listFiles[position].getName());
			listFiles = listFiles[position].listFiles(new MyFileFilter());
			listFiles = FileUtils.sortFiles(listFiles);
			adapter.updte(listFiles);
		} else {

		}
	}

	/**
	 * 处理按下返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (parendFile != null && !parendFile.getName().equals("")
					&& !parendFile.getName().equals(getSdpath())) {
				listFiles = parendFile.listFiles(new MyFileFilter());
				listFiles = FileUtils.sortFiles(listFiles);
				adapter.updte(listFiles);
				parendFile = parendFile.getParentFile();

				String path = tv_path.getText().toString();
				int indexOf = path.lastIndexOf("/");
				path = path.substring(0, indexOf);
				tv_path.setText(path);
				if ((adapter.state != null)) {
					lv_folder.setAdapter(adapter);
					lv_folder.onRestoreInstanceState(adapter.state);
				}
			} else {
				finish();
			}
		}
		return true;
	}
}
