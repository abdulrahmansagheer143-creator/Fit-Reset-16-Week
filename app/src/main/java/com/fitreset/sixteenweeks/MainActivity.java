package com.fitreset.sixteenweeks;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    private final int BG=Color.rgb(15,23,19), PANEL=Color.rgb(27,39,31), LIME=Color.rgb(199,242,108), MUTED=Color.rgb(171,187,174);
    private SharedPreferences prefs;
    private LinearLayout content;
    private int week=1;
    private final String[] days={"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY"};
    private final String[][] plan={
      {"Mobility + easy walk","Rest / gentle walk","Supported strength basics","Rest / mobility","Easy walk + core control","Optional light practice","Rest"},
      {"Full-body basics","Walk 20–30 min","Full-body basics","Rest / mobility","Walk 20–30 min","Light technique","Rest"},
      {"Strength technique","Walk 25–30 min","Strength technique","Rest","Walk 25–30 min","Mobility + easy cardio","Rest"},
      {"Full-body strength","Easy walk","Full-body strength","Rest","Walk + mobility","Optional light session","Rest"},
      {"Upper-body technique","Walk 30 min","Supported lower-body technique","Rest","Upper-body technique","Easy cardio","Rest"},
      {"Strength session A","Walk 30 min","Strength session B","Rest","Strength session A","Easy cardio + mobility","Rest"}
    };
    public void onCreate(Bundle b) {
      super.onCreate(b);
      prefs=getSharedPreferences("fitreset",0);
      getWindow().setStatusBarColor(BG); getWindow().setNavigationBarColor(BG);
      LinearLayout root=new LinearLayout(this); root.setOrientation(1); root.setBackgroundColor(BG);
      LinearLayout header=new LinearLayout(this); header.setOrientation(1); header.setPadding(22,22,22,18); header.setBackgroundColor(Color.rgb(34,54,39));
      header.addView(txt("YOUR 16-WEEK RESET",12,LIME,true)); header.addView(txt("FIT RESET.",30,Color.WHITE,true)); header.addView(txt("Build consistency. Train with control.",14,MUTED,false));
      root.addView(header);
      ScrollView scroll=new ScrollView(this); content=new LinearLayout(this); content.setOrientation(1); content.setPadding(16,16,16,20); scroll.addView(content);
      root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
      LinearLayout nav=new LinearLayout(this); nav.setGravity(Gravity.CENTER); nav.setBackgroundColor(Color.rgb(22,33,26));
      String[] tabs={"HOME","PLAN","TRACK","FOOD"};
      for(final String tab:tabs){ Button x=new Button(this); x.setText(tab); x.setTextSize(11); x.setTextColor(LIME); x.setBackgroundColor(Color.TRANSPARENT); x.setOnClickListener(v->{if(tab.equals("HOME"))home();else if(tab.equals("PLAN"))showPlan();else if(tab.equals("TRACK"))track();else food();}); nav.addView(x,new LinearLayout.LayoutParams(0,-2,1)); }
      root.addView(nav); setContentView(root); home();
    }
    private TextView txt(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);if(bold)t.setTypeface(null,Typeface.BOLD);return t;}
    private LinearLayout card(){LinearLayout c=new LinearLayout(this);c.setOrientation(1);c.setPadding(16,14,16,14);c.setBackgroundColor(PANEL);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.bottomMargin=12;c.setLayoutParams(p);return c;}
    private void add(View v){content.addView(v);}
    private void heading(String s){TextView t=txt(s,20,Color.WHITE,true);t.setPadding(0,8,0,12);add(t);}
    private Button button(String s,Runnable r){Button b=new Button(this);b.setText(s);b.setTextColor(Color.rgb(20,30,20));b.setBackgroundColor(LIME);b.setOnClickListener(v->r.run());return b;}
    private int count(){int n=0;for(int w=1;w<=16;w++)for(int d=0;d<7;d++)if(prefs.getBoolean("d"+w+"_"+d,false))n++;return n;}
    private void home(){content.removeAllViews();LinearLayout c=card();c.addView(txt("CURRENT PROGRAM",12,LIME,true));c.addView(txt("WEEK "+week+" / 16",28,Color.WHITE,true));c.addView(txt(count()+" sessions completed",15,MUTED,false));c.addView(button("OPEN 16-WEEK PLAN",()->showPlan()));add(c);LinearLayout s=card();s.addView(txt("YOUR PROGRESS",13,LIME,true));s.addView(txt(count()+" / 112 sessions",24,Color.WHITE,true));s.addView(txt("Move well, then build up.",18,Color.WHITE,true));s.addView(txt("Warm up gently. Keep movements controlled. Stop if you feel pain, dizziness or unusual breathlessness.",14,MUTED,false));add(s);LinearLayout safe=card();safe.addView(txt("TRAIN SMART",17,LIME,true));safe.addView(txt("General fitness planner, not medical advice. Seek appropriate medical or physiotherapy clearance before resistance training if you have a medical condition.",13,MUTED,false));add(safe);}
    private void showPlan(){content.removeAllViews();heading("16-WEEK TRAINING PLAN");LinearLayout pick=card();LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER_VERTICAL);row.addView(button("‹",()->{week=Math.max(1,week-1);showPlan();}));row.addView(txt("   WEEK "+week+" OF 16   ",18,Color.WHITE,true),new LinearLayout.LayoutParams(0,-2,1));row.addView(button("›",()->{week=Math.min(16,week+1);showPlan();}));pick.addView(row);add(pick);String[] tasks=plan[Math.min(week-1,5)];for(int i=0;i<7;i++){final int d=i;LinearLayout c=card();CheckBox cb=new CheckBox(this);cb.setText(days[i]+"\n"+tasks[i]);cb.setTextColor(Color.WHITE);cb.setTextSize(14);cb.setButtonTintList(android.content.res.ColorStateList.valueOf(LIME));cb.setChecked(prefs.getBoolean("d"+week+"_"+i,false));cb.setOnCheckedChangeListener((v,on)->prefs.edit().putBoolean("d"+week+"_"+d,on).apply());c.addView(cb);add(c);}LinearLayout note=card();note.addView(txt("Weeks 5–16 use conservative repeatable templates. Increase difficulty only when comfortable and safe.",12,MUTED,false));add(note);}
    private void track(){content.removeAllViews();heading("PROGRESS TRACKER");LinearLayout c=card();c.addView(txt("WORKOUT COMPLETION",13,LIME,true));c.addView(txt(count()+" / 112 sessions",25,Color.WHITE,true));EditText w=new EditText(this);w.setSingleLine(true);w.setHint("Body weight (kg)");w.setInputType(8194);w.setTextColor(Color.WHITE);String old=prefs.getString("weight","");if(!old.isEmpty())w.setText(old);c.addView(w);c.addView(button("SAVE WEIGHT",()->{prefs.edit().putString("weight",w.getText().toString()).apply();Toast.makeText(this,"Saved on this phone",0).show();}));add(c);}
    private void food(){content.removeAllViews();heading("NUTRITION TRACKER");LinearLayout c=card();c.addView(txt("DAILY PROTEIN CHECK",13,LIME,true));EditText goal=new EditText(this);goal.setHint("Protein goal (grams)");goal.setInputType(2);goal.setTextColor(Color.WHITE);EditText eaten=new EditText(this);eaten.setHint("Protein eaten today (grams)");eaten.setInputType(2);eaten.setTextColor(Color.WHITE);c.addView(goal);c.addView(eaten);TextView result=txt("",14,Color.WHITE,true);c.addView(button("CALCULATE",()->{try{int g=Integer.parseInt(goal.getText().toString()),e=Integer.parseInt(eaten.getText().toString());result.setText(e+" / "+g+" g logged · "+Math.max(0,g-e)+" g remaining");}catch(Exception ex){result.setText("Enter valid numbers.");}}));c.addView(result);add(c);LinearLayout f=card();f.addView(txt("Food ideas: eggs, milk, curd, paneer, dal, rajma, chicken, fish, soy, fruit and vegetables.",14,MUTED,false));add(f);}
}
