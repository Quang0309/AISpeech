package com.example.admin.ai_speech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.library.bubbleview.BubbleTextView;

import java.util.List;

/**
 * Created by ADMIN on 28-Aug-17.
 */

public class CustomAdapter extends BaseAdapter {
    private List<chatmodel> list_chat_models;
    private Context context;
    private LayoutInflater layoutInflater;

    public CustomAdapter(List<chatmodel> list_chat_models, Context context) {
        this.list_chat_models = list_chat_models;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list_chat_models.size();
    }

    @Override
    public Object getItem(int position) {
        return list_chat_models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;
        if (view == null || this.list_chat_models.get(position).isSend != ((ViewHolder)view.getTag()).isSend) {
            if (list_chat_models.get(position).isSend) {
                view = layoutInflater.inflate(R.layout.mess_send, null);
            } else
                view = layoutInflater.inflate(R.layout.mess_recv, null);
            holder = new ViewHolder();
            holder.isSend = this.list_chat_models.get(position).isSend;
            view.setTag(holder);
            BubbleTextView text_message = (BubbleTextView) view.findViewById(R.id.text_message);
            text_message.setText(list_chat_models.get(position).message);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }
        return view;
    }
}
class ViewHolder
{
    public boolean isSend;
}