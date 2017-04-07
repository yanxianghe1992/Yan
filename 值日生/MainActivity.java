package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button button1, button2, button3;
    private TextView textView1, textView2, textView3;
    private ListView listView;
    private List<String> list = new ArrayList();
    private MyBaseAdapter adapter;
    private String st1;
    private String st2;
    private String st3;
    private SQLiteDatabase sq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        //利用本地数据库来保存
        sq = openOrCreateDatabase("student", MODE_APPEND, null);//创建库
        sq.execSQL("create table if not exists user(_id integer primary key autoincrement,name text not null)");//创建表
        adapter = new MyBaseAdapter(this, list);
        listView.setAdapter(adapter);
        seleteKu();//刷新数据
        registerForContextMenu(listView);//菜单要长按listView才能触发
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.addSubMenu(100, 100, 100, "临时删除");
        menu.addSubMenu(200, 200, 200, "临时增加");
        menu.addSubMenu(300, 300, 300, "初始化状态");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();//获取列表的数据
        int itemId = item.getItemId();
        switch (itemId) {
            case 100:
                list.remove(info.position);//info.position为当前选择的item的下标 对应listView值
                adapter.setList(list);
                button1.setEnabled(false);
                button2.setEnabled(false);
                break;
            case 200:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);//创建对话框
                builder.setTitle("增加值日生");
                View view = View.inflate(this, R.layout.add, null);//创建自定义View
                final EditText editText = (EditText) view.findViewById(R.id.add_et);//找出自定义View 下的edtext
                builder.setView(view);//设置自定义View
                builder.setPositiveButton("增加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {//确定按钮
                        String trim = editText.getText().toString().trim();
                        list.add(trim);
                        adapter.setList(list);
                        button1.setEnabled(false);
                        button2.setEnabled(false);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case 300:
                seleteKu();//刷新数据
                button1.setEnabled(true);
                button2.setEnabled(true);
                break;
        }


        return super.onContextItemSelected(item);
    }

    private void seleteKu() {//刷新数据
        list.clear();
        Cursor user = sq.query("user", new String[]{"name"}, null, null, null, null, null);//查询表里所有 name 的内容
        while (user.moveToNext()) {
            int name = user.getColumnIndex("name");//获取 index
            String string = user.getString(name);  //获取数据
            list.add(string);//list 添加数据
        }
        adapter.setList(list);//刷新listview

    }


    private void initView() {
        button1 = (Button) findViewById(R.id.bt1);
        button2 = (Button) findViewById(R.id.bt2);
        button3 = (Button) findViewById(R.id.bt3);
        textView1 = (TextView) findViewById(R.id.tv1);
        textView2 = (TextView) findViewById(R.id.tv2);
        textView3 = (TextView) findViewById(R.id.tv3);
        listView = (ListView) findViewById(R.id.lv);

    }

    private void initEvent() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }


    @Override
    public void onClick(View view1) {
        switch (view1.getId()) {
            case R.id.bt1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);//创建对话框
                builder.setTitle("增加值日生");
                View view = View.inflate(this, R.layout.add, null);//创建自定义View
                final EditText editText = (EditText) view.findViewById(R.id.add_et);//找出自定义View 下的edtext
                builder.setView(view);//设置自定义View
                builder.setPositiveButton("增加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {//确定按钮
                        String trim = editText.getText().toString().trim();
                        ContentValues values = new ContentValues();//创建CV来保存 trim数据 来对接
                        values.put("name", trim);
                        sq.insert("user", null, values);//调用表的增加函数来 保存在表内
                        list.add(trim);
                        adapter.setList(list);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.bt2:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("删除值日生");
                View view2 = View.inflate(this, R.layout.delete, null);
                final EditText editText2 = (EditText) view2.findViewById(R.id.delete_et);
                builder2.setView(view2);
                builder2.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String trim = editText2.getText().toString().trim();
                        sq.delete("user", "name=?", new String[]{trim});//删除表内的东西
                        seleteKu();//刷新数据
                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog2 = builder2.create();
                dialog2.show();
                break;
            case R.id.bt3:
                Random r = new Random();
                st1 = list.get(r.nextInt(list.size()));
                textView1.setText(st1);
                st2 = list.get(r.nextInt(list.size()));
                while (st1.equals(st2)) {
                    st2 = list.get(r.nextInt(list.size()));
                }
                textView2.setText(st2);
                st3 = list.get(r.nextInt(list.size()));
                while (st1.equals(st3) || st2.equals(st3)) {
                    st3 = list.get(r.nextInt(list.size()));
                }
                textView3.setText(st3);
                break;
        }
    }
}