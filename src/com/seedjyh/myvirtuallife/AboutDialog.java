package com.seedjyh.myvirtuallife;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AboutDialog extends Dialog
{
	public AboutDialog(Context context)
	{
		super(context);
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_about);

        Init();
        Bind();
    }

    private void Init()
    {
        ok_button_click_listener = new Button.OnClickListener() {
			public void onClick(View v)
			{
				AboutDialog.this.dismiss();
				return;
			}
        };
    	return;
    }
    
    private void Bind()
    {
    	((Button)findViewById(R.id.button_id_about_dialog_ok) ).setOnClickListener(ok_button_click_listener);
    	return;
    }

	private Button.OnClickListener ok_button_click_listener;
}
