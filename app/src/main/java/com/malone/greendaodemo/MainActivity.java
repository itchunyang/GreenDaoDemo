package com.malone.greendaodemo;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.malone.greendaodemo.bean.Girl;
import com.malone.greendaodemo.gen.DaoMaster;
import com.malone.greendaodemo.gen.DaoSession;
import com.malone.greendaodemo.gen.GirlDao;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.LazyList;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    /**
     * DevOpenHelper:schemaVersion 版本改变 会删除原有数据库
     * WARNING: Drops all table on Upgrade! Use only during development.
     * 数据库升级的话，会删除所有表,然后重新创建。这种方式在开发期间，APP还没有上线之前是可以的。
     * <p>
     * APP上线后,数据库升级需要我们写一个类来实现OpenHelper
     */
    DaoMaster.DevOpenHelper devOpenHelper;

    /**
     * 这个类是数据库的统领，也是整个操作的入口。其管理着数据库， 数据库版本号和一些数据库相关的信息
     * DaoMaster可以用来生成下面将要说到DaoSession
     */
    DaoMaster daoMaster;

    /**
     * 管理所有的XXXDao, DaoSession中也会有增删改查的方法， 其可以直接通过要插入实体的类型找到对应的XXXDao后再进行操作
     */
    DaoSession daoSession;

    /**
     * 对实体进行操作，有比DaoSession更丰富的操作
     */
    GirlDao girlDao;

    private ListView lv;
    private EditText etId;
    private EditText etUrl;
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv);
        etId = (EditText) findViewById(R.id.etId);
        etUrl = (EditText) findViewById(R.id.etUrl);
        etName = (EditText) findViewById(R.id.etName);
        etUrl.setText("http://img4.imgtn.bdimg.com/it/u=4273228609,1931985481&fm=23&gp=0.jpg-->");
        etName.setText("冲田杏梨");

        devOpenHelper = new DaoMaster.DevOpenHelper(this, "girl-db", null);
        db = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        girlDao = daoSession.getGirlDao();
    }

    public void insert(View view) {
        String idString = etId.getText().toString().trim();
        Long id = null;
        String name = etName.getText().toString().trim();
        String url = etUrl.getText().toString();

        if (!idString.isEmpty())
            id = Long.parseLong(idString);

        Girl girl = new Girl(id, name, url);

        /**
         *  //插入note 如果note指定主键与表中已经存在了，就会发生异常
         */
        long rowId = girlDao.insert(girl);
        Toast.makeText(this, "插入后 rowid=" + rowId, Toast.LENGTH_SHORT).show();


        /**
         * 当主键存在的时候会替换，所以能够很好的执行插入操作,推荐
         */
//            girlDao.insertOrReplace(girl);
//            girlDao.save(girl);
//        }

    }

    public void insertOrReplace(View view) {
        String idString = etId.getText().toString().trim();
        Long id = null;
        String name = etName.getText().toString().trim();
        String url = etUrl.getText().toString();

        if (!idString.isEmpty())
            id = Long.parseLong(idString);

        Girl girl = new Girl(id, name, url);
        long rowId = girlDao.insertOrReplace(girl);
        Toast.makeText(this, "插入后 rowid=" + rowId, Toast.LENGTH_SHORT).show();
    }


    public void batchInsert(View view) {
        List<Girl> list = new ArrayList<>();
        list.add(new Girl(null, "柳岩1", "http://www.baidu.com->1"));
        list.add(new Girl(null, "柳岩2", "http://www.baidu.com->2"));
        list.add(new Girl(null, "柳岩3", "http://www.baidu.com->3"));

        girlDao.insertInTx(list);
//        girlDao.insertOrReplaceInTx(list);
    }

    public void bitmapInsert(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://s6.sinaimg.cn/mw690/003CJNRygy6YYZ7rmw515&690");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream is = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] data = new byte[1024];
                    int len;
                    while((len = is.read(data)) != -1){
                        bos.write(data,0,len);
                    }
                    is.close();
                    Girl girl = new Girl(null,"许苗苗","http://s6.sinaimg.cn/mw690/003CJNRygy6YYZ7rmw515&690",bos.toByteArray());
                    girlDao.insert(girl);
                    System.out.println("存储图片完成");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public void delete(View view) {

        Long id = Long.parseLong(etId.getText().toString().trim());
        Girl girl = new Girl(id, "名字随便写", "内容随便写");

        //从数据库中删除给定的实
        girlDao.delete(girl);

        //从数据库中删除给定Key所对应的实体
//        girlDao.deleteByKey(id);

        //实质就是根据key批量删除的
        //girlDao.deleteInTx(new Girl[]{girl1, girl2});

        //删除所有
//        girlDao.deleteAll();

        //使用事务操作删除数据库中给定的所有key所对应的实体
        //girlDao.deleteByKeyInTx(new Long[]{Long.parseLong("1"), Long.parseLong("2")});
    }

    /**
     * 批量删除
     */
    public void deleteBatch(View view) {
        DeleteQuery<Girl> deleteQuery = girlDao.queryBuilder().where(GirlDao.Properties.Id.ge(Long.parseLong("2"))).buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

    public void deleteAll(View view) {
        girlDao.deleteAll();
    }

    public void update(View view) {
        Long id = Long.parseLong(etId.getText().toString().trim());
        Girl gilr = new Girl(id, "update-name", "update-content");
        girlDao.update(gilr);
//        girlDao.updateInTx(new Girl[]{});
    }

    private Query<Girl> query;

    public void query(View view) {

        List<Girl> list;
//        String where = "where NAME=?";
//        String[] args = {"冲田杏梨1"};
//        List<Girl> list = girlDao.queryRaw(where,args);
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i));
//        }

//        Query<Girl> query = girlDao.queryRawCreate(where,args);
//        lv.setAdapter(new MyAdapter(this,query.list()));


        /**
         * 通过QueryBuilder创建一个Query，Query对象可以在一次查询结束后重新使用。相比创建新的Query对象，这样更加高效。
         * 如果查询参数没有改变，可再次调用list/unique方法。如果参数改变，则必须调用setParameter方法修改相应的参数。参数的索引地址从0开始。而索引为参数添加到QueryBuilder的顺序。
         */
        QueryBuilder<Girl> queryBuilder = girlDao.queryBuilder().where(GirlDao.Properties.Id.ge(2));
        queryBuilder.LOG_SQL = true;//打印出SQL语句
        queryBuilder.offset(1);
        queryBuilder.limit(2);//分页

        query = queryBuilder.build();

        //所有实体载入内存，以ArrayList形式返回，使用最简单
        list = query.list();
        lv.setAdapter(new MyAdapter(this, list));

//        query.setParameter(0, 6); // id>= 6
//        list = query.list();

        //实体按需加载到内存。当列表中的其中一个元素第一次被访问，它会被加载并缓存备将来使用。使用完必须关闭。
//        LazyList<Girl> lazyList = query.listLazy();
//        lazyList.close();

//        query.listIterator();
    }

    public void queryReuse(View view) {
        //不需要每次都构建queryBuilder
        lv.setAdapter(new MyAdapter(this, query.list()));
        System.out.println(query.unique());
    }

    public void queryUnique(View view) {
        QueryBuilder<Girl> queryBuilder = girlDao.queryBuilder();
        Girl girl = queryBuilder.where(GirlDao.Properties.Id.eq(1)).unique();
        System.out.println(girl);
    }

    public void queryLazy(View view) {
        //query.list();所有实体载入内存，以ArrayList形式返回，使用最简单。

        /**
         * 实体按需加载到内存。当列表中的其中一个元素第一次被访问，它会被加载并缓存备将来使用。使用完必须关闭。
         */
        LazyList<Girl> list = query.listLazy();
        lv.setAdapter(new MyAdapter(this,list));
        //list.close();
//        query.listLazyUncached();//一个“虚拟”的实体列表：任何访问列表中的元素都会从数据库中读取。使用完必须关闭。
    }

    public void queryAll(View view) {
        List<Girl> list;
//        list = girlDao.loadAll();

        list = girlDao.queryBuilder().build().list();

//        list = daoSession.loadAll(Girl.class);
//        list = daoSession.queryBuilder(Girl.class).build().list();

        MyAdapter adapter = new MyAdapter(this, list);
        lv.setAdapter(adapter);
    }



}
