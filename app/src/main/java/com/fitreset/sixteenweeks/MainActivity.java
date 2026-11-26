package com.fitreset.sixteenweeks;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.graphics.Typeface;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.text.InputType;

public class MainActivity extends Activity {
    private static final int BG=Color.rgb(13,20,16), PANEL=Color.rgb(25,36,29), PANEL2=Color.rgb(36,51,41), LIME=Color.rgb(199,242,108), MUTED=Color.rgb(166,183,171), WHITE=Color.WHITE, ORANGE=Color.rgb(255,184,92);
    private SharedPreferences prefs;
    private LinearLayout content, root;
    private int selectedTab=0, week=1, activeSession=0;
    private int[] setDone=new int[6];
    private CountDownTimer restTimer;
    private TextView timerText;
    private boolean sessionActive=false;

    private static class Exercise {
        String id,name,group,equipment,reps,steps,cues,safety,art;
        int sets,rest;
        Exercise(String id,String name,String group,String equipment,int sets,String reps,int rest,String steps,String cues,String safety,String art) {
            this.id=id;this.name=name;this.group=group;this.equipment=equipment;this.sets=sets;this.reps=reps;this.rest=rest;this.steps=steps;this.cues=cues;this.safety=safety;this.art=art;
        }
    }

    private final Exercise[] library = {
        new Exercise("press","Seated Dumbbell Chest Press","CHEST / SEENA","Dumbbells + back-supported bench",2,"8–12",90,
          "1. Bench par baitho, poori back support par aur feet floor par rakho.\n2. Halke dumbbells chest ke paas pakdo; wrists seedhe.\n3. Saans bahar chhodte hue dumbbells ko upar press karo.\n4. Control ke saath neeche lao; comfortable range se aage nahi.",
          "Back ko support par rakho. Press karte waqt saans bahar chhodo; breath hold mat karo. Shoulders ko kaanon ki taraf mat uthao." ,
          "Safety: Doctor/physio ki clearance aur sahi setup ke baad hi try karo. Back pain, tingling, weakness ya breathing symptom aaye toh turant stop.","press"),
        new Exercise("row","Chest-Supported Dumbbell Row","BACK / PEETH","Incline bench + light dumbbells",2,"8–12",90,
          "1. Bench ko comfortable incline par set karo aur chest ko support do.\n2. Arms ko neeche relax rehne do; back round mat karo.\n3. Elbows ko dheere hips ki taraf kheecho.\n4. Chhota pause, phir slowly release karo.",
          "Chest bench par hi supported rahe. Jhatka, twist ya head upar kheenchna avoid karo. Bahut light weight se start karo." ,
          "Physio clearance zaroori. Unsupported bent-over row mat karo. Pair mein pain/tingling jaaye ya symptom badhe toh stop.","row"),
        new Exercise("raise","Seated Lateral Raise","SHOULDERS / KANDHE","Very light dumbbells + back support",2,"10–15",60,
          "1. Back support ke saath seedha baitho; arms sides par.\n2. Arms ko side mein utna hi uthao jitna comfortable ho.\n3. Chhota pause, phir dheere neeche lao.",
          "Bahut light weight lo. Shoulders relaxed aur body stable rakho; swing ya side mein jhukna nahi." ,
          "Clearance ke baad hi. Shoulder/back pain ya breathing issue aaye toh ruk jao.","raise"),
        new Exercise("curl","Seated Biceps Curl","BICEPS / BAazu","Light dumbbells + back support",2,"8–12",60,
          "1. Back support ke saath baitho; elbows sides ke paas.\n2. Upper arms ko stable rakhkar dumbbells curl karo.\n3. 2–3 seconds mein slow neeche lao.",
          "Wrists seedhe rakho; body ko rock ya lower back ko arch mat karo. Curl karte waqt saans bahar chhodo." ,
          "Clearance ke baad hi. Aisa halka load lo jisme normal breathing aur stable posture bana rahe.","curl"),
        new Exercise("pushdown","Band Triceps Pressdown","TRICEPS / BAazu","Resistance band, secure anchor",2,"10–15",60,
          "1. Comfortable aur balanced stance mein khade ho.\n2. Elbows ko body ke paas rakho.\n3. Elbows seedhe karke hands ko neeche press karo.\n4. Bina jhuke ya twist kiye dheere return karo.",
          "Band ka anchor strong aur secure hona chahiye; pehle halka test karo. Movement controlled rakho." ,
          "Doctor/physio clearance ke baad hi. Apna body weight band par mat daalo.","pushdown"),
        new Exercise("kneeext","Seated Knee Extension","QUADS / JAANGH","Chair; bodyweight ya very light band",2,"10–12 each leg",60,
          "1. Chair par poori back support ke saath baitho; feet floor par.\n2. Ek knee ko dheere seedha karo, comfortable range tak.\n3. Chhota pause karke control se neeche lao.\n4. Ab doosri leg.",
          "Pelvis aur back supported rakho. Kick mat karo aur heavy ankle weights mat lagao." ,
          "Is lower-body movement ko physio se approve karao. Back ya leg symptom aaye toh turant stop.","knee"),
        new Exercise("wallpush","Wall Push-up","CHEST + ARMS","Stable wall",2,"8–12",60,
          "1. Stable wall ke saamne khade ho; hands chest height par.\n2. Thoda peeche step lo; body ek line mein rakho.\n3. Elbows bend karke chest ko wall ki taraf lao.\n4. Dheere se wall ko push karke wapas aao.",
          "Chhota range rakho aur lower back neutral. Wall push karte waqt saans bahar chhodo." ,
          "Tumhari condition mein bodyweight exercise bhi doctor/physio se clear honi chahiye. Back ya breathing symptom aaye toh stop.","wall"),
        new Exercise("bandrow","Seated Band Row","BACK / PEETH","Resistance band + stable anchor",2,"10–12",60,
          "1. Feet supported karke seedha baitho; band chest height par secure karo.\n2. Arms aage, shoulders relaxed rakho.\n3. Bina peeche jhuke elbows ko back kheecho.\n4. Dheere starting position par aao.",
          "Seedhe baitho; forward round ya peeche rock mat karo. Light band use karo." ,
          "Clearance aur secure anchor ke baad hi. Sitting se back/leg symptom badhe toh mat karo.","row"),
        new Exercise("external","Seated Band External Rotation","SHOULDER SUPPORT","Light resistance band",2,"10–12",45,
          "1. Seedha baitho; elbows 90° bend karke sides ke paas.\n2. Hands ko saamne rakho.\n3. Elbows paas rakhte hue hands ko dheere alag karo.\n4. Slowly starting position par lao.",
          "Sirf pain-free chhota range. Trunk twist ya shoulders ko force mat karo." ,
          "Physio se review aur exercise clearance ke baad hi use karo.","raise"),
        new Exercise("hamcurl","Seated Band Hamstring Curl","HAMSTRINGS / PICHLI JAANGH","Chair + light band",2,"10–12 each leg",60,
          "1. Back support ke saath baitho; light band ko secure anchor karo.\n2. Band ka doosra end ankle par lagao.\n3. Heel ko halki resistance ke saath chair ke neeche peeche lao.\n4. Dheere return karo.",
          "Back supported aur movement smooth rakho. Har set se pehle anchor check karo." ,
          "Physio approval zaroori. Back pain ya pair mein failta symptom ho toh avoid/stop.","knee"),
        new Exercise("calf","Supported Calf Raise","CALVES / PINḌLI","Stable chair ya counter",2,"10–15",45,
          "1. Stable chair/counter ke peeche khade ho aur lightly hold karo.\n2. Feet hip-width par rakho.\n3. Pairon ke panjon par dheere utho.\n4. Bina bounce kiye slow neeche aao.",
          "Torso seedha rakho; extra weight mat lo. Balance unsure ho toh stop." ,
          "Clearance ke baad hi; stable support use karo aur symptom-free range rakho.","calf"),
        new Exercise("hammer","Seated Hammer Curl","BICEPS + FOREARMS","Very light dumbbells + back support",2,"8–12",60,
          "1. Back support ke saath baitho; palms ek doosre ki taraf.\n2. Dono dumbbells ko shoulders ki taraf curl karo.\n3. Elbows paas rakhkar dheere neeche lao.",
          "Swing ya back arch mat karo. Light weight lo aur continuously breathe karte raho." ,
          "Doctor/physio clearance ke baad hi. Zor se brace karna ya breath hold karna pade toh stop.","curl")
    };

    private final int[][] sessions={{0,1,2,3,4,5},{6,7,8,9,10,11},{0,1,2,3,5,10}};
    private final String[] sessionNames={"DAY A · CHEST + BACK + ARMS","DAY B · CONTROL + LEGS","DAY C · FULL BODY LIGHT"};
    private final String[] weekdays={"MON","WED","FRI"};

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs=getSharedPreferences("fitreset_v3",0);
        getWindow().setStatusBarColor(BG);getWindow().setNavigationBarColor(BG);
        root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(BG);
        LinearLayout header=new LinearLayout(this);header.setOrientation(LinearLayout.VERTICAL);header.setPadding(20,18,20,15);GradientDrawable headerBg=new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{Color.rgb(44,77,49),Color.rgb(22,36,27)});headerBg.setCornerRadius(new float[]{0,0,26,26,26,26,0,0});header.setBackground(headerBg);
        header.addView(text("16-WEEK BODY RESET · HINGLISH COACH",11,LIME,true));
        header.addView(text("FIT RESET.",29,WHITE,true));
        header.addView(text("Smart training  •  Muscle build  •  Progress track",13,MUTED,false));
        root.addView(header);
        ScrollView scroll=new ScrollView(this);content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(15,14,15,18);scroll.addView(content);
        root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout nav=new LinearLayout(this);nav.setBackgroundColor(Color.rgb(20,31,24));nav.setPadding(2,5,2,5);
        String[] tabs={"HOME","PLAN","EXERCISE","PROGRESS","DIET"};
        for(int i=0;i<tabs.length;i++){final int ix=i;Button b=new Button(this);b.setText(tabs[i]);b.setTextSize(9);b.setAllCaps(false);b.setTextColor(LIME);b.setBackgroundColor(Color.TRANSPARENT);b.setPadding(2,8,2,8);b.setOnClickListener(v->{showTab(ix);});nav.addView(b,new LinearLayout.LayoutParams(0,-2,1));}
        root.addView(nav);setContentView(root);showHome();
    }

    private TextView text(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);if(bold)t.setTypeface(null,Typeface.BOLD);return t;}
    private LinearLayout card(){LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(14,13,14,13);GradientDrawable surface=new GradientDrawable();surface.setColor(PANEL);surface.setCornerRadius(22);surface.setStroke(1,Color.rgb(48,67,53));c.setBackground(surface);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.bottomMargin=11;c.setLayoutParams(p);return c;}
    private void add(View v){content.addView(v);}
    private void title(String s){TextView t=text(s,19,WHITE,true);t.setPadding(2,8,2,12);add(t);}
    private Button btn(String s,Runnable action){Button b=new Button(this);b.setText(s);b.setTextSize(13);b.setAllCaps(false);b.setTextColor(Color.rgb(18,29,19));GradientDrawable gd=new GradientDrawable();gd.setColor(LIME);gd.setCornerRadius(16);b.setBackground(gd);b.setPadding(18,10,18,10);b.setOnClickListener(v->action.run());return b;}
    private EditText field(String hint,int type){EditText e=new EditText(this);e.setSingleLine(true);e.setHint(hint);e.setInputType(type);e.setTextColor(WHITE);e.setHintTextColor(MUTED);return e;}
    private void showTab(int i){selectedTab=i;sessionActive=false;if(restTimer!=null){restTimer.cancel();restTimer=null;}if(i==0)showHome();else if(i==1)showTraining();else if(i==2)showLibrary();else if(i==3)showProgress();else showFood();}
    private boolean cleared(){return prefs.getBoolean("clinician_clearance",false);}
    private void clearanceCard(){
        LinearLayout c=card();c.addView(text("PEHLE SAFETY, PHIR TRAINING",12,ORANGE,true));
        c.addView(text(cleared()?"Doctor/physio clearance note hai. Har session se pehle apne symptoms check karo.":"Strength workout shuru karne se pehle doctor/physio se L4–L5 back aur breathing/asthma ke hisaab se clearance lo.",14,WHITE,true));
        c.addView(text("App tumhari back ya breathing check nahi kar sakta. Exercise library dekh sakte ho, lekin strength session tabhi unlock hoga jab doctor/physio clearance confirm karoge.",12,MUTED,false));
        add(c);
        if(!cleared())add(btn("Doctor/physio ne clearance diya hai",()->new AlertDialog.Builder(this).setTitle("Clearance confirm karo").setMessage("Kya doctor/physio ne tumhari current back aur breathing condition review karke resistance training aur ye exercises approve ki hain? Sirf aaj theek feel karne par YES select mat karna.").setNegativeButton("Abhi nahi",null).setPositiveButton("Haan, clearance mila hai", (d,w)->{prefs.edit().putBoolean("clinician_clearance",true).apply();showHome();}).show()));
    }
    private void showHome(){
        content.removeAllViews();int sessionsDone=prefs.getInt("sessions_done",0);
        LinearLayout hero=card();hero.addView(text("TERA 16-WEEK MISSION",11,LIME,true));hero.addView(text("Muscle build karo. Body fat gradual kam karo.",23,WHITE,true));hero.addView(text("Current phase: "+(week<=4?"BASE":week<=8?"BUILD":week<=12?"PROGRESS":"CONSOLIDATE"),13,MUTED,false));hero.addView(text("Week "+week+" / 16  ·  "+sessionsDone+" workouts complete",13,MUTED,false));hero.addView(btn("AAJ KA WORKOUT DEKHO",()->showTraining()));add(hero);
        LinearLayout grid=new LinearLayout(this);grid.setOrientation(LinearLayout.HORIZONTAL);
        grid.addView(stat("WORKOUTS",String.valueOf(sessionsDone)),new LinearLayout.LayoutParams(0,-2,1));
        grid.addView(stat("WEEK",week+"/16"),new LinearLayout.LayoutParams(0,-2,1));
        grid.addView(stat("CLEARANCE",cleared()?"DONE":"PENDING"),new LinearLayout.LayoutParams(0,-2,1));add(grid);
        clearanceCard();
        LinearLayout guide=card();guide.addView(text("PLAN KAISE CHALEGA?",16,LIME,true));guide.addView(text("Har week 3 non-consecutive strength days (A/B/C). Baaki din sirf comfortable walking/recovery, agar doctor ne allow kiya ho. Har exercise ke 2 sets se plan diya hai. Reps slow aur controlled. Weight badhane ki zabardasti nahi.",13,WHITE,false));add(guide);
        LinearLayout notice=card();notice.addView(text("GOAL: STRONG AUR LEAN BODY — SAFE TAREEKE SE",14,LIME,true));notice.addView(text("Abs visible hona body-fat, muscle, genetics aur time par depend karta hai. App progress track karega; 16 weeks mein six-pack ki guarantee nahi de sakta.",12,MUTED,false));add(notice);
        add(btn("EXERCISES AUR FORM DEKHO",()->showLibrary()));
    }
    private View stat(String name,String value){LinearLayout c=card();c.setPadding(10,10,10,10);c.addView(text(name,10,MUTED,true));c.addView(text(value,17,WHITE,true));return c;}

    private void showTraining(){
        content.removeAllViews();title("TERA WEEKLY WORKOUT PLAN");
        LinearLayout phase=card();phase.addView(text("WEEK "+week+" / 16",14,LIME,true));
        SeekBar seek=new SeekBar(this);seek.setMax(15);seek.setProgress(week-1);seek.setProgressTintList(android.content.res.ColorStateList.valueOf(LIME));seek.setThumbTintList(android.content.res.ColorStateList.valueOf(LIME));seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){public void onProgressChanged(SeekBar s,int p,boolean u){week=p+1;}public void onStartTrackingTouch(SeekBar s){}public void onStopTrackingTouch(SeekBar s){showTraining();}});
        phase.addView(seek);phase.addView(text(week<=4?"Week 1–4 · Basics seekho, form samjho":week<=8?"Week 5–8 · Same form ke saath consistency":week<=12?"Week 9–12 · Controlled strength build": "Week 13–16 · Progress review aur recovery",13,MUTED,false));add(phase);
        clearanceCard();
        String[] info={"Chest press · supported row · shoulder raise · biceps curl · triceps band · seated knee extension","Wall push-up · seated band row · shoulder rotation · hamstring curl · calf raise · hammer curl","Selected upper-body moves repeat + light lower-body option"};
        for(int i=0;i<3;i++){final int ix=i;LinearLayout c=card();c.addView(text(weekdays[i]+"  ·  "+sessionNames[i],14,LIME,true));c.addView(text(info[i],13,WHITE,false));c.addView(text("Approx. 25–40 min · 2 sets · slow aur controlled pace",11,MUTED,false));if(cleared())c.addView(btn("READINESS CHECK KARO & START",()->readiness(ix)));else c.addView(text("LOCKED · doctor/physio clearance zaroori",12,ORANGE,true));add(c);}
        LinearLayout walk=card();walk.addView(text("LIGHT WALK / CARDIO",15,LIME,true));walk.addView(text("Sirf tab easy pace par walk karo jab breathing normal ho aur clinician ne allow kiya ho. Jitni duration pehle se comfortably tolerate hoti hai, wahi se start karo. Steps ya speed ka pressure mat lo.",13,WHITE,false));walk.addView(btn("WALKING MINUTES ADD KARO",()->logWalk()));add(walk);
        LinearLayout rest=card();rest.addView(text("REST & RECOVERY DAYS",15,LIME,true));rest.addView(text("Halka daily movement (agar allowed ho), proper sleep, regular meals aur rest. Recovery bhi training ka part hai.",13,MUTED,false));add(rest);
    }

    private void readiness(int which){
        LinearLayout box=new LinearLayout(this);box.setOrientation(1);box.setPadding(8,4,8,4);
        CheckBox back=new CheckBox(this);back.setText("Aaj unusual back/leg pain, numbness ya weakness nahi hai");back.setTextColor(WHITE);back.setButtonTintList(android.content.res.ColorStateList.valueOf(LIME));
        CheckBox breath=new CheckBox(this);breath.setText("Breathing normal baseline par hai; wheezing/chest tightness nahi");breath.setTextColor(WHITE);breath.setButtonTintList(android.content.res.ColorStateList.valueOf(LIME));
        CheckBox meds=new CheckBox(this);meds.setText("Mera prescribed reliever inhaler/action plan paas hai (agar applicable)");meds.setTextColor(WHITE);meds.setButtonTintList(android.content.res.ColorStateList.valueOf(LIME));
        box.addView(text("Unwell ho, symptoms badh rahe hon ya doubt ho toh workout start mat karo.",13,ORANGE,true));box.addView(back);box.addView(breath);box.addView(meds);
        AlertDialog dialog=new AlertDialog.Builder(this).setTitle("Workout se pehle readiness check").setView(box).setNegativeButton("Cancel",null).setPositiveButton("Continue",null).create();
        dialog.setOnShowListener(d->{Button ok=dialog.getButton(AlertDialog.BUTTON_POSITIVE);ok.setEnabled(false);android.widget.CompoundButton.OnCheckedChangeListener l=(b,v)->ok.setEnabled(back.isChecked()&&breath.isChecked()&&meds.isChecked());back.setOnCheckedChangeListener(l);breath.setOnCheckedChangeListener(l);meds.setOnCheckedChangeListener(l);ok.setOnClickListener(v->{dialog.dismiss();startWorkout(which);});});
        dialog.show();
    }

    private void startWorkout(int which){activeSession=which;setDone=new int[6];sessionActive=true;renderWorkout();}
    private void renderWorkout(){
        content.removeAllViews();title(sessionNames[activeSession]);
        LinearLayout intro=card();intro.addView(text("AAJ KA SESSION",11,LIME,true));intro.addView(text("Har set comfortable effort se karo. 60–90 sec ya zyada rest lo—next set tabhi jab breathing normal ho.",13,WHITE,false));intro.addView(btn("SESSION END KARO",()->finishWorkout()));add(intro);
        int[] ids=sessions[activeSession];
        for(int pos=0;pos<ids.length;pos++){final int ix=pos;Exercise e=library[ids[pos]];
            LinearLayout c=card();c.setOrientation(1);c.addView(new ExerciseSketch(this,e.art),new LinearLayout.LayoutParams(-1,125));
            c.addView(text(e.name,16,WHITE,true));c.addView(text(e.group+"  ·  "+e.equipment,11,MUTED,false));c.addView(text(e.sets+" sets  ·  "+e.reps+" reps  ·  rest "+e.rest+" sec",12,LIME,true));
            c.addView(text("Complete: "+setDone[pos]+" / "+e.sets+" sets",12,MUTED,false));
            c.addView(btn("FORM AUR DETAILS DEKHO",()->exerciseDetails(e)));
            if(setDone[pos]<e.sets)c.addView(btn("SET "+(setDone[pos]+1)+" COMPLETE",()->{setDone[ix]++;renderWorkout();if(setDone[ix]<e.sets)startRest(e.rest);}));
            else c.addView(text("✓ EXERCISE COMPLETE",13,LIME,true));
            add(c);
        }
        LinearLayout end=card();end.addView(text("Jaldi mat karo. Pain, unusual breathlessness ya chest symptoms ke saath workout continue mat karo.",12,ORANGE,true));end.addView(btn("WORKOUT FINISH & SAVE",()->finishWorkout()));add(end);
    }
    private void startRest(int seconds){
        if(restTimer!=null)restTimer.cancel();
        LinearLayout c=card();timerText=text("REST: "+seconds+" sec",20,LIME,true);c.addView(timerText);
        c.addView(btn("REST SKIP",()->{if(restTimer!=null)restTimer.cancel();timerText.setText("Rest skip ho gaya — ready feel ho tabhi continue karo.");}));
        content.addView(c,Math.min(content.getChildCount(),2));
        restTimer=new CountDownTimer(seconds*1000L,1000){public void onTick(long ms){if(timerText!=null)timerText.setText("REST: "+(ms/1000)+" sec");}public void onFinish(){if(timerText!=null)timerText.setText("REST COMPLETE — comfortable ho tab continue karo");}}.start();
    }
    private void finishWorkout(){
        if(restTimer!=null){restTimer.cancel();restTimer=null;}int total=0;for(int i=0;i<6;i++)total+=setDone[i];
        if(total==0){new AlertDialog.Builder(this).setMessage("Koi set log nahi hua. Bina save kiye end karein?").setNegativeButton("Workout continue",null).setPositiveButton("End", (d,w)->{sessionActive=false;showTraining();}).show();return;}
        prefs.edit().putInt("sessions_done",prefs.getInt("sessions_done",0)+1).putInt("last_sets",total).putInt("week",week).apply();
        sessionActive=false;new AlertDialog.Builder(this).setTitle("Workout save ho gaya!").setMessage(total+" sets record hue. Ab recover karo, paani piyo aur symptoms note karo.").setPositiveButton("Done",(d,w)->showHome()).show();
    }

    private void showLibrary(){
        content.removeAllViews();title("EXERCISE LIBRARY · EXERCISE BANK");
        LinearLayout intro=card();intro.addView(text("PEHLE FORM · LIGHT WEIGHT · NO STRAIN",12,LIME,true));intro.addView(text("Kisi exercise par tap karo: movement sketch, step-by-step Hinglish guide, form tips aur safety note milega. Ye demo guide hai, personal physio prescription nahi.",13,MUTED,false));add(intro);
        String[] filters={"ALL","CHEST","BACK","SHOULDER","ARMS","LEGS"};
        LinearLayout f=new LinearLayout(this);f.setOrientation(LinearLayout.HORIZONTAL);
        for(String filter:filters){Button b=new Button(this);b.setText(filter);b.setTextSize(9);b.setTextColor(LIME);b.setAllCaps(false);b.setBackgroundColor(PANEL2);b.setOnClickListener(v->{showLibraryFilter(filter);});f.addView(b,new LinearLayout.LayoutParams(0,-2,1));}
        add(f);showLibraryItems("ALL");
    }
    private void showLibraryFilter(String filter){content.removeAllViews();title("EXERCISE BANK · "+filter);add(btn("← SAARI EXERCISES",()->showLibrary()));showLibraryItems(filter);}
    private void showLibraryItems(String filter){
        for(Exercise e:library){boolean ok=filter.equals("ALL")||(filter.equals("ARMS")&&(e.group.contains("BICEPS")||e.group.contains("TRICEPS")||e.group.contains("ARMS")||e.group.contains("FOREARMS")))||e.group.contains(filter);if(!ok)continue;
            LinearLayout c=card();c.addView(new ExerciseSketch(this,e.art),new LinearLayout.LayoutParams(-1,120));c.addView(text(e.name,15,WHITE,true));c.addView(text(e.group+" · "+e.equipment,11,MUTED,false));c.addView(btn("DEMO & HINGLISH GUIDE",()->exerciseDetails(e)));add(c);
        }
    }
    private void exerciseDetails(Exercise e){
        ScrollView sc=new ScrollView(this);LinearLayout box=new LinearLayout(this);box.setOrientation(1);box.setPadding(12,8,12,8);box.addView(new ExerciseSketch(this,e.art),new LinearLayout.LayoutParams(-1,175));box.addView(text(e.group+" · "+e.equipment,12,LIME,true));
        box.addView(text("SETUP AUR MOVEMENT",13,WHITE,true));box.addView(text(e.steps,14,MUTED,false));box.addView(text("FORM TIPS",13,WHITE,true));box.addView(text(e.cues,14,MUTED,false));box.addView(text("SAFETY NOTE",13,ORANGE,true));box.addView(text(e.safety,13,MUTED,false));sc.addView(box);
        new AlertDialog.Builder(this).setTitle(e.name+"  ·  "+e.sets+" × "+e.reps).setView(sc).setPositiveButton("BAND KARO",null).show();
    }

    private void showProgress(){
        content.removeAllViews();title("BODY PROGRESS TRACKER");
        LinearLayout c=card();c.addView(text("BODY WEIGHT (KG)",12,LIME,true));EditText weight=field("Example: 68.0",InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);weight.setText(prefs.getString("weight",""));c.addView(weight);
        c.addView(text("WAIST (CM)",12,LIME,true));EditText waist=field("Har baar same point par measure karo",InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);waist.setText(prefs.getString("waist",""));
        c.addView(text("CHEST (CM)",12,LIME,true));EditText chest=field("Optional",InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);chest.setText(prefs.getString("chest",""));
        c.addView(text("ARM (CM)",12,LIME,true));EditText arm=field("Optional",InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);arm.setText(prefs.getString("arm",""));
        c.addView(btn("MEASUREMENTS SAVE KARO",()->{prefs.edit().putString("weight",weight.getText().toString()).putString("waist",waist.getText().toString()).putString("chest",chest.getText().toString()).putString("arm",arm.getText().toString()).apply();Toast.makeText(this,"Measurements phone par save ho gayi",Toast.LENGTH_SHORT).show();}));
        add(c);
        LinearLayout log=card();log.addView(text("RECOVERY CHECK-IN",14,LIME,true));EditText sleep=field("Kal raat kitni neend? (hours)",InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);sleep.setText(prefs.getString("sleep",""));log.addView(sleep);EditText energy=field("Aaj energy level (1–5)",InputType.TYPE_CLASS_NUMBER);energy.setText(prefs.getString("energy",""));log.addView(energy);EditText pain=field("Aaj back/leg symptoms? (short note)",InputType.TYPE_CLASS_TEXT);pain.setText(prefs.getString("symptoms",""));log.addView(pain);log.addView(btn("RECOVERY CHECK-IN SAVE",()->{prefs.edit().putString("sleep",sleep.getText().toString()).putString("energy",energy.getText().toString()).putString("symptoms",pain.getText().toString()).apply();Toast.makeText(this,"Check-in save ho gaya",Toast.LENGTH_SHORT).show();}));add(log);
        LinearLayout walk=card();walk.addView(text("IS WEEK WALKING MINUTES",13,LIME,true));walk.addView(text(prefs.getInt("walk_minutes",0)+" min logged",19,WHITE,true));walk.addView(btn("WALKING TIME ADD KARO",()->logWalk()));add(walk);
        LinearLayout stats=card();stats.addView(text("SAME TAREEKE SE MEASURE KARO",13,LIME,true));stats.addView(text("Har baar same time aur same tape placement rakho. Weight daily up-down ho sakta hai; ek reading ke bajay weekly trend dekho.",12,MUTED,false));add(stats);
    }
    private void logWalk(){
        EditText mins=field("Kitne minutes walk ki?",InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(this).setTitle("Easy walk log karo").setView(mins).setNegativeButton("Cancel",null).setPositiveButton("SAVE",(d,w)->{int n=0;try{n=Integer.parseInt(mins.getText().toString());}catch(Exception ignored){}if(n>0)prefs.edit().putInt("walk_minutes",prefs.getInt("walk_minutes",0)+n).apply();Toast.makeText(this,"Walking time save ho gaya",Toast.LENGTH_SHORT).show();}).show();
    }

    private void showFood(){
        content.removeAllViews();title("DIET & NUTRITION GUIDE");
        LinearLayout c=card();c.addView(text("APNA DAILY TARGET SET KARO",13,LIME,true));EditText protein=field("Daily protein target (grams)",InputType.TYPE_CLASS_NUMBER);protein.setText(prefs.getString("protein_goal",""));EditText calories=field("Calories target (optional)",InputType.TYPE_CLASS_NUMBER);calories.setText(prefs.getString("calorie_goal",""));c.addView(protein);c.addView(calories);c.addView(btn("TARGETS SAVE KARO",()->{prefs.edit().putString("protein_goal",protein.getText().toString()).putString("calorie_goal",calories.getText().toString()).apply();Toast.makeText(this,"Targets save ho gaye",Toast.LENGTH_SHORT).show();}));add(c);
        LinearLayout intake=card();intake.addView(text("AAJ KA FOOD LOG",14,LIME,true));EditText eaten=field("Aaj kitna protein khaya? (grams)",InputType.TYPE_CLASS_NUMBER);eaten.setText(prefs.getString("protein_today",""));intake.addView(eaten);EditText meals=field("Aaj ke meals (eggs, dal, chicken etc.)",InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);meals.setText(prefs.getString("food_notes",""));intake.addView(meals);
        intake.addView(btn("AAJ KA FOOD SAVE KARO",()->{prefs.edit().putString("protein_today",eaten.getText().toString()).putString("food_notes",meals.getText().toString()).apply();Toast.makeText(this,"Food log save ho gaya",Toast.LENGTH_SHORT).show();}));add(intake);
        LinearLayout guide=card();guide.addView(text("SIMPLE MEAL PLATE",14,LIME,true));guide.addView(text("Har main meal mein protein + sabzi/fruit + carbs + fluids include karo. Options: eggs, milk, dahi, paneer, dal, rajma, chicken, fish, soy, rice, oats aur fruits.",13,WHITE,false));add(guide);
        LinearLayout caution=card();caution.addView(text("CRASH DIET BILKUL NAHI",13,ORANGE,true));caution.addView(text("Starvation diet, dehydration tricks ya extreme cutting mat karo. Calorie deficit har kisi ke liye suitable nahi hota. Medical condition ho toh dietitian se targets confirm karo.",12,MUTED,false));add(caution);
    }

    private class ExerciseSketch extends View {
        private final Paint p=new Paint(3);private final String type;
        ExerciseSketch(Activity a,String type){super(a);this.type=type;setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
        private void line(Canvas c,float x1,float y1,float x2,float y2,int color,float sw){p.setColor(color);p.setStrokeWidth(sw);p.setStyle(Paint.Style.STROKE);p.setStrokeCap(Paint.Cap.ROUND);c.drawLine(x1,y1,x2,y2,p);}
        @Override protected void onDraw(Canvas canvas){
            super.onDraw(canvas);float w=getWidth(),h=getHeight();p.setStyle(Paint.Style.FILL);p.setColor(PANEL2);canvas.drawRoundRect(new RectF(0,0,w,h),18,18,p);
            float x=w*.5f,head=h*.22f,shoulder=h*.38f,hip=h*.62f,knee=h*.80f;
            p.setColor(LIME);canvas.drawCircle(x,head,10,p);
            line(canvas,x,head+11,x,hip,WHITE,6);
            line(canvas,x,hip,x-27,knee,WHITE,6);line(canvas,x,knee,x-30,h*.91f,WHITE,6);
            line(canvas,x,hip,x+27,knee,WHITE,6);line(canvas,x+27,knee,x+32,h*.91f,WHITE,6);
            line(canvas,x-32,h*.92f,x+36,h*.92f,MUTED,3);
            if(type.equals("press")){line(canvas,x,shoulder,x-30,shoulder+12,LIME,6);line(canvas,x-30,shoulder+12,x-38,h*.23f,LIME,6);line(canvas,x,shoulder,x+30,shoulder+12,LIME,6);line(canvas,x+30,shoulder+12,x+38,h*.23f,LIME,6);}
            else if(type.equals("row")){line(canvas,x,shoulder,x-32,h*.48f,LIME,6);line(canvas,x-32,h*.48f,x-16,h*.48f,LIME,6);line(canvas,x,shoulder,x+32,h*.48f,LIME,6);line(canvas,x+32,h*.48f,x+16,h*.48f,LIME,6);}
            else if(type.equals("raise")){line(canvas,x,shoulder,x-65,h*.33f,LIME,6);line(canvas,x,shoulder,x+65,h*.33f,LIME,6);}
            else if(type.equals("curl")){line(canvas,x,shoulder,x-34,h*.48f,LIME,6);line(canvas,x-34,h*.48f,x-23,h*.31f,LIME,6);line(canvas,x,shoulder,x+34,h*.48f,LIME,6);line(canvas,x+34,h*.48f,x+23,h*.31f,LIME,6);}
            else if(type.equals("pushdown")){line(canvas,x,shoulder,x-23,h*.52f,LIME,6);line(canvas,x-23,h*.52f,x-23,h*.68f,LIME,6);line(canvas,x,shoulder,x+23,h*.52f,LIME,6);line(canvas,x+23,h*.52f,x+23,h*.68f,LIME,6);}
            else if(type.equals("knee")){line(canvas,x,shoulder,x-35,h*.49f,LIME,6);line(canvas,x-35,h*.49f,x-50,h*.40f,LIME,6);line(canvas,x,hip,x+30,h*.72f,WHITE,6);line(canvas,x+30,h*.72f,x+75,h*.72f,LIME,6);}
            else if(type.equals("wall")){line(canvas,x,shoulder,x-50,h*.52f,LIME,6);line(canvas,x-50,h*.52f,x-60,h*.47f,LIME,6);line(canvas,x,shoulder,x+50,h*.52f,LIME,6);line(canvas,x+50,h*.52f,x+60,h*.47f,LIME,6);}
            else if(type.equals("calf")){line(canvas,x,hip,x-27,knee,WHITE,6);line(canvas,x-27,knee,x-30,h*.88f,LIME,6);line(canvas,x,hip,x+27,knee,WHITE,6);line(canvas,x+27,knee,x+35,h*.85f,LIME,6);line(canvas,x+58,h*.90f,x+58,h*.69f,ORANGE,3);line(canvas,x+52,h*.75f,x+58,h*.69f,ORANGE,3);line(canvas,x+64,h*.75f,x+58,h*.69f,ORANGE,3);}
            else {line(canvas,x,shoulder,x-37,h*.48f,LIME,6);line(canvas,x-37,h*.48f,x-23,h*.50f,LIME,6);line(canvas,x,shoulder,x+37,h*.48f,LIME,6);line(canvas,x+37,h*.48f,x+23,h*.50f,LIME,6);}
            p.setStyle(Paint.Style.FILL);p.setColor(MUTED);p.setTextSize(10);canvas.drawText("MOVEMENT SKETCH · READ THE FORM CUES BELOW",12,h-9,p);
        }
    }
}
