package cn.lizhangqu.kb.activity;

import java.io.UnsupportedEncodingException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import cn.lizhangqu.kb.R;
import cn.lizhangqu.kb.adapter.MenuAdapter;
import cn.lizhangqu.kb.model.LinkNode;
import cn.lizhangqu.kb.service.CourseService;
import cn.lizhangqu.kb.service.LinkService;
import cn.lizhangqu.kb.util.HttpUtil;
import cn.lizhangqu.kb.util.LinkUtil;
import cn.lizhangqu.kb.util.SharedPreferenceUtil;
import cn.lizhangqu.kb.util.HttpUtil.QueryCallback;

/**
 * @author lizhangqu
 * @date 2015-2-1
 */
public class MainActivity extends Activity {

	private GridView gridView;
	private LinkService linkService;
	private CourseService courseService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initValue();// 变量初始化
		initView();// 视图初始话
		initEvent();// 事件初始化

	}

	/**
	 * 初始化变量
	 */
	private void initValue() {
		linkService = LinkService.getLinkService();
		courseService = CourseService.getCourseService();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		List<LinkNode> objects = linkService.findAll();
		gridView.setAdapter(new MenuAdapter(getApplicationContext(),
				R.layout.item_linknode_layout, objects));
	}

	/**
	 * 初始事件
	 */
	private void initEvent() {
		// 一系列点击事件的初始化
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView temp = (TextView) view.findViewById(R.id.title);
				String title = temp.getText().toString();
				if (title.equals(LinkUtil.XSGRKB)) {
					jump2Kb(false);
				} else {
					Toast.makeText(getApplicationContext(), title,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}


	/**
	 * 跳到课表页面
	 */
	private void jump2Kb(boolean flag) {
		SharedPreferenceUtil util = new SharedPreferenceUtil(
				getApplicationContext(), "flag");
		if (flag) {
			//flag为true直接跳转
			util.setKeyData(LinkUtil.XSGRKB, "TRUE");
			Intent intent = new Intent(MainActivity.this, CourseActivity.class);
			startActivity(intent);
		} else {
			//flag为false，则先判断是否获取过课表
			//如果已经获取过课表，则跳转
			String keyData = util.getKeyData(LinkUtil.XSGRKB);
			if (keyData.equals("TRUE")) {
				Intent intent = new Intent(MainActivity.this,
						CourseActivity.class);
				startActivity(intent);
			} else {
				//未获取则获取
				HttpUtil.getQuery(MainActivity.this, linkService,LinkUtil.XSGRKB, new QueryCallback() {
					@Override
					public String handleResult(byte[] result) {
							String ret=null;
							try {
								ret = courseService.parseCourse(new String(result,"gb2312"));
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							jump2Kb(true);
							return ret;
					}
				});
			}
		}

	}
}
