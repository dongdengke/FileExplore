package cn.edu.bjtu.fileexplorer;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.bjtu.fileexploree.R;
import cn.edu.bjtu.fileexplorer.adapter.FileAdapter;
import cn.edu.bjtu.fileexplorer.utils.FileUtils;
import cn.edu.bjtu.fileexplorer.utils.MyFileFilter;

public class MainActivity extends Activity implements OnItemClickListener,
		OnItemLongClickListener {

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
	// 上下文菜单操作的文件类型（文件或者文件夹）
	private String fileType = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			listFiles = getListFiles();
			adapter = new FileAdapter(this, listFiles, lv_folder);

			lv_folder.setAdapter(adapter);

		} else {
			Toast.makeText(this, "SD卡状态异常，请检查SD卡状态", Toast.LENGTH_SHORT).show();
		}
		lv_folder.setOnItemClickListener(this);
		lv_folder.setOnItemLongClickListener(this);
		// lv_folder.setOnScrollListener(this);
	}

	public File[] getListFiles() {
		file = Environment.getExternalStorageDirectory();
		listFiles = file.listFiles(new MyFileFilter());
		return listFiles = FileUtils.sortFiles(listFiles);
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

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		if (file.isDirectory()) {
			fileType = "文件夹";
		} else {
			fileType = "文件";
		}
		lv_folder.setOnCreateContextMenuListener(contextMenuListener);
		return false;
	}

	/**
	 * 创建上下文菜单的监听器
	 */
	OnCreateContextMenuListener contextMenuListener = new OnCreateContextMenuListener() {

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			menu.setHeaderTitle("操作" + fileType);
			int resId;
			if (fileType.equals("文件夹")) {
				resId = R.drawable.menu_folder;
			} else {
				resId = R.drawable.menu_file;
			}
			menu.setHeaderIcon(resId);
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.file_context_menu, menu);
		}
	};

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// 关键地方
		AdapterView.AdapterContextMenuInfo menu;
		menu = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = menu.position;
		final File file = listFiles[position];

		switch (item.getItemId()) {
		case R.id.rename:
			rename(file);

			break;
		case R.id.selectAll:

			break;
		case R.id.copy:
			Toast.makeText(this, "点击了复制", Toast.LENGTH_LONG).show();
			break;
		case R.id.paste:
			Toast.makeText(this, "点击了粘贴", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * 文件的重命名
	 * 
	 * @param file2
	 */
	private void rename(final File file) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("重命名");

		final EditText edit = new EditText(this);
		edit.setHint("请输入新的名字");
		edit.setText(file.getName());
		edit.selectAll();
		dialog.setView(edit);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String newName = edit.getText().toString();
				if (newName != null && !"".equals(newName)) {
					String oldPath = file.getPath();
					int indexOf = oldPath.lastIndexOf("/");
					String newPath = oldPath.substring(0, indexOf);
					boolean b = file
							.renameTo(new File(newPath + "/" + newName));
					if (b) {
						Toast.makeText(MainActivity.this, "重命名成功",
								Toast.LENGTH_LONG).show();
						adapter.updte(getListFiles());
						if (adapter.state != null) {
							lv_folder.onRestoreInstanceState(adapter.state);
						}
					} else {
						Toast.makeText(MainActivity.this, "重命名失败",
								Toast.LENGTH_LONG).show();
					}

				} else {
					Toast.makeText(MainActivity.this, "名字不能为空",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		dialog.show();
	}
}
