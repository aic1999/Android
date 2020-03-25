package com.lmm.comwell;
import cn.bmob.v3.*;
import cn.bmob.v3.datatype.BmobFile;

public class Test extends BmobObject
{
	private String name;
    private BmobFile icon;

	public BmobFile getIcon() {
		return icon;
	}

	public void setIcon(BmobFile icon) {
		this.icon = icon;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}}
