package com.example.peerhub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import androidx.fragment.app.Fragment;

public class map extends Fragment {

    private PopupWindow popupWindow;
    private ImageButton dsaButton, webButton, appButton, springButton, mlButton, blockchainButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        // Find the buttons that will trigger the pop-up
        dsaButton = rootView.findViewById(R.id.dsa);
        webButton = rootView.findViewById(R.id.web);
        appButton = rootView.findViewById(R.id.app);
        springButton = rootView.findViewById(R.id.spring);
        mlButton = rootView.findViewById(R.id.ml);
        blockchainButton = rootView.findViewById(R.id.blockchain);

        // Set click listeners for the buttons
        setButtonClickListeners();

        return rootView;
    }

    private void setButtonClickListeners() {
        dsaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, R.drawable.dsa, "DSA Course", "https://www.youtube.com/@codestorywithMIK/playlists", "https://whimsical.com/4th-year-roadmap-to-dream-placement-WB2HTZixtsohXoDcvr6Me7");
            }
        });

        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, R.drawable.webdev, "Web Development Course", "https://www.youtube.com/playlist?list=PLu0W_9lII9agq5TrH9XLIKQvv0iaF2X3w","https://intellipaat.com/blog/web-development-roadmap/");
            }
        });
        appButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, R.drawable.appdev, "APP Development Course", "https://www.youtube.com/watch?v=uEhmQd0Z1CA", "https://roadmap.sh/android");
            }
        });
        springButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, R.drawable.spring, "Spring Boot Development Course", "https://www.youtube.com/playlist?list=PLA3GkZPtsafacdBLdd3p1DyRd5FGfr3Ue", "https://roadmap.sh/spring-boot");
            }
        });
        mlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, R.drawable.ml, "Machine learning Course", "https://www.youtube.com/watch?v=NWONeJKn6kc&t=5s", "https://roadmap.sh/ai-data-scientist");
            }
        });
        blockchainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, R.drawable.blockchain, "Bloackchain Development Course", "https://www.youtube.com/watch?v=6aF6p2VUORE", "https://roadmap.sh/blockchain");
            }
        });
    }

   private void showPopup(View anchorView, int imageResource, String text, final String link1Url, final String link2Url) {
    // Hide the background buttons
    hideBackgroundButtons();

    // Inflate the pop-up layout
    View popupView = getLayoutInflater().inflate(R.layout.pop_layout, null);

    // Create the PopupWindow
    popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    // Set up views in the pop-up
    ImageButton link1Button = popupView.findViewById(R.id.link1Button);
    ImageButton link2Button = popupView.findViewById(R.id.link2Button);

    // Set click listeners for the link buttons
    link1Button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openLink(link1Url);
        }
    });

    link2Button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openLink(link2Url);
        }
    });

    // Measure the anchorView to get its width and height
    anchorView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    int anchorWidth = anchorView.getMeasuredWidth();
    int anchorHeight = anchorView.getMeasuredHeight();

    // Calculate x and y coordinates to center the popup window
    int x = anchorView.getLeft() + (anchorWidth - popupView.getMeasuredWidth()) / 2;
    int y = anchorView.getTop() + (anchorHeight - popupView.getMeasuredHeight()) / 2;

    // Show the popup window at the calculated position
    popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);
}

    private void openLink(String linkUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(linkUrl));
        startActivity(intent);
    }

    private void hideBackgroundButtons() {
        dsaButton.setVisibility(View.GONE);
        webButton.setVisibility(View.GONE);
        appButton.setVisibility(View.GONE);
        springButton.setVisibility(View.GONE);
        mlButton.setVisibility(View.GONE);
        blockchainButton.setVisibility(View.GONE);
    }

    private void showBackgroundButtons() {
        dsaButton.setVisibility(View.VISIBLE);
        webButton.setVisibility(View.VISIBLE);
        appButton.setVisibility(View.VISIBLE);
        springButton.setVisibility(View.VISIBLE);
        mlButton.setVisibility(View.VISIBLE);
        blockchainButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        dismissPopup();
        showBackgroundButtons();
    }

    private void dismissPopup() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}
