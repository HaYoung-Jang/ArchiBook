package org.techtown.archivebook;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

// profile_color에 해당하는 drawable 클래스
public class ProfileColor {

    private Context context;
    private String color;

    public Drawable colorDrawable(Context context, String color) {
        final Drawable PROFILE_BLUE = ContextCompat.getDrawable(context, R.drawable.view_circle_blue);
        final Drawable PROFILE_YELLOW = ContextCompat.getDrawable(context, R.drawable.view_circle_yellow);
        final Drawable PROFILE_PINK = ContextCompat.getDrawable(context, R.drawable.view_circle_pink);
        final Drawable PROFILE_GREEN = ContextCompat.getDrawable(context, R.drawable.view_circle_green);
        final Drawable PROFILE_ORANGE = ContextCompat.getDrawable(context, R.drawable.view_circle_orange);
        final Drawable PROFILE_PURPLE = ContextCompat.getDrawable(context, R.drawable.view_circle_purple);

        switch (color) {
            case "blue":
                return PROFILE_BLUE;
            case "yellow":
                return PROFILE_YELLOW;
            case "pink":
                return PROFILE_PINK;
            case "green":
                return PROFILE_GREEN;
            case "orange":
                return PROFILE_ORANGE;
            case "purple":
                return PROFILE_PURPLE;
            default:
                return PROFILE_BLUE;
        }
    }
}
