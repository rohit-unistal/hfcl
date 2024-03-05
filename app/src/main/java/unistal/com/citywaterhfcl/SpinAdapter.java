package unistal.com.citywaterhfcl;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SpinAdapter extends BaseAdapter {
        private ArrayList<ItemModel> allNodes;
        private Context context;
        private LayoutInflater inflater;
        int intHideIndex=-1;

        public SpinAdapter(Context ctx, ArrayList<ItemModel> allNodes) {
            context= ctx;
            this.allNodes = allNodes;
        }

        @Override
        public int getCount() {
            return allNodes.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }



    class Holder{
            private TextView tvName;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View myView = null;
            try {
                Holder holder;
                myView = convertView;
                if (myView == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    myView = inflater.inflate(R.layout.customlayoutspinner, null);

                    holder = new Holder();
                    holder.tvName = (TextView) myView.findViewById(R.id.tvName);
                    myView.setTag(holder);
                } else {
                    holder = (Holder) myView.getTag();
                }

                holder.tvName.setText(allNodes.get(i).getName());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return myView;
        }

        // Below code is used to hide first item.

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View v = null;
            if (position == intHideIndex) {
                TextView tv = new TextView(context);
                tv.setVisibility(View.GONE);
                tv.setHeight(0);
                v = tv;
                v.setVisibility(View.GONE);
            }
            else        v = super.getDropDownView(position, null, parent);
            return v;

        }

    }

