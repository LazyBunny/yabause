package org.uoyabause.android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.R.attr.id;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/*
typedef struct
        {
        char filename[12];
        char comment[11];
        u8 language;
        u8 year;
        u8 month;
        u8 day;
        u8 hour;
        u8 minute;
        u8 week;
        u32 datasize;
        u16 blocksize;
        } saveinfo_struct;
*/

class BackupItem {
    public String _filename;
    public String _comment;
    public int _language;
    public Date _savedate;
    public int _datasize;
    public int _blocksize;

    public BackupItem(){
    }
    public BackupItem(
            String filename,
            String comment,
            int language,
            Date savedate,
            int datasize,
            int blocksize

    ){
        _filename = filename;
        _comment =  comment;
        _language = language;
        _savedate = savedate;
        _datasize = datasize;
        _blocksize = blocksize;
    }
}

class BackupItemComparator implements Comparator<BackupItem> {
    @Override
    public int compare(BackupItem p1, BackupItem p2) {
        int diff = p1._savedate.compareTo(p2._savedate);
        if( diff > 0 ){
            return -1;
        }
        return 1;
    }
}

class BackupItemInvComparator implements Comparator<BackupItem> {
    @Override
    public int compare(BackupItem p1, BackupItem p2) {
        int diff = p1._savedate.compareTo(p2._savedate);
        if( diff > 0 ){
            return 1;
        }
        return -1;
    }
}

class BackupItemAdapter extends RecyclerView.Adapter<BackupItemAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "BackupItemAdapter";
    private Yabause _yabause;
    private ArrayList<BackupItem> _items = null;
    private RecyclerView mRecycler;
    private int selectpos=0;
    private BackupItemAdapter mAdapter;

    public void setSelected( int pos ){
        selectpos = pos;
    }

    public void setBackupItems( ArrayList<BackupItem> items ){
        this._items = items;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecycler= recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecycler = null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final CardView cardView;
        //private final Toolbar toolbar;

        public ViewHolder(View v) {
            super(v);
            v.setClickable(true);
            textView = (TextView) v.findViewById(R.id.tvName);
            cardView = (CardView) v.findViewById(R.id.cardview);
        }
        public TextView getTextView() {
            return textView;
        }
        public CardView getCardView() {
            return cardView;
        }
        //public Toolbar getToolBar() {
        //    return toolbar;
        //}
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static interface OnItemClickListener {
        public void onItemClick(BackupItemAdapter adapter, int position, BackupItem item);
    }

    @Override
    public void onClick(View view) {
        if (mRecycler == null) {
            return;
        }
        if (mListener != null) {
            int position = mRecycler.getChildAdapterPosition(view);
            BackupItem item = _items.get(position);
            mListener.onItemClick(this, position, item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.backuplist_item, viewGroup, false);

        v.setOnClickListener(this);
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        return new ViewHolder(v);
    }

    public int dp2px( Context cx, int dp) {
        WindowManager wm = (WindowManager) cx
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);
        return (int) (dp * displaymetrics.density + 0.5f);
    }

    static public final String DATE_PATTERN ="yyyy/MM/dd HH:mm:ss";

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
        viewHolder.getTextView().setText(new SimpleDateFormat(DATE_PATTERN).format(_items.get(position)._savedate));
        //viewHolder.getToolBar().setTitle(new SimpleDateFormat(DATE_PATTERN).format(_state_items.get(position)._savedate));
        //Context cx = viewHolder.getImageView().getContext();
        //Glide.with(cx)
        //        .load(new File(_state_items.get(position)._image_filename))
        //        .centerCrop()
        //        .override(dp2px(cx,220),dp2px(cx,220)*3/4)
        //        .into(viewHolder.getImageView());

        if( selectpos == position ){
            //viewHolder.getCardView().setBackgroundColor( cx.getResources().getColor(R.color.selected_background) );
            viewHolder.getCardView().setSelected(true);
        }else{
            //viewHolder.getCardView().setBackgroundColor( cx.getResources().getColor(R.color.default_background) );
            viewHolder.getCardView().setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }

    public void remove( int position ) {
/*
        File file = new File(_items.get(position)._filename);
        if( file != null ){
            file.delete();
        }
        file = new File(_items.get(position)._filename);
        if( file != null ){
            file.delete();
        }
*/
        _items.remove(position);
        notifyItemRemoved(position);
    }
}

class BackupDevice {
    public int id_;
    public String name_;
}


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BackupManagerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BackupManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BackupManagerFragment extends Fragment implements BackupItemAdapter.OnItemClickListener {

    public static String TAG = "BackupManagerFragment";
    private List<BackupDevice> backup_devices_;
    private ArrayList<BackupItem> _items;
    protected RecyclerView recycle_view_;
    private BackupItemAdapter adapter_;
    private LinearLayoutManager layout_manager_;

    private OnFragmentInteractionListener mListener;
    private View v_;


    public BackupManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BackupManagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BackupManagerFragment newInstance(String param1, String param2) {
        BackupManagerFragment fragment = new BackupManagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        String jsonstr;
        jsonstr = YabauseRunnable.getDevicelist();
        backup_devices_ = new ArrayList<BackupDevice>();
        try {

            JSONObject json = new JSONObject(jsonstr);
            JSONArray array = json.getJSONArray("devices");
            for (int i = 0; i < array.length(); i++) {
                JSONObject data = array.getJSONObject(i);
                BackupDevice tmp = new BackupDevice();
                tmp.name_ = data.getString("name");
                tmp.id_= data.getInt("id");
                backup_devices_.add(tmp);
            }

        }catch(JSONException e){
            Log.e(TAG, "Fail to convert to json", e);
        }

        if( backup_devices_.size() == 0 ){
            Log.e(TAG, "Can't find device");
        }



        //backup_devices_[0].name_;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v_ = inflater.inflate(R.layout.fragment_backup_manager, container, false);

        // setup device list
        RadioGroup radioGroup = (RadioGroup) v_.findViewById(R.id.radiogroup);

        RadioButton b1 = (RadioButton)v_.findViewById(R.id.radioButton_internal);
        b1.setText(backup_devices_.get(0).name_);
        if( backup_devices_.size() >= 2 ){
            b1 = (RadioButton)v_.findViewById(R.id.radioButton_external);
            b1.setText(backup_devices_.get(1).name_);
        }else{
            b1 = (RadioButton)v_.findViewById(R.id.radioButton_external);
            b1.setEnabled(false);
        }

        // 指定した ID のラジオボタンをチェックします
        //radioGroup.check(id.radiobutton_green);
        // チェックされているラジオボタンの ID を取得します
        //RadioButton radioButton = (RadioButton) v.findViewById(radioGroup.getCheckedRadioButtonId());
        // ラジオグループのチェック状態が変更された時に呼び出されるコールバックリスナーを登録します
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            // ラジオグループのチェック状態が変更された時に呼び出されます
            // チェック状態が変更されたラジオボタンのIDが渡されます
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) BackupManagerFragment.this.v_.findViewById(checkedId);

                if( checkedId == R.id.radioButton_internal) {
                    updateSaveList( backup_devices_.get(0).id_ );
                }
                else if( checkedId == R.id.radioButton_external) {
                    updateSaveList( backup_devices_.get(1).id_ );
                }
            }
        });

        //updateSaveList( 0 );
        radioGroup.check(R.id.radioButton_internal);

        recycle_view_ = (RecyclerView) v_.findViewById(R.id.recyclerView);
        adapter_ = new BackupItemAdapter();
        adapter_.setBackupItems(_items);
        adapter_.setOnItemClickListener(this);
        recycle_view_.setAdapter(adapter_);
        int scrollPosition = 0;
        if (recycle_view_.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recycle_view_.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        layout_manager_ = new LinearLayoutManager(getActivity());
        recycle_view_.setLayoutManager(layout_manager_);

        v_.setFocusableInTouchMode(true);
        v_.requestFocus();
        //v_.setOnKeyListener(this);

        // Inflate the layout for this fragment
        return v_;
    }

    void updateSaveList( int device ){
        String jsonstr = YabauseRunnable.getFilelist(0);
        _items = new ArrayList<BackupItem>();
        try {
            JSONObject json = new JSONObject(jsonstr);
            JSONArray array = json.getJSONArray("saves");
            for (int i = 0; i < array.length(); i++) {
                JSONObject data = array.getJSONObject(i);
                BackupItem tmp = new BackupItem();
                tmp._filename = data.getString("filename");
                tmp._comment= data.getString("comment");
                tmp._datasize = data.getInt("datasize");
                tmp._blocksize = data.getInt("blocksize");

                String datestr = "";
                datestr += String.format("%04d",data.getInt("year"));
                datestr += String.format("%02d",data.getInt("month"));
                datestr += String.format("%02d",data.getInt("day"));
                datestr += " ";
                datestr += String.format("%02d",data.getInt("hour")) + ":";
                datestr += String.format("%02d",data.getInt("minutes")) + ":00";

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                try{ tmp._savedate = sdf.parse(datestr); }catch( Exception e) {
                    Log.e(TAG, "fail to convert datestr",e);
                    tmp._savedate = new Date();
                }
                _items.add(tmp);
            }

        }catch( JSONException e ){
            Log.e(TAG, "Fail to convert to json", e);
        }
    }

    private int selected_item_ = 0;
    private void selectItem(int position){
        if( position < 0 ) position = 0;
        if( position >= adapter_.getItemCount() ) position = adapter_.getItemCount()-1;

        int pre = selected_item_;

        selected_item_ = position;
        adapter_.setSelected(selected_item_);
        adapter_.notifyItemChanged(pre);
        adapter_.notifyItemChanged(selected_item_);
        recycle_view_.stopScroll();
        recycle_view_.smoothScrollToPosition(position);

/*
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                View v;
                for (int i = 0 ; i< mAdapter.getItemCount() ; ++i){
                    v = mLayoutManager.findViewByPosition(i);
                    if (v != null) {
                        v.setSelected(i == mSelectedItem);
                     }
                }
            }
        });
*/
    }


    public void onButtonPressed(Uri uri) {
 //       if (mListener != null) {
 //           mListener.onFragmentInteraction(uri);
 //       }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
/*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(BackupItemAdapter adapter, int position, BackupItem item) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}