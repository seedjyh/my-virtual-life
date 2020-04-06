package com.seedjyh.myvirtuallife;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SetAttributeValueDialog extends Dialog
{
	public SetAttributeValueDialog(Context context, int default_value, ListViewDataSetter setter)
	{
		super(context);
		default_value_ = default_value;
		setter_ = setter;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_set_attribute_value);

        Init();
        Bind();
    }

    private void Init()
    {
        ((EditText)findViewById(R.id.edittext_id_set_attribute_value_dialog_input_value) ).setText("" + default_value_);
        ok_button_click_listener = new Button.OnClickListener() {
			public void onClick(View v)
			{
				try
				{
					int inputed_value = Integer.parseInt(((EditText)findViewById(R.id.edittext_id_set_attribute_value_dialog_input_value) ).getText().toString() );
					setter_.SetValue(inputed_value);
					SetAttributeValueDialog.this.dismiss();
				}
				catch (NumberFormatException e)
				{
					ShowAlertDialog("Error", e.getMessage() );
				}
				return;
			}
        };
        
        cancel_button_click_listener = new Button.OnClickListener() {
			public void onClick(View v)
			{
				SetAttributeValueDialog.this.dismiss();
				return;
			}
        };
    	return;
    }
    
    private void ShowAlertDialog(String title, String content)
    {
    	AlertDialog.Builder builder = new AlertDialog.Builder(super.getContext() );
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setPositiveButton("OK",
	        new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {;
	            }
	        }
        );
        builder.show();
    	return;
    }
    
    private void Bind()
    {
    	((Button)findViewById(R.id.button_id_set_attribute_value_dialog_ok) ).setOnClickListener(ok_button_click_listener);
    	((Button)findViewById(R.id.button_id_set_attribute_value_dialog_cancel) ).setOnClickListener(cancel_button_click_listener);
    	return;
    }

	private Button.OnClickListener ok_button_click_listener;
	private Button.OnClickListener cancel_button_click_listener;

	private int default_value_; 
	private ListViewDataSetter setter_;
}
