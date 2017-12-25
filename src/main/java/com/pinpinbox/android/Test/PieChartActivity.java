//package com.pinpinbox.android.Test;
//
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.text.SpannableString;
//import android.text.style.ForegroundColorSpan;
//import android.text.style.RelativeSizeSpan;
//import android.text.style.StyleSpan;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//
//import com.pinpinbox.android.R;
//import com.pinpinbox.android.StringClass.ColorClass;
//
//import java.util.ArrayList;
//
///**
// * Created by vmage on 2017/10/23.
// */
//
//public class PieChartActivity extends FragmentActivity implements SeekBar.OnSeekBarChangeListener,
//        OnChartValueSelectedListener {
//
//
//    protected String[] mParties = new String[] {
//            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
//            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
//            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
//            "Party Y", "Party Z"
//    };
//
//
//
//
//
//
//    private PieChart mChart;
//    private SeekBar mSeekBarX, mSeekBarY;
//    private TextView tvX, tvY;
//    private Button btSet;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.test_pie_chart);
//
//        btSet = (Button)findViewById(R.id.btSet);
//
//        tvX = (TextView) findViewById(R.id.tvXMax);
//        tvY = (TextView) findViewById(R.id.tvYMax);
//
//        mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
//        mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);
//        mSeekBarX.setProgress(4);
//        mSeekBarY.setProgress(10);
//
//        mChart = (PieChart) findViewById(R.id.chart1);
//
//
//
//        mChart.setUsePercentValues(true);
//
//        mChart.getDescription().setEnabled(false);
//        mChart.setExtraOffsets(5, 10, 5, 5);
//
//        mChart.setDragDecelerationFrictionCoef(0.95f);
//
//        /*中央文字*/
////        mChart.setCenterTextTypeface(mTfLight);
////        mChart.setCenterText(generateCenterSpannableText());
//
//
//
//        /*中間白色區塊*/
//        mChart.setDrawHoleEnabled(true);
//        mChart.setHoleColor(Color.WHITE);
//
//        mChart.setTransparentCircleColor(Color.WHITE);
//        mChart.setTransparentCircleAlpha(110);
//
//        mChart.setHoleRadius(48f);//58
//        mChart.setTransparentCircleRadius(24f);//61
//        mChart.setDrawCenterText(true);
//
//
//
//
//
//        mChart.setRotationAngle(0);
//        // enable rotation of the chart by touch
//        mChart.setRotationEnabled(true);
//        mChart.setHighlightPerTapEnabled(true);
//
//        // mChart.setUnit(" €");
//        // mChart.setDrawUnitsInChart(true);
//
//        // add a selection listener
//        mChart.setOnChartValueSelectedListener(this);
//
//        setData(4, 100);
//
//        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
//        // mChart.spin(2000, 0, 360);
//
//        mSeekBarX.setOnSeekBarChangeListener(this);
//        mSeekBarY.setOnSeekBarChangeListener(this);
//
//        mChart.getLegend().setEnabled(false);
//
////        Legend l = mChart.getLegend();
////        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
////        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
////        l.setOrientation(Legend.LegendOrientation.VERTICAL);
////        l.setDrawInside(false);
////        l.setXEntrySpace(7f);
////        l.setYEntrySpace(0f);
////        l.setYOffset(0f);
//
//        // entry label styling
////              mChart.setEntryLabelTypeface(mTfRegular);
//        mChart.setEntryLabelColor(Color.BLACK);
//        mChart.setEntryLabelTextSize(12f); //default 12f 不包括數字部分
//
//
//        setCustom();
//
//
//
//
//    }
//
//    private void setCustom() {
//
//        btSet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                mChart.setUsePercentValues(false);
//
//
//
//
//                setCustomData();
//
//                mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad); //餅圖動畫
//
//
//
//            }
//        });
//
//
//    }
//
//
//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//        tvX.setText("" + (mSeekBarX.getProgress()));
//        tvY.setText("" + (mSeekBarY.getProgress()));
//
//        setData(mSeekBarX.getProgress(), mSeekBarY.getProgress());
//    }
//
//
//    private void setCustomData(){
//
//        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
//
//
//        entries.add(new PieEntry(2000, "已領"));
//        entries.add(new PieEntry(8000, "可領取"));
//        entries.add(new PieEntry(400, "未結算"));
//
//
//
//        /*數值*/
//        PieDataSet dataSet = new PieDataSet(entries, "積分統計"); //第二個參數為右上角字符串
//
//
//
//        dataSet.setDrawIcons(false);
//        dataSet.setSliceSpace(0f); //餅圖區塊間隔 數值越大分越開 default => 3f
//        dataSet.setIconsOffset(new MPPointF(0, 40));
//        dataSet.setSelectionShift(20f); //餅圖大小 數值越大圖越小 點選放大 default => 5f
//
//        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//
//
//
//        /*設定顏色*/
//        ArrayList<Integer> colors = new ArrayList<>();
//        colors.add(Color.parseColor(ColorClass.MAIN_FIRST));
//        colors.add(Color.parseColor(ColorClass.PINK_FRIST));
//        colors.add(Color.parseColor(ColorClass.GREY_FIRST));
//
//        dataSet.setColors(colors);
//
//
//        PieData data = new PieData(dataSet);
//        data.setValueFormatter(new DefaultValueFormatter(0)); // 0 => 小數點位數
//        data.setValueTextSize(18f); //default => 11f
//        data.setValueTextColor(Color.BLACK);
//
//
//
////        data.setValueTypeface(mTfLight);
//        mChart.setData(data);
//
//        // undo all highlights
//        mChart.highlightValue(null);
//
//        mChart.invalidate();
//
//
//
//
//    }
//
//
//    private void setData(int count, float range) {
//
//        float mult = range;
//
//        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
//
//        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
//        // the chart.
//        for (int i = 0; i < count ; i++) {
//            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5),
//                    mParties[i % mParties.length],
//                    getResources().getDrawable(R.drawable.pinpin_192)));
//        }
//
//        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
//
//        dataSet.setDrawIcons(false);
//
//        dataSet.setSliceSpace(3f);
//        dataSet.setIconsOffset(new MPPointF(0, 40));
//        dataSet.setSelectionShift(5f);
//
//        // add a lot of colors
//
//        ArrayList<Integer> colors = new ArrayList<Integer>();
//
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
//
//        colors.add(ColorTemplate.getHoloBlue());
//
//        dataSet.setColors(colors);
//        //dataSet.setSelectionShift(0f);
//
//        PieData data = new PieData(dataSet);
//        data.setValueFormatter(new PercentFormatter());
//        data.setValueTextSize(11f);
//        data.setValueTextColor(Color.WHITE);
////        data.setValueTypeface(mTfLight);
//        mChart.setData(data);
//
//        // undo all highlights
//        mChart.highlightValues(null);
//
//        mChart.invalidate();
//    }
//
//    private SpannableString generateCenterSpannableText() {
//
//        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
//        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
//        return s;
//    }
//
//    @Override
//    public void onValueSelected(Entry e, Highlight h) {
//
//        if (e == null)
//            return;
//        Log.i("VAL SELECTED",
//                "Value: " + e.getY() + ", index: " + h.getX()
//                        + ", DataSet index: " + h.getDataSetIndex());
//    }
//
//    @Override
//    public void onNothingSelected() {
//        Log.i("PieChart", "nothing selected");
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//        // TODO Auto-generated method stub
//
//    }
//}
