/**
 * Copyright CMW Mobile.com, 2010. 
 */
package com.qdcatplayer.main.Setting2;

import java.util.ArrayList;
import java.util.HashMap;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.Setting2.FolderChooserPreference.MyItemClickListener;
import com.qdcatplayer.main.entities.MyFolder;

import android.R.integer;
import android.app.Activity;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The ImageArrayAdapter is the array adapter used for displaying an additional
 * image to a list preference item.
 * @author Casper Wakkers
 */
public class FolderArrayAdapter extends ArrayAdapter<MyFolder> {
	private MyItemClickListener mListener = null;
	
	
	/**
	 * absPath Hashmap for Collapse and Expand
	 */
	private HashMap<String, Boolean> treeMap=null;
	private HashMap<String, ViewHolder> viewHolderMap=null;
	/**
	 * absPath Hashmap for checked tracking
	 */
	private HashMap<String, Boolean> checkedMap=null;
	/**
	 * List of all folders
	 */
	private ArrayList<MyFolder> folders = null;
	
	/**
	 * Root folder when init this Listview
	 */
	private MyFolder root = null;
	/**
	 * List id of pre checked item
	 */
	private Integer[] selected = null;
	public FolderArrayAdapter(Context context, int textViewResourceId,
			ArrayList<MyFolder> folders, Integer[] selected, MyFolder root, MyItemClickListener from) {
		super(context, textViewResourceId, folders);
		
		this.selected = selected;
		this.root=root;
		this.mListener = from;
		this.folders = folders;
		this.viewHolderMap = new HashMap<String, ViewHolder>();
		this.checkedMap = new HashMap<String, Boolean>();
		treeMap = new HashMap<String, Boolean>();
	}
	@Override
	public MyFolder getItem(int position) {
		return folders.get(position);
	}
	/**
	 * my own method
	 * @param obj
	 */
	private void collapseNode(MyFolder obj)
	{
		//remove direct childs first
		for(MyFolder tmp:obj.getChildFolders())
		{
			remove(tmp);
			//remove from hashmap too
			viewHolderMap.remove(tmp.getAbsPath());//very importance
		}
		//check valid to remove all deep-level
		for(int i=0;i<folders.size();i++)
		{
			if(!isValid(folders.get(i)))
			{
				remove(folders.get(i));
				viewHolderMap.remove(folders.get(i).getAbsPath());//very importance
			}
		}
	}
	/**
	 * Check if object should be keeped or not
	 * because parent was missing when collapsed
	 * @param obj
	 * @return
	 */
	private Boolean isValid(MyFolder obj)
	{
		if(obj.getParentFolder().getAbsPath().equals(root.getAbsPath()))
		{
			return true;
		}
		for(MyFolder tmp:folders)
		{
			if(obj.getParentFolder().getAbsPath().equals(tmp.getAbsPath()))
			{
				return true;
			}
		}
		return false;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		CheckBox cb;
		TextView tv;
		ViewHolder holder;
		MyFolder fd=folders.get(position);
		if(convertView==null)
		{
			//LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();//may be fail in some circumstance
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.setting_folder_chooser_item, parent, false);
			
			cb = (CheckBox)convertView.findViewById(
				R.id.checkBox);
			tv = (TextView)convertView.findViewById(R.id.textView_folderName);
			
			holder = new ViewHolder(tv, null, cb, fd, position );
			
			
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					//mListener.onClick("Checkbox");
					//becareful because depening on layout
					//switch to use onclick is not nice
					ViewHolder holder = (ViewHolder)((View) buttonView.getParent()).getTag();
					/**
					 * Muc dich la de biet hien tai cac fd nao duoc check
					 */
					//ViewHolder holder = (ViewHolder)((View) buttonView.getParent()).getTag();
					if(isChecked)
					{
						checkedMap.put(holder.getFd().getAbsPath(), true);
					}
					else
					{
						checkedMap.remove(holder.getFd().getAbsPath());
					}
					tickFolder(holder.getFd(), isChecked);
				}
			});
			cb.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					//ViewHolder holder = (ViewHolder)((View)arg0.getParent()).getTag();
					//set tick dong bo
					//tickFolder(holder.getFd(), ((CheckBox)arg0).isChecked());
				}
			});
			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					
					ViewHolder holder = (ViewHolder)arg0.getTag();
					MyFolder fd = holder.getFd();
					if(treeMap.get(fd.getAbsPath())==null)
					{
						treeMap.put(fd.getAbsPath(), true);
						expandNode(fd);
					}
					else
					{
						treeMap.remove(fd.getAbsPath());
						collapseNode(fd);
					}
					
				}
			});
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
			holder.setFd(fd);
			holder.setPos(position);
			
			tv = holder.getTv();
			cb = (CheckBox)convertView.findViewById(R.id.checkBox);
			
			if(checkedMap.get(fd.getAbsPath())!=null)
			{
				cb.setChecked(true);
			}
			else
			{
				cb.setChecked(false);
			}
		}
		
		
		tv.setText(fd.getAbsPath().replace(fd.getParentFolder().getAbsPath(), fd.getLevelString("-")));
		/**
		 * Set listener for checkbox
		 */
		
		/**
		 * Set listener for TextView
		 */
		
		
		/**
		 * Set ViewHolder for other gate using
		 */
		//khong can thiet lam do co the goi qua getParent de lay
		//cb.setTag(holder);//to know which folder related to this checkbox is checked or not
		convertView.setTag(holder);
		viewHolderMap.put(fd.getAbsPath(), holder);
		return convertView;
		
	}
	private void tickFolder(MyFolder root, Boolean checked) {
		//get holder that present for current root folder
		ViewHolder holder = viewHolderMap.get(root.getAbsPath());//
		//check valid
		if(holder==null)
		{
			return;
		}
		MyFolder fd = holder.getFd();
		//tick tat ca cac con
		for(MyFolder tmp:fd.getChildFolders())
		{
			ViewHolder holder2 = viewHolderMap.get(tmp.getAbsPath());//
			//check valid
			if(holder2==null)
			{
				return;
			}
			holder2.getCb().setChecked(checked);
		}
	}
	private void expandNode(MyFolder obj) {
		Integer pos = getPosition(obj);
		Boolean checked = checkedMap.get(obj.getAbsPath());
		//load all childs of folder
		for(MyFolder item:obj.getChildFolders())
		{
			//holderHashMap will refresh via getView because id changed
			insert(item, pos+1);
			//check parent then childs will add too
			if(checked!=null)
			{
				checkedMap.put(item.getAbsPath(), true);
			}
			else
			{
				checkedMap.remove(item.getAbsPath());
			}
		}
		//must call validate to ensure tick logical		
	}
	
	public class ViewHolder
	{
		private Boolean expanded = false;
		
		private CheckBox cb=null;
		private ImageView img=null;
		private TextView tv=null;
		private MyFolder fd=null;
		private Integer pos=null;
		
		public Integer getPos() {
			return pos;
		}
		public void setPos(Integer pos) {
			this.pos = pos;
		}
		public ViewHolder(TextView tv, ImageView img, CheckBox cb, MyFolder fd, Integer pos) {
			this.tv=tv;
			this.img=img;
			this.cb=cb;
			this.fd=fd;
			this.pos=pos;
		}
		public CheckBox getCb() {
			return cb;
		}
		public ImageView getImg() {
			return img;
		}
		public TextView getTv() {
			return tv;
		}
		public void setCb(CheckBox cb) {
			this.cb = cb;
		}
		public void setImg(ImageView img) {
			this.img = img;
		}
		public void setTv(TextView tv) {
			this.tv = tv;
		}
		public Boolean getExpanded() {
			return expanded;
		}
		public void setExpanded(Boolean expanded) {
			this.expanded = expanded;
		}
		public MyFolder getFd() {
			return fd;
		}
		public void setFd(MyFolder fd) {
			this.fd = fd;
		}
	}
}
