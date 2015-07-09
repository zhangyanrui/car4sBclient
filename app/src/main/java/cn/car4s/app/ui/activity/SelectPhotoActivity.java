package cn.car4s.app.ui.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout.LayoutParams;
import cn.car4s.app.R;
import cn.car4s.app.util.DeviceUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.*;

public class SelectPhotoActivity extends BaseActivity implements
        OnClickListener {
    public static final int SHOW_LIB = 3;// 3
    protected static final int SCAN_OK = 0;
    // private LinkedHashMap<String, ArrayList<String>> mGroupMap = new
    // LinkedHashMap<String, ArrayList<String>>();
    // private LinkedHashMap<String, ArrayList<String>> thumbGroupMap = new
    // LinkedHashMap<String, ArrayList<String>>();
    //
    private LinkedHashMap<String, List<Map<String, String>>> groupMap = new LinkedHashMap<String, List<Map<String, String>>>();

    private List<Map<String, String>> thumbList = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> photoList = new ArrayList<Map<String, String>>();

    private List<String> thumbLi = new ArrayList<String>();
    private List<String> photoLi = new ArrayList<String>();

    // private ArrayList<ImageBean> imgBeanLists = new ArrayList<ImageBean>();
    private List<String> keys;
    // 所有的图片
    private ArrayList<Map<String, String>> mAllImgs;
    private ArrayList<Map<String, String>> previewImages = new ArrayList<Map<String, String>>();
    ;

    private GridView mGridView;
    private PhotoAdapter mPhotoAdapter;

    private ArrayList<String> selectItems = new ArrayList<String>();


    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SCAN_OK) {
                for (Map<String, String> photo : photoList) {
                    for (Map<String, String> thumb : thumbList) {
                        if (thumb.containsKey("image_id")
                                && photo.containsKey("image_id")
                                && thumb.get("image_id").equals(
                                photo.get("image_id"))) {
                            thumbLi.add(thumb.get("path"));
                            photoLi.add(photo.get("photopath"));
                            break;
                        }
                    }
                }

                for (String path : photoLi) {

                    File pa_file = new File(path).getParentFile();
                    String parentName = pa_file.getAbsolutePath();
                    // 根据父路径名将图片放入到mGruopMap中
                    if (!groupMap.containsKey(parentName)) {
                        ArrayList<String> chileList = new ArrayList<String>();
                        chileList.add(path);
                        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("thumb", thumbLi.get(photoLi.indexOf(path)));
                        map.put("photo", path);
                        list.add(map);
                        mAllImgs.add(map);
                        groupMap.put(parentName, list);
                    } else {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("thumb", thumbLi.get(photoLi.indexOf(path)));
                        map.put("photo", path);
                        mAllImgs.add(map);
                        groupMap.get(parentName).add(map);
                    }
                }

                LinkedHashMap<String, List<Map<String, String>>> sortMap = new LinkedHashMap<String, List<Map<String, String>>>();
                if (mAllImgs != null && mAllImgs.size() > 0)
                    sortMap.put("相机胶卷", mAllImgs);
                keys = new ArrayList<String>();
                keys.add("相机胶卷");
                for (String key : groupMap.keySet()) {
                    if (key.contains("Camera")) {
                        keys.add(key);
                        sortMap.put(key, groupMap.get(key));
                        groupMap.remove(key);
                        break;
                    }
                }
                for (String key : groupMap.keySet()) {
                    keys.add(key);
                    sortMap.put(key, groupMap.get(key));
                }

                groupMap = sortMap;
                sortMap = null;
                previewImages.addAll(groupMap.get("相机胶卷"));// 默认相机胶卷

                mPhotoAdapter.notifyDataSetChanged();
            }
        }
    };
    private TextView cameragroup;
    private Button confirm;
    private boolean isSns;// 是否来自论坛需要
    private PopupWindow mPopupWindow;
    private int maxCount;
    private RelativeLayout titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selectphoto);
        mGridView = (GridView) findViewById(R.id.photogridview);
        confirm = (Button) findViewById(R.id.confirm);
        cameragroup = (TextView) findViewById(R.id.cameragroup);
        titleLayout = (RelativeLayout) findViewById(R.id.title);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        confirm.setOnClickListener(this);
        cameragroup.setOnClickListener(this);
        isSns = getIntent().getBooleanExtra("issns", false);
        maxCount = getIntent().getIntExtra("maxCount", Integer.MAX_VALUE);
        if (isSns) {
            confirm.setVisibility(View.VISIBLE);
            confirm.setEnabled(false);
        } else {
            confirm.setVisibility(View.INVISIBLE);
        }
        mAllImgs = new ArrayList<Map<String, String>>();
        mPhotoAdapter = new PhotoAdapter();
        mGridView.setAdapter(mPhotoAdapter);
        mGridView.setSelector(new ColorDrawable(android.R.color.transparent));
        /**
         * new Thread(new Runnable() {
         *
         * @Override public void run() { Uri mImageUri =
         *           MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
         *           ContentResolver mContentResolver = SelectPhotoActivity.this
         *           .getContentResolver();
         *
         *           // 只查询jpeg和png的图片 Cursor mCursor =
         *           mContentResolver.query(mImageUri, null,
         *           MediaStore.Images.Media.MIME_TYPE + "=? or " +
         *           MediaStore.Images.Media.MIME_TYPE + "=?", new String[] {
         *           "image/jpeg", "image/png" },
         *           MediaStore.Images.Media.DATE_MODIFIED); int i = 0; while
         *           (mCursor.moveToNext()) { // 获取图片的路径 String path =
         *           mCursor.getString(mCursor
         *           .getColumnIndex(MediaStore.Images.Media.DATA));
         *
         *           // 获取该图片的父路径名 File pa_file = new
         *           File(path).getParentFile(); String parentName =
         *           pa_file.getAbsolutePath(); if (mAllImgs.size() < 281) {
         *           mAllImgs.add(path); } // 根据父路径名将图片放入到mGruopMap中 if
         *           (!mGroupMap.containsKey(parentName)) {
         *           System.out.println("parentName " + parentName);
         *           ArrayList<String> chileList = new ArrayList<String>();
         *           chileList.add(path); mGroupMap.put(parentName, chileList);
         *           } else { mGroupMap.get(parentName).add(path); } }
         *
         *           mCursor.close();
         *
         *           // 通知Handler扫描图片完成 mHandler.sendEmptyMessage(SCAN_OK);
         *
         *           } }).start();
         */

        initData();
    }

    class PhotoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return previewImages.size();
        }

        @Override
        public Object getItem(int arg0) {
            return previewImages.get(arg0).get("thumb");
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int position, View convernView,
                            final ViewGroup arg2) {
            ViewHolder mViewHolder = null;
            if (convernView == null) {
                mViewHolder = new ViewHolder();
                convernView = SelectPhotoActivity.this.getLayoutInflater()
                        .inflate(R.layout.selectphoto_item, null);
                mViewHolder.imageView = (ImageView) convernView
                        .findViewById(R.id.photo);
                mViewHolder.checkBox = (CheckBox) convernView
                        .findViewById(R.id.select);
                // if (isSns) {
                // mViewHolder.checkBox.setVisibility(View.VISIBLE);
                // } else {
                // mViewHolder.checkBox.setVisibility(View.INVISIBLE);
                // }
                convernView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convernView.getTag();
            }
            mViewHolder.checkBox
                    .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton button,
                                                     boolean isChecked) {
                            try {

                                if (isChecked) {
                                    if (selectItems != null
                                            && selectItems.size() >= maxCount) {
//                                        UIHelper.showTips("最多选" + maxCount);
                                        button.setChecked(false);
                                        return;
                                    }
                                    if (!selectItems.contains(previewImages
                                            .get(position).get("photo"))) {
                                        selectItems.add(previewImages.get(
                                                position).get("photo"));
                                    }
                                } else {
                                    if (selectItems.contains(previewImages.get(
                                            position).get("photo")))
                                        selectItems.remove(previewImages.get(
                                                position).get("photo"));
                                }

                                if (selectItems != null
                                        && selectItems.size() > 0) {
                                    confirm.setEnabled(true);
                                    confirm.setText("确定(" + selectItems.size() + ")");
                                } else {
                                    confirm.setText("确定");
                                    confirm.setEnabled(false);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            mViewHolder.imageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // if (position == 0) {
                    // startActivity(new Intent(SelectPhotoActivity.this,
                    // CameraActivity.class));
                    // finish();
                    // } else {
                    // Intent intent = new Intent(SelectPhotoActivity.this,
                    // ImagePreviewActivity.class);
                    // intent.putStringArrayListExtra("photoPath",
                    // mAllImgs);
                    // intent.putExtra("curPos", position - 1);
                    // startActivity(intent);

                    try {
                        if (isSns) {
                            // View view = getView(position, layoutView,
                            // arg2);
                            // if
                            // (selectItems.contains(mAllImgs.get(position -
                            // 1))){
                            // selectItems.remove(mAllImgs.get(position -
                            // 1));
                            // ((CheckBox)view.findViewById(R.id.select)).setChecked(false);
                            // }
                            // else{
                            // ((CheckBox)view.findViewById(R.id.select)).setChecked(true);
                            // selectItems.add(mAllImgs.get(position - 1));
                            // }
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra("loadimagepath",
                                    previewImages.get(position).get("photo"));
                            setResult(200, intent);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                // }
            });

            // mViewHolder.imageView.setImageBitmap(BitmapFactory.decodeFile(mAllImgs.get(arg0)));
            LayoutParams lp = new LayoutParams((int) DeviceUtil.getWidth() / 3,
                    (int) DeviceUtil.getWidth() / 3);
            mViewHolder.imageView.setLayoutParams(lp);
            mViewHolder.checkBox.setVisibility(View.INVISIBLE);
            // if (position == 0) {
            // ImageLoader.getInstance().displayImage(
            // null,
            // mViewHolder.imageView,
            // new DisplayImageOptions.Builder().showImageOnLoading(
            // android.R.color.transparent).build());
            // mViewHolder.imageView.setImageDrawable(SelectPhotoActivity.this
            // .getResources().getDrawable(
            // R.drawable.ic_takephoto_take));
            // mViewHolder.checkBox.setVisibility(View.GONE);
            // } else {
            if (isSns)
                mViewHolder.checkBox.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(
                    "file://" + previewImages.get(position).get("thumb"),
                    mViewHolder.imageView,
                    new DisplayImageOptions.Builder().showImageOnLoading(
                            android.R.color.transparent).build());
            // }
            // try {
            if (selectItems.contains(previewImages.get(position).get("photo"))) {
                mViewHolder.checkBox.setChecked(true);
            } else {
                mViewHolder.checkBox.setChecked(false);
            }
            // } catch (Exception e) {
            // e.printStackTrace();
            // }
            return convernView;
        }

    }

    class ViewHolder {
        public ImageView imageView;
        public CheckBox checkBox;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra("loadimagepath", selectItems);
            setResult(200, intent);
            finish();
        } else if (v.getId() == R.id.back) {
            finish();
        } else if (v.getId() == R.id.cameragroup) {
            if (mPopupWindow == null) {
                ListView mListView = new ListView(SelectPhotoActivity.this);
                mListView.setVerticalFadingEdgeEnabled(false);
                mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
                mListView.setAdapter(new ListViewAdapter());
                mListView.setBackgroundColor(Color.WHITE);
                mPopupWindow = new PopupWindow(mListView,
                        LayoutParams.MATCH_PARENT,
                        (int) (DeviceUtil.getHeigh() / 3 * 2));
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        Drawable drawable = getResources().getDrawable(R.drawable.navigationbar_arrow_down);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        cameragroup.setCompoundDrawables(null, null, drawable, null);
                    }
                });
                mListView.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        previewImages.clear();
                        previewImages.addAll(groupMap.get(keys.get(arg2)));
//						selectItems.clear();
                        mPhotoAdapter.notifyDataSetChanged();
                        if (mPopupWindow != null && mPopupWindow.isShowing())
                            mPopupWindow.dismiss();
                    }
                });
            }
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            } else {
                mPopupWindow.showAsDropDown(titleLayout);
                Drawable drawable = getResources().getDrawable(R.drawable.navigationbar_arrow_up);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                cameragroup.setCompoundDrawables(null, null, drawable, null);
            }

        }

    }

    class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return groupMap.size();
        }

        @Override
        public Object getItem(int arg0) {
            return groupMap.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View converView, ViewGroup arg2) {
            ListViewHolder mViewHolder = null;
            if (converView == null) {
                mViewHolder = new ListViewHolder();
                converView = SelectPhotoActivity.this.getLayoutInflater()
                        .inflate(R.layout.photogroup, null);
                mViewHolder.icon = (ImageView) converView
                        .findViewById(R.id.photoicon);
                mViewHolder.name = (TextView) converView
                        .findViewById(R.id.groupname);
                mViewHolder.count = (TextView) converView
                        .findViewById(R.id.count);
                converView.setTag(mViewHolder);
            } else {
                mViewHolder = (ListViewHolder) converView.getTag();
            }

            try {
                List<Map<String, String>> items = groupMap.get(keys
                        .get(position));
                if (items != null && items.size() > 0) {
                    ImageLoader.getInstance().displayImage(
                            "file://"
                                    + groupMap.get(keys.get(position)).get(0)
                                    .get("thumb"), mViewHolder.icon);
                }
                String[] name = keys.get(position).split("/");
                mViewHolder.name.setText(name[name.length - 1]);
                mViewHolder.count.setText("("
                        + groupMap.get(keys.get(position)).size() + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return converView;
        }
    }

    class ListViewHolder {
        ImageView icon;
        TextView name;
        TextView count;
    }

    void initData() {
        getThumbData();
        getMediaImage();

        // 通知Handler扫描图片完成
        mHandler.sendEmptyMessage(SCAN_OK);

    }

    private void getThumbData() {
        ContentResolver cr = getContentResolver();
        String[] projection = {Thumbnails._ID, Thumbnails.IMAGE_ID,
                Thumbnails.DATA};
        Cursor cur = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
                null, null, null);
        if (cur.moveToFirst()) {
            int _id;
            int image_id;
            String image_path;
            int _idColumn = cur.getColumnIndex(Thumbnails._ID);
            int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

            do {
                // Get the field values
                _id = cur.getInt(_idColumn);
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);

                // Do something with the values.
                Log.i("Image", _id + " image_id:" + image_id + " path:"
                        + image_path + "---");
                HashMap<String, String> hash = new HashMap<String, String>();
                hash.put("image_id", image_id + "");
                hash.put("path", image_path);
                thumbList.add(hash);

            } while (cur.moveToNext());

        }
        cur.close();
    }

    private void getMediaImage() {
        String columns[] = new String[]{Media.DATA, Media._ID, Media.TITLE,
                Media.DISPLAY_NAME, Media.SIZE};
        // 得到一个游标
        Cursor cursor = this.getContentResolver().query(
                Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
        if (cursor == null)
            return;
        if (cursor.moveToFirst()) {
            int _id;
            String image_path;
            int _idColumn = cursor.getColumnIndex(Media._ID);
            int dataColumn = cursor.getColumnIndex(Media.DATA);

            do {
                // Get the field values
                _id = cursor.getInt(_idColumn);
                // image_id = cursor.getInt(image_idColumn);
                image_path = cursor.getString(dataColumn);

                // Do something with the values.
                Log.i("Image", _id + " image_id:" + _id + " path:" + image_path
                        + "---");
                HashMap<String, String> hash = new HashMap<String, String>();
                hash.put("image_id", _id + "");
                hash.put("photopath", image_path);
                photoList.add(hash);

            } while (cursor.moveToNext());

        }
        cursor.close();
    }

}
