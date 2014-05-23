package dev.okhotny.TVCalendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.android.volley.toolbox.NetworkImageView;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * Created by sergii on 2014-05-10.
 */
class VolleyCardThumbnail extends CardThumbnail {

    public VolleyCardThumbnail(Context context) {
        super(context, R.layout.main_card_thumbnail);
        setExternalUsage(true);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View viewImage) {
        NetworkImageView image = (NetworkImageView) viewImage;
        image.setImageUrl(getUrlResource(), App.sInstance.imageLoader);
    }

    public void setupImagePopup() {
        getParentCard().addPartialOnClickListener(Card.CLICK_LISTENER_THUMBNAIL_VIEW, new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                showBigImagePopup();
            }
        });
    }

    private void showBigImagePopup() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        NetworkImageView imageView = new NetworkImageView(getContext());
        dialog.setContentView(imageView);
        //get current screen h/w
        Rect displayFrame = new Rect();
        ((Activity) getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(displayFrame);

        int w = displayFrame.width();
        int h = displayFrame.height();

        dialog.getWindow().setLayout(w, h);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        imageView.setImageUrl(getUrlResource(), App.sInstance.imageLoader);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
