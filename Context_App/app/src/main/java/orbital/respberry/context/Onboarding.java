package orbital.respberry.context;

import android.os.Bundle;
import android.support.annotation.Nullable;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class Onboarding extends MaterialIntroActivity {

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.onboardbackground)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.main_icon)
                .title("Welcome to Context!!?")
                .description("Context enables you to open a website link on your laptop through your phone ")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.onboardbackground)
                .buttonsColor(R.color.onboardingbutton)
                .image(R.drawable.theexenew)
                .title("Additional Software Required..?")
                .description("Remember to download and open the Context.exe file for laptop")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.onboardbackground)
                .buttonsColor(R.color.onboardingbutton)
                .image(R.drawable.context_logo)
                .title("How to get started")
                .description("After logging in, enter or share a website link in the app and click 'Receive' in the exe file")
                .build());


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.onboardbackground)
                .buttonsColor(R.color.onboardingbutton)
                .image(R.drawable.areyouready)
                .description("Thats it for now. Are you ready to begin exploring the app?")
                .build());

    }

        @Override
        public void onFinish() {
        super.onFinish();
    }


}
