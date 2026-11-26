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
        new Exercise("press","Seated dumbbell chest press","CHEST","Dumbbells + bench with back support",2,"8–12",90,
          "1. Sit with your upper and lower back supported and feet flat.\n2. Hold very light dumbbells near chest level; wrists stay straight.\n3. Press up smoothly without arching your lower back.\n4. Lower slowly only as far as comfortable.",
          "Keep your back against the support. Exhale as you press; do not hold your breath. Stop before your shoulders shrug.",
          "Resistance exercise: use only after your clinician/physiotherapist has cleared it and shown a comfortable setup. Stop for back pain, tingling, weakness or breathing symptoms.","press"),
        new Exercise("row","Chest-supported dumbbell row","BACK","Incline bench + light dumbbells",2,"8–12",90,
          "1. Set the bench to a comfortable incline and lie chest-supported.\n2. Let arms hang without rounding your back.\n3. Pull elbows gently toward your hips.\n4. Pause briefly, then lower slowly.",
          "Chest stays on the pad; avoid jerking, twisting or lifting your head. Begin with very light weights.",
          "Requires clinician/physio clearance. Do not perform a bent-over unsupported row; discontinue if symptoms travel into a leg or worsen.","row"),
        new Exercise("raise","Supported seated lateral raise","SHOULDERS","Very light dumbbells + back support",2,"10–15",60,
          "1. Sit tall with your back supported and arms by your sides.\n2. Raise arms out to the sides only to a comfortable height.\n3. Pause briefly, then lower slowly.",
          "Use very light weights. Keep shoulders relaxed and trunk still; no swinging or leaning.",
          "Resistance exercise: only after clearance. Stop for shoulder, back or breathing symptoms.","raise"),
        new Exercise("curl","Seated dumbbell biceps curl","BICEPS","Light dumbbells + back support",2,"8–12",60,
          "1. Sit with back supported, elbows close to your sides.\n2. Curl the dumbbells without moving your upper arms.\n3. Lower over 2–3 seconds.",
          "Keep wrists straight; do not rock or arch your back. Exhale during the curl.",
          "Use only after clearance. Choose a load that lets you breathe normally and keep your torso still.","curl"),
        new Exercise("pushdown","Resistance-band triceps pressdown","TRICEPS","Resistance band securely anchored above",2,"10–15",60,
          "1. Stand upright in a comfortable, balanced stance.\n2. Keep elbows near your sides.\n3. Press hands down by straightening elbows.\n4. Return slowly without leaning or twisting.",
          "Use a secure anchor; test it gently first. Keep the movement small and controlled.",
          "Only after clinician/physio clearance. Do not lean your body weight into the band.","pushdown"),
        new Exercise("kneeext","Seated knee extension","QUADRICEPS","Chair; bodyweight or very light band",2,"10–12 each leg",60,
          "1. Sit fully supported with both feet on the floor.\n2. Straighten one knee slowly until comfortable.\n3. Pause briefly, then lower with control.\n4. Alternate legs.",
          "Keep your pelvis and back supported. No kicking or heavy ankle weights.",
          "A lower-body option to review with your physiotherapist before use; stop if it causes back or leg symptoms.","knee"),
        new Exercise("wallpush","Wall push-up","CHEST + ARMS","Wall",2,"8–12",60,
          "1. Face a stable wall and place hands at chest height.\n2. Step back a short distance; keep body in one line.\n3. Bend elbows and bring chest toward wall.\n4. Push away smoothly.",
          "Keep the range short and the lower back neutral. Exhale as you push away.",
          "Even bodyweight work should be cleared with your clinician in your situation. Stop if back or breathing symptoms appear.","wall"),
        new Exercise("bandrow","Seated resistance-band row","BACK","Resistance band + stable anchor",2,"10–12",60,
          "1. Sit upright with feet supported and band anchored at chest height.\n2. Hold the band with arms forward but shoulders relaxed.\n3. Draw elbows back without leaning.\n4. Return slowly.",
          "Stay tall; do not round forward or rock backward. Use a light band.",
          "Only after clearance and with a secure anchor. Do not use if sitting posture increases back/leg symptoms.","row"),
        new Exercise("external","Seated band external rotation","ROTATOR CUFF","Light resistance band",2,"10–12",45,
          "1. Sit upright with elbows bent 90 degrees and tucked at your sides.\n2. Start with hands in front.\n3. Move hands gently apart while elbows stay near ribs.\n4. Return slowly.",
          "Small pain-free range; do not twist your trunk or force the shoulders.",
          "Review with physiotherapist and use only after exercise clearance.","raise"),
        new Exercise("hamcurl","Seated band hamstring curl","HAMSTRINGS","Chair + light band",2,"10–12 each leg",60,
          "1. Sit supported with one end of a light band secured safely in front.\n2. Loop the other end around one ankle.\n3. Draw the heel back under the chair against light resistance.\n4. Return slowly.",
          "Keep your back supported and movement smooth. Check the anchor before each set.",
          "Physiotherapist approval required; avoid if it provokes back or radiating leg symptoms.","knee"),
        new Exercise("calf","Supported calf raise","CALVES","Stable chair or counter",2,"10–15",45,
          "1. Stand behind a stable support and hold it lightly.\n2. Keep feet hip-width apart.\n3. Rise onto the balls of your feet.\n4. Lower slowly without bouncing.",
          "Keep your torso upright; do not use extra weight. Stop if balance is uncertain.",
          "Only after clearance; choose a stable support and a range that does not provoke symptoms.","calf"),
        new Exercise("hammer","Seated hammer curl","BICEPS + FOREARMS","Very light dumbbells + back support",2,"8–12",60,
          "1. Sit with your back supported and palms facing inward.\n2. Curl both hands toward shoulders.\n3. Lower slowly while keeping elbows close to your sides.",
          "No swinging or back arching. Use a light load and breathe continuously.",
          "Only after clinician clearance; stop if you need to brace hard or hold your breath.","curl")
    };

    private final int[][] sessions={{0,1,2,3,4,5},{6,7,8,9,10,11},{0,1,2,3,5,10}};
    private final String[] sessionNames={"WORKOUT A · PUSH + PULL","WORKOUT B · CONTROL + LEGS","WORKOUT C · FULL BODY"};
    private final String[] weekdays={"MON","WED","FRI"};

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs=getSharedPreferences("fitreset_v3",0);
        getWindow().setStatusBarColor(BG);getWindow().setNavigationBarColor(BG);
        root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(BG);
        LinearLayout header=new LinearLayout(this);header.setOrientation(LinearLayout.VERTICAL);header.setPadding(20,18,20,15);header.setBackgroundColor(Color.rgb(34,53,39));
        header.addView(text("16-WEEK BODY RESET",11,LIME,true));
        header.addView(text("FIT RESET.",29,WHITE,true));
        header.addView(text("Train with control · Build strength · Track change",13,MUTED,false));
        root.addView(header);
        ScrollView scroll=new ScrollView(this);content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(15,14,15,18);scroll.addView(content);
        root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout nav=new LinearLayout(this);nav.setBackgroundColor(Color.rgb(20,31,24));nav.setPadding(2,5,2,5);
        String[] tabs={"HOME","TRAIN","EXERCISES","PROGRESS","FOOD"};
        for(int i=0;i<tabs.length;i++){final int ix=i;Button b=new Button(this);b.setText(tabs[i]);b.setTextSize(9);b.setAllCaps(false);b.setTextColor(LIME);b.setBackgroundColor(Color.TRANSPARENT);b.setPadding(2,8,2,8);b.setOnClickListener(v->{showTab(ix);});nav.addView(b,new LinearLayout.LayoutParams(0,-2,1));}
        root.addView(nav);setContentView(root);showHome();
    }

    private TextView text(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);if(bold)t.setTypeface(null,Typeface.BOLD);return t;}
    private LinearLayout card(){LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(14,13,14,13);c.setBackgroundColor(PANEL);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.bottomMargin=11;c.setLayoutParams(p);return c;}
    private void add(View v){content.addView(v);}
    private void title(String s){TextView t=text(s,19,WHITE,true);t.setPadding(2,8,2,12);add(t);}
    private Button btn(String s,Runnable action){Button b=new Button(this);b.setText(s);b.setTextSize(13);b.setAllCaps(false);b.setTextColor(Color.rgb(18,29,19));b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(LIME));b.setOnClickListener(v->action.run());return b;}
    private EditText field(String hint,int type){EditText e=new EditText(this);e.setSingleLine(true);e.setHint(hint);e.setInputType(type);e.setTextColor(WHITE);e.setHintTextColor(MUTED);return e;}
    private void showTab(int i){selectedTab=i;sessionActive=false;if(restTimer!=null){restTimer.cancel();restTimer=null;}if(i==0)showHome();else if(i==1)showTraining();else if(i==2)showLibrary();else if(i==3)showProgress();else showFood();}
    private boolean cleared(){return prefs.getBoolean("clinician_clearance",false);}
    private void clearanceCard(){
        LinearLayout c=card();c.addView(text("YOUR SAFETY GATE",12,ORANGE,true));
        c.addView(text(cleared()?"Clinician clearance recorded. Still check symptoms before each session.":"Before resistance workouts, ask your doctor/physiotherapist to approve your L4–L5 and breathing status, plus these exercise variations.",14,WHITE,true));
        c.addView(text("The app cannot assess your spine, asthma control or current symptoms. Browsing exercises is available now; starting a resistance session stays locked until you confirm clinician clearance.",12,MUTED,false));
        add(c);
        if(!cleared())add(btn("I have clinician clearance",()->new AlertDialog.Builder(this).setTitle("Confirm clearance").setMessage("Has your doctor or physiotherapist reviewed your current back and breathing condition and cleared you for resistance training and these exercise types? Do not select this based only on feeling okay today.").setNegativeButton("Not yet",null).setPositiveButton("Yes, I have clearance", (d,w)->{prefs.edit().putBoolean("clinician_clearance",true).apply();showHome();}).show()));
    }
    private void showHome(){
        content.removeAllViews();int sessionsDone=prefs.getInt("sessions_done",0);
        LinearLayout hero=card();hero.addView(text("YOUR 16-WEEK GOAL",11,LIME,true));hero.addView(text("Build muscle. Reduce body fat.",23,WHITE,true));hero.addView(text("Current phase: "+(week<=4?"FOUNDATION":week<=8?"BUILD":week<=12?"DEVELOP":"CONSOLIDATE"),13,MUTED,false));hero.addView(text("Week "+week+" of 16  ·  "+sessionsDone+" sessions logged",13,MUTED,false));hero.addView(btn("OPEN TODAY'S TRAINING",()->showTraining()));add(hero);
        LinearLayout grid=new LinearLayout(this);grid.setOrientation(LinearLayout.HORIZONTAL);
        grid.addView(stat("SESSIONS",String.valueOf(sessionsDone)),new LinearLayout.LayoutParams(0,-2,1));
        grid.addView(stat("WEEK",week+"/16"),new LinearLayout.LayoutParams(0,-2,1));
        grid.addView(stat("CLEARANCE",cleared()?"YES":"PENDING"),new LinearLayout.LayoutParams(0,-2,1));add(grid);
        clearanceCard();
        LinearLayout guide=card();guide.addView(text("HOW THIS PROGRAM WORKS",16,LIME,true));guide.addView(text("3 non-consecutive strength days per week (A/B/C), with comfortable walking on other days. Start with 2 sets per exercise. Keep reps smooth and stop well before straining. There is no forced weight increase.",13,WHITE,false));add(guide);
        LinearLayout notice=card();notice.addView(text("THE GOAL IS A STRONGER, LEANER BODY — SAFELY",14,LIME,true));notice.addView(text("A visible six-pack depends on body-fat level, muscle, genetics and time. This app tracks habits and progress; it cannot promise a particular look in 16 weeks.",12,MUTED,false));add(notice);
        add(btn("VIEW EXERCISE DEMONSTRATIONS",()->showLibrary()));
    }
    private View stat(String name,String value){LinearLayout c=card();c.setPadding(10,10,10,10);c.addView(text(name,10,MUTED,true));c.addView(text(value,17,WHITE,true));return c;}

    private void showTraining(){
        content.removeAllViews();title("YOUR WEEKLY TRAINING");
        LinearLayout phase=card();phase.addView(text("WEEK "+week+" / 16",14,LIME,true));
        SeekBar seek=new SeekBar(this);seek.setMax(15);seek.setProgress(week-1);seek.setProgressTintList(android.content.res.ColorStateList.valueOf(LIME));seek.setThumbTintList(android.content.res.ColorStateList.valueOf(LIME));seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){public void onProgressChanged(SeekBar s,int p,boolean u){week=p+1;}public void onStartTrackingTouch(SeekBar s){}public void onStopTrackingTouch(SeekBar s){showTraining();}});
        phase.addView(seek);phase.addView(text(week<=4?"Weeks 1–4 · Learn setup and control":week<=8?"Weeks 5–8 · Repeat with consistent form":week<=12?"Weeks 9–12 · Build repeatable strength": "Weeks 13–16 · Consolidate and review",13,MUTED,false));add(phase);
        clearanceCard();
        String[] info={"Chest press · supported row · lateral raise · curl · band pressdown · seated knee extension","Wall push-up · band row · external rotation · band hamstring curl · calf raise · hammer curl","Repeat key upper-body patterns with a lighter lower-body option"};
        for(int i=0;i<3;i++){final int ix=i;LinearLayout c=card();c.addView(text(weekdays[i]+"  ·  "+sessionNames[i],14,LIME,true));c.addView(text(info[i],13,WHITE,false));c.addView(text("Approx. 25–40 min · 2 sets each · controlled pace",11,MUTED,false));if(cleared())c.addView(btn("CHECK READINESS & START",()->readiness(ix)));else c.addView(text("LOCKED · clinician clearance required",12,ORANGE,true));add(c);}
        LinearLayout walk=card();walk.addView(text("EASY WALKING / CARDIO",15,LIME,true));walk.addView(text("Choose an easy, conversational pace only if breathing is settled and your clinician has approved it. Begin with a short duration that you already tolerate; do not chase step targets or speed.",13,WHITE,false));walk.addView(btn("LOG WALKING MINUTES",()->logWalk()));add(walk);
        LinearLayout rest=card();rest.addView(text("RECOVERY DAYS",15,LIME,true));rest.addView(text("Gentle daily movement, adequate sleep, regular meals and rest. A rest day is part of the plan.",13,MUTED,false));add(rest);
    }

    private void readiness(int which){
        LinearLayout box=new LinearLayout(this);box.setOrientation(1);box.setPadding(8,4,8,4);
        CheckBox back=new CheckBox(this);back.setText("No unusual back/leg pain, numbness or weakness today");back.setTextColor(WHITE);back.setButtonTintList(android.content.res.ColorStateList.valueOf(LIME));
        CheckBox breath=new CheckBox(this);breath.setText("Breathing is at my usual baseline; no wheeze/chest tightness");breath.setTextColor(WHITE);breath.setButtonTintList(android.content.res.ColorStateList.valueOf(LIME));
        CheckBox meds=new CheckBox(this);meds.setText("My prescribed reliever/action plan is available, if applicable");meds.setTextColor(WHITE);meds.setButtonTintList(android.content.res.ColorStateList.valueOf(LIME));
        box.addView(text("Do not start if you are unwell, symptoms are flaring, or you are unsure.",13,ORANGE,true));box.addView(back);box.addView(breath);box.addView(meds);
        AlertDialog dialog=new AlertDialog.Builder(this).setTitle("Session readiness").setView(box).setNegativeButton("Cancel",null).setPositiveButton("Continue",null).create();
        dialog.setOnShowListener(d->{Button ok=dialog.getButton(AlertDialog.BUTTON_POSITIVE);ok.setEnabled(false);android.widget.CompoundButton.OnCheckedChangeListener l=(b,v)->ok.setEnabled(back.isChecked()&&breath.isChecked()&&meds.isChecked());back.setOnCheckedChangeListener(l);breath.setOnCheckedChangeListener(l);meds.setOnCheckedChangeListener(l);ok.setOnClickListener(v->{dialog.dismiss();startWorkout(which);});});
        dialog.show();
    }

    private void startWorkout(int which){activeSession=which;setDone=new int[6];sessionActive=true;renderWorkout();}
    private void renderWorkout(){
        content.removeAllViews();title(sessionNames[activeSession]);
        LinearLayout intro=card();intro.addView(text("TODAY'S SESSION",11,LIME,true));intro.addView(text("Complete each set at a comfortable effort. Rest 60–90 seconds or longer until breathing is back to normal.",13,WHITE,false));intro.addView(btn("END SESSION",()->finishWorkout()));add(intro);
        int[] ids=sessions[activeSession];
        for(int pos=0;pos<ids.length;pos++){final int ix=pos;Exercise e=library[ids[pos]];
            LinearLayout c=card();c.setOrientation(1);c.addView(new ExerciseSketch(this,e.art),new LinearLayout.LayoutParams(-1,125));
            c.addView(text(e.name,16,WHITE,true));c.addView(text(e.group+"  ·  "+e.equipment,11,MUTED,false));c.addView(text(e.sets+" sets  ·  "+e.reps+" reps  ·  rest "+e.rest+" sec",12,LIME,true));
            c.addView(text("Completed: "+setDone[pos]+" / "+e.sets+" sets",12,MUTED,false));
            c.addView(btn("EXERCISE FORM & DETAILS",()->exerciseDetails(e)));
            if(setDone[pos]<e.sets)c.addView(btn("COMPLETE SET "+(setDone[pos]+1),()->{setDone[ix]++;renderWorkout();if(setDone[ix]<e.sets)startRest(e.rest);}));
            else c.addView(text("✓ EXERCISE COMPLETE",13,LIME,true));
            add(c);
        }
        LinearLayout end=card();end.addView(text("Take your time. Never train through pain, unusual breathlessness or chest symptoms.",12,ORANGE,true));end.addView(btn("FINISH & SAVE SESSION",()->finishWorkout()));add(end);
    }
    private void startRest(int seconds){
        if(restTimer!=null)restTimer.cancel();
        LinearLayout c=card();timerText=text("REST: "+seconds+" sec",20,LIME,true);c.addView(timerText);
        c.addView(btn("SKIP REST",()->{if(restTimer!=null)restTimer.cancel();timerText.setText("Rest skipped — resume only when ready.");}));
        content.addView(c,Math.min(content.getChildCount(),2));
        restTimer=new CountDownTimer(seconds*1000L,1000){public void onTick(long ms){if(timerText!=null)timerText.setText("REST: "+(ms/1000)+" sec");}public void onFinish(){if(timerText!=null)timerText.setText("REST COMPLETE — resume when comfortable");}}.start();
    }
    private void finishWorkout(){
        if(restTimer!=null){restTimer.cancel();restTimer=null;}int total=0;for(int i=0;i<6;i++)total+=setDone[i];
        if(total==0){new AlertDialog.Builder(this).setMessage("No sets were logged. End without saving?").setNegativeButton("Continue workout",null).setPositiveButton("End", (d,w)->{sessionActive=false;showTraining();}).show();return;}
        prefs.edit().putInt("sessions_done",prefs.getInt("sessions_done",0)+1).putInt("last_sets",total).putInt("week",week).apply();
        sessionActive=false;new AlertDialog.Builder(this).setTitle("Session saved").setMessage(total+" sets recorded. Recover, hydrate and note any symptoms.").setPositiveButton("Done",(d,w)->showHome()).show();
    }

    private void showLibrary(){
        content.removeAllViews();title("EXERCISE LIBRARY");
        LinearLayout intro=card();intro.addView(text("FORM FIRST · LIGHT LOADS · NO STRAIN",12,LIME,true));intro.addView(text("Tap any movement to see the illustrated setup, step-by-step instructions, cues and safety notes. These are previews, not a personalised physiotherapy prescription.",13,MUTED,false));add(intro);
        String[] filters={"ALL","CHEST","BACK","SHOULDERS","ARMS","LEGS"};
        LinearLayout f=new LinearLayout(this);f.setOrientation(LinearLayout.HORIZONTAL);
        for(String filter:filters){Button b=new Button(this);b.setText(filter);b.setTextSize(9);b.setTextColor(LIME);b.setAllCaps(false);b.setBackgroundColor(PANEL2);b.setOnClickListener(v->{showLibraryFilter(filter);});f.addView(b,new LinearLayout.LayoutParams(0,-2,1));}
        add(f);showLibraryItems("ALL");
    }
    private void showLibraryFilter(String filter){content.removeAllViews();title("EXERCISE LIBRARY · "+filter);add(btn("← ALL EXERCISES",()->showLibrary()));showLibraryItems(filter);}
    private void showLibraryItems(String filter){
        for(Exercise e:library){boolean ok=filter.equals("ALL")||(filter.equals("ARMS")&&(e.group.contains("BICEPS")||e.group.contains("TRICEPS")||e.group.contains("ARMS")||e.group.contains("FOREARMS")))||e.group.contains(filter);if(!ok)continue;
            LinearLayout c=card();c.addView(new ExerciseSketch(this,e.art),new LinearLayout.LayoutParams(-1,120));c.addView(text(e.name,15,WHITE,true));c.addView(text(e.group+" · "+e.equipment,11,MUTED,false));c.addView(btn("VIEW DEMO & INSTRUCTIONS",()->exerciseDetails(e)));add(c);
        }
    }
    private void exerciseDetails(Exercise e){
        ScrollView sc=new ScrollView(this);LinearLayout box=new LinearLayout(this);box.setOrientation(1);box.setPadding(12,8,12,8);box.addView(new ExerciseSketch(this,e.art),new LinearLayout.LayoutParams(-1,175));box.addView(text(e.group+" · "+e.equipment,12,LIME,true));
        box.addView(text("SETUP & MOVEMENT",13,WHITE,true));box.addView(text(e.steps,14,MUTED,false));box.addView(text("FORM CUES",13,WHITE,true));box.addView(text(e.cues,14,MUTED,false));box.addView(text("SAFETY",13,ORANGE,true));box.addView(text(e.safety,13,MUTED,false));sc.addView(box);
        new AlertDialog.Builder(this).setTitle(e.name+"  ·  "+e.sets+" × "+e.reps).setView(sc).setPositiveButton("CLOSE",null).show();
    }

    private void showProgress(){
        content.removeAllViews();title("BODY & PERFORMANCE TRACKER");
        LinearLayout c=card();c.addView(text("BODY WEIGHT (KG)",12,LIME,true));EditText weight=field("e.g. 68.0",InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);weight.setText(prefs.getString("weight",""));c.addView(weight);
        c.addView(text("WAIST (CM)",12,LIME,true));EditText waist=field("Measure at the same point each time",InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);waist.setText(prefs.getString("waist",""));
        c.addView(text("CHEST (CM)",12,LIME,true));EditText chest=field("Optional",InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);chest.setText(prefs.getString("chest",""));
        c.addView(text("ARM (CM)",12,LIME,true));EditText arm=field("Optional",InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);arm.setText(prefs.getString("arm",""));
        c.addView(btn("SAVE MEASUREMENTS",()->{prefs.edit().putString("weight",weight.getText().toString()).putString("waist",waist.getText().toString()).putString("chest",chest.getText().toString()).putString("arm",arm.getText().toString()).apply();Toast.makeText(this,"Measurements saved on this phone",Toast.LENGTH_SHORT).show();}));
        add(c);
        LinearLayout log=card();log.addView(text("RECOVERY CHECK-IN",14,LIME,true));EditText sleep=field("Sleep last night (hours)",InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);sleep.setText(prefs.getString("sleep",""));log.addView(sleep);EditText energy=field("Energy today (1–5)",InputType.TYPE_CLASS_NUMBER);energy.setText(prefs.getString("energy",""));log.addView(energy);EditText pain=field("Back/leg symptoms today (brief note)",InputType.TYPE_CLASS_TEXT);pain.setText(prefs.getString("symptoms",""));log.addView(pain);log.addView(btn("SAVE RECOVERY CHECK-IN",()->{prefs.edit().putString("sleep",sleep.getText().toString()).putString("energy",energy.getText().toString()).putString("symptoms",pain.getText().toString()).apply();Toast.makeText(this,"Check-in saved",Toast.LENGTH_SHORT).show();}));add(log);
        LinearLayout walk=card();walk.addView(text("WALKING MINUTES THIS WEEK",13,LIME,true));walk.addView(text(prefs.getInt("walk_minutes",0)+" min logged",19,WHITE,true));walk.addView(btn("ADD WALKING MINUTES",()->logWalk()));add(walk);
        LinearLayout stats=card();stats.addView(text("MEASURE CONSISTENTLY",13,LIME,true));stats.addView(text("Use similar time, conditions and tape placement. Weight can fluctuate day to day; look at longer trends rather than one reading.",12,MUTED,false));add(stats);
    }
    private void logWalk(){
        EditText mins=field("Minutes walked",InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(this).setTitle("Log comfortable walking").setView(mins).setNegativeButton("Cancel",null).setPositiveButton("SAVE",(d,w)->{int n=0;try{n=Integer.parseInt(mins.getText().toString());}catch(Exception ignored){}if(n>0)prefs.edit().putInt("walk_minutes",prefs.getInt("walk_minutes",0)+n).apply();Toast.makeText(this,"Walking minutes saved",Toast.LENGTH_SHORT).show();}).show();
    }

    private void showFood(){
        content.removeAllViews();title("NUTRITION FOR A LEANER PHYSIQUE");
        LinearLayout c=card();c.addView(text("SET YOUR OWN DAILY TARGETS",13,LIME,true));EditText protein=field("Protein target (grams)",InputType.TYPE_CLASS_NUMBER);protein.setText(prefs.getString("protein_goal",""));EditText calories=field("Calorie target (optional)",InputType.TYPE_CLASS_NUMBER);calories.setText(prefs.getString("calorie_goal",""));c.addView(protein);c.addView(calories);c.addView(btn("SAVE TARGETS",()->{prefs.edit().putString("protein_goal",protein.getText().toString()).putString("calorie_goal",calories.getText().toString()).apply();Toast.makeText(this,"Targets saved",Toast.LENGTH_SHORT).show();}));add(c);
        LinearLayout intake=card();intake.addView(text("TODAY'S FOOD LOG",14,LIME,true));EditText eaten=field("Protein eaten today (grams)",InputType.TYPE_CLASS_NUMBER);eaten.setText(prefs.getString("protein_today",""));intake.addView(eaten);EditText meals=field("Meals / notes (e.g. eggs, dal, chicken)",InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);meals.setText(prefs.getString("food_notes",""));intake.addView(meals);
        intake.addView(btn("SAVE TODAY'S FOOD",()->{prefs.edit().putString("protein_today",eaten.getText().toString()).putString("food_notes",meals.getText().toString()).apply();Toast.makeText(this,"Food log saved",Toast.LENGTH_SHORT).show();}));add(intake);
        LinearLayout guide=card();guide.addView(text("SIMPLE MEAL STRUCTURE",14,LIME,true));guide.addView(text("Include a protein source, vegetables or fruit, a carbohydrate source and fluids across meals. Options: eggs, milk, curd, paneer, dal, rajma, chicken, fish, soy, rice, oats and fruit.",13,WHITE,false));add(guide);
        LinearLayout caution=card();caution.addView(text("NO CRASH DIET",13,ORANGE,true));caution.addView(text("Avoid starvation diets, dehydration tricks or extreme cuts. A sustainable calorie deficit is not appropriate for everyone; set calorie/protein targets with a qualified dietitian if you have a medical condition or symptoms.",12,MUTED,false));add(caution);
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
