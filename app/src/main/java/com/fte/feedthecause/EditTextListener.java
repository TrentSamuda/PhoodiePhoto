package com.fte.feedthecause;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by trenton on 10/24/16.
 */

public class EditTextListener extends EditText{

    private TextView partner;

    public EditTextListener(Context context, AttributeSet attrs ) {
        super(context, attrs);

    }

    public void setPartner(TextView x){
        partner = x;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {

        if (focused)
            partner.setVisibility(VISIBLE);
        else
            partner.setVisibility(INVISIBLE);

        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }


}
