package com.niko.moneycounter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class AuthorActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_author);

    initViews();

    initListeners();
  }

  private void initViews() {
    Toolbar toolbar = findViewById(R.id.authorToolbar);
    toolbar.setTitle(R.string.author);
  }

  private void initListeners() {
    findViewById(R.id.vk).setOnClickListener(view -> {
      try {
        Intent intent = new Intent(Intent.ACTION_VIEW,
            Uri.parse("vkontakte://profile/nikodroid"));
        startActivity(intent);
      } catch (ActivityNotFoundException ex) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
            Uri.parse("https://vk.com/nikodroid"));
        startActivity(intent);
      }
    });

    findViewById(R.id.git).setOnClickListener(view -> {
      Intent intent = new Intent(Intent.ACTION_VIEW,
          Uri.parse("https://github.com/NikolayGst"));
      startActivity(intent);
    });
  }
}
