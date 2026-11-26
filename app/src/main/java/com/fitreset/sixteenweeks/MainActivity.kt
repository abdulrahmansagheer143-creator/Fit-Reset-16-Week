package com.fitreset.sixteenweeks

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.*
import android.content.Context

class MainActivity : Activity() {
    private val bg = Color.rgb(15, 23, 19)
    private val panel = Color.rgb(27, 39, 31)
    private val lime = Color.rgb(199, 242, 108)
    private val muted = Color.rgb(171, 187, 174)
    private val prefs by lazy { getSharedPreferences("fit_reset", Context.MODE_PRIVATE) }
    private var week = 1
    private lateinit var content: LinearLayout
    private lateinit var root: LinearLayout

    private val days = arrayOf("MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY")
    private val plans = arrayOf(
        arrayOf("Mobility + easy walk","Rest / gentle walk","Supported strength basics","Rest / mobility","Easy walk + core control","Optional light practice","Rest"),
        arrayOf("Full-body basics","Walk 20–30 min","Full-body basics","Rest / mobility","Walk 20–30 min","Light technique","Rest"),
        arrayOf("Strength technique","Walk 25–30 min","Strength technique","Rest","Walk 25–30 min","Mobility + easy cardio","Rest"),
        arrayOf("Full-body strength","Easy walk","Full-body strength","Rest","Walk + mobility","Optional light session","Rest"),
        arrayOf("Upper-body technique","Walk 30 min","Supported lower-body technique*","Rest","Upper-body technique","Easy cardio","Rest"),
        arrayOf("Strength session A","Walk 30 min","Strength session B*","Rest","Strength session A","Easy cardio + mobility","Rest")
    )
    private val phases = arrayOf("FOUNDATION","FOUNDATION","FOUNDATION","FOUNDATION","BUILD","BUILD","BUILD","RECOVERY","DEVELOP","DEVELOP","DEVELOP","RECOVERY","PERFORM","PERFORM","PERFORM","REVIEW")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        buildShell()
        showHome()
    }

    private fun buildShell() {
        root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setBackgroundColor(bg) }
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(22, 24, 22, 18)
            setBackgroundColor(Color.rgb(34, 54, 39))
        }
        header.addView(label("YOUR 16-WEEK RESET", 12, lime, true))
        header.addView(label("FIT RESET.", 30, Color.WHITE, true))
        header.addView(label("Build consistency. Train with control.", 14, muted, false))
        root.addView(header)
        val scroll = ScrollView(this)
        content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(16,16,16,24) }
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1,0,1f))
        val nav = LinearLayout(this).apply { setPadding(4,8,4,8); setBackgroundColor(Color.rgb(22,33,26)); gravity=Gravity.CENTER }
        listOf("HOME","PLAN","TRACK","FOOD").forEach { name ->
            val b = Button(this).apply {
                text = name; textSize = 11f; setTextColor(lime); setBackgroundColor(Color.TRANSPARENT)
                setOnClickListener { when(name) { "HOME"->showHome(); "PLAN"->showPlan(); "TRACK"->showTrack(); else->showFood() } }
            }
            nav.addView(b, LinearLayout.LayoutParams(0, -2, 1f))
        }
        root.addView(nav)
        setContentView(root)
    }

    private fun label(s:String, size:Int, color:Int, bold:Boolean): TextView =
        TextView(this).apply { text=s; textSize=size.toFloat(); setTextColor(color); if(bold) setTypeface(null,Typeface.BOLD) }

    private fun card(): LinearLayout = LinearLayout(this).apply {
        orientation=LinearLayout.VERTICAL; setPadding(16,14,16,14); setBackgroundColor(panel)
        val p=LinearLayout.LayoutParams(-1,-2); p.bottomMargin=12; layoutParams=p
    }

    private fun addCard(v: View) { content.addView(v) }
    private fun heading(s:String) { val t=label(s,20,Color.WHITE,true); t.setPadding(2,8,2,12); addCard(t) }
    private fun action(s:String, onClick:()->Unit) = Button(this).apply {
        text=s; textSize=14f; setTextColor(Color.rgb(20,30,20)); setBackgroundColor(lime); setOnClickListener { onClick() }
    }
    private fun checkedCount():Int = (1..16).sumOf { w -> (0..6).count { d -> prefs.getBoolean("done_${w}_${d}",false) } }

    private fun showHome() {
        content.removeAllViews()
        val c=card()
        c.addView(label("CURRENT PROGRAM",11,lime,true))
        c.addView(label("WEEK $week / 16",28,Color.WHITE,true))
        val n=checkedCount()
        c.addView(label("$n sessions checked off",14,muted,false))
        c.addView(action("OPEN 16-WEEK PLAN") { showPlan() })
        addCard(c)
        val stats=card()
        stats.addView(label("YOUR PROGRESS",12,lime,true))
        stats.addView(label("$n / 112 workout days",24,Color.WHITE,true))
        stats.addView(label("Today's reminder",18,Color.WHITE,true))
        stats.addView(label("Warm up gently. Keep movements controlled. Stop if you feel pain, dizziness or unusual breathlessness.",14,muted,false))
        addCard(stats)
        val safe=card()
        safe.addView(label("TRAIN SMART",17,lime,true))
        safe.addView(label("This is a general fitness planner, not medical advice. Get appropriate medical/physio clearance before resistance training if you have a medical condition. Avoid any movement that causes pain, numbness, weakness or unusual breathing difficulty.",13,muted,false))
        addCard(safe)
    }

    private fun showPlan() {
        content.removeAllViews()
        heading("16-WEEK TRAINING PLAN")
        val picker=card()
        val row=LinearLayout(this).apply { orientation=LinearLayout.HORIZONTAL; gravity=Gravity.CENTER_VERTICAL }
        row.addView(action("‹") { week=(week-1).coerceAtLeast(1); showPlan() })
        row.addView(label("   WEEK $week OF 16   ",18,Color.WHITE,true),LinearLayout.LayoutParams(0,-2,1f))
        row.addView(action("›") { week=(week+1).coerceAtMost(16); showPlan() })
        picker.addView(row)
        picker.addView(label(phases[week-1],12,lime,true))
        addCard(picker)
        val items=plans[(week-1).coerceAtMost(5)]
        days.indices.forEach { i ->
            val c=card()
            val line=LinearLayout(this).apply { orientation=LinearLayout.HORIZONTAL; gravity=Gravity.CENTER_VERTICAL }
            val cb=CheckBox(this).apply {
                text=days[i]+"
"+items[i]; textSize=14f; setTextColor(Color.WHITE); buttonTintList=android.content.res.ColorStateList.valueOf(lime)
                isChecked=prefs.getBoolean("done_${week}_${i}",false)
                setOnCheckedChangeListener { _,v -> prefs.edit().putBoolean("done_${week}_${i}",v).apply() }
            }
            line.addView(cb,LinearLayout.LayoutParams(-1,-2))
            c.addView(line); addCard(c)
        }
        val note=card()
        note.addView(label("*Choose only comfortable, supported variations. Progress gradually; weeks 5–16 repeat a conservative template rather than prescribing heavier loads.",12,muted,false))
        addCard(note)
    }

    private fun showTrack() {
        content.removeAllViews(); heading("PROGRESS TRACKER")
        val c=card(); c.addView(label("WORKOUT COMPLETION",13,lime,true))
        c.addView(label("${checkedCount()} / 112 sessions",25,Color.WHITE,true))
        val weight=EditText(this).apply { hint="Body weight (kg)"; inputType=android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL; setTextColor(Color.WHITE); setHintTextColor(muted) }
        c.addView(weight)
        val saved=prefs.getString("weight","")
        if(!saved.isNullOrEmpty()) weight.setText(saved)
        c.addView(action("SAVE WEIGHT") { prefs.edit().putString("weight",weight.text.toString()).apply(); Toast.makeText(this,"Weight saved on this device",Toast.LENGTH_SHORT).show() })
        addCard(c)
        val note=card(); note.addView(label("Your logs stay on this phone. Record weight under similar conditions for more useful comparisons.",13,muted,false)); addCard(note)
    }

    private fun showFood() {
        content.removeAllViews(); heading("NUTRITION")
        val c=card()
        c.addView(label("DAILY PROTEIN CHECK",13,lime,true))
        val goal=EditText(this).apply { hint="Protein goal (grams)"; inputType=2; setTextColor(Color.WHITE); setHintTextColor(muted) }
        val eaten=EditText(this).apply { hint="Protein eaten today (grams)"; inputType=2; setTextColor(Color.WHITE); setHintTextColor(muted) }
        c.addView(goal); c.addView(eaten)
        val result=label("",15,Color.WHITE,true)
        c.addView(action("CALCULATE") {
            val g=goal.text.toString().toIntOrNull() ?: 0
            val e=eaten.text.toString().toIntOrNull() ?: 0
            result.text=if(g>0) "$e / $g g logged · ${(g-e).coerceAtLeast(0)} g remaining" else "Enter a valid goal."
        })
        c.addView(result); addCard(c)
        val foods=card()
        foods.addView(label("SIMPLE FOOD IDEAS",17,lime,true))
        foods.addView(label("Eggs · milk · curd · paneer · dal · rajma · chicken · fish · soy · fruit · vegetables · whole grains",14,muted,false))
        addCard(foods)
        val disclaimer=card()
        disclaimer.addView(label("Protein needs vary with body size, health and activity. Use a suitable target rather than forcing a high intake; seek qualified advice if you have a medical condition.",12,muted,false))
        addCard(disclaimer)
    }
}
