package jonomoneta.juno.moviecash.Adapter;

import android.content.Context;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import java.util.ArrayList;

import jonomoneta.juno.moviecash.CustomEditText;
import jonomoneta.juno.moviecash.CustomRadioButton;
import jonomoneta.juno.moviecash.Model.Response.QuizResponse;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Utils.Utility;

import static jonomoneta.juno.moviecash.Activity.PredictionActivity.ansid;
import static jonomoneta.juno.moviecash.Activity.PredictionActivity.anstxt;
import static jonomoneta.juno.moviecash.Activity.PredictionActivity.isRight;

public class PredictionAdapter extends PagerAdapter {

    //    private ArrayList<Prediction.ResponseData> predictionArrayList;
    private static ArrayList<QuizResponse.ResponseData.QuestionList> quizList;
    private LayoutInflater inflater;
    private Context context;
    private static CustomRadioButton ansOneRB;
    private static CustomRadioButton ansTwoRB;
    private static CustomRadioButton ansThreeRB;
    private static CustomRadioButton ansFourRB;
    private static CustomRadioButton ansFiveRB;
    static CustomEditText answerET;
    LinearLayout mainLL;


//    public PredictionAdapter(Context context, ArrayList<Prediction.ResponseData> predictionArrayList) {
//        this.context = context;
//        this.predictionArrayList = predictionArrayList;
//        inflater = LayoutInflater.from(context);
//    }

    public PredictionAdapter(Context context, ArrayList<QuizResponse.ResponseData.QuestionList> quizList, LinearLayout mainLL) {
        this.context = context;
        this.quizList = quizList;
        this.mainLL = mainLL;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return quizList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View view1 = inflater.inflate(R.layout.prediction_item, view, false);

        assert view1 != null;
        RadioGroup radGrp = view1.findViewById(R.id.radGrp);
        ansOneRB = view1.findViewById(R.id.ansOneRB);
        ansTwoRB = view1.findViewById(R.id.ansTwoRB);
        ansThreeRB = view1.findViewById(R.id.ansThreeRB);
        ansFourRB = view1.findViewById(R.id.ansFourRB);
        ansFiveRB = view1.findViewById(R.id.ansFiveRB);
        answerET = view1.findViewById(R.id.answerET);


        setCheckBox(position);

        answerET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    anstxt = charSequence.toString();
                } else {
                    anstxt = "";
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        radGrp.setVisibility(View.VISIBLE);
        answerET.setVisibility(View.GONE);
        setOptions(quizList.get(position).getAnswersList().size(), position);

        ansOneRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.disableAllView(mainLL);
                ansid = quizList.get(position).getAnswersList().get(0).getID();
                isRight = quizList.get(position).getAnswersList().get(0).isRight();

            }
        });

        ansTwoRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utility.disableAllView(mainLL);
                ansid = quizList.get(position).getAnswersList().get(1).getID();
                isRight = quizList.get(position).getAnswersList().get(1).isRight();
            }
        });

        ansThreeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.disableAllView(mainLL);
                ansid = quizList.get(position).getAnswersList().get(2).getID();
                isRight = quizList.get(position).getAnswersList().get(2).isRight();

            }
        });

        ansFourRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.disableAllView(mainLL);
                ansid = quizList.get(position).getAnswersList().get(3).getID();
                isRight = quizList.get(position).getAnswersList().get(3).isRight();

            }
        });

        ansFiveRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.disableAllView(mainLL);
                ansid = quizList.get(position).getAnswersList().get(4).getID();
                isRight = quizList.get(position).getAnswersList().get(4).isRight();

            }
        });


        view.addView(view1, 0);

        return view1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


    private void setOptions(int size, int position) {

        switch (size) {
            case 1:
                ansOneRB.setVisibility(View.VISIBLE);
                ansTwoRB.setVisibility(View.GONE);
                ansThreeRB.setVisibility(View.GONE);
                ansFourRB.setVisibility(View.GONE);
                ansFiveRB.setVisibility(View.GONE);

                ansOneRB.setText(quizList.get(position).getAnswersList().get(0).getAnswer());

                break;
            case 2:
                ansOneRB.setVisibility(View.VISIBLE);
                ansTwoRB.setVisibility(View.VISIBLE);
                ansThreeRB.setVisibility(View.GONE);
                ansFourRB.setVisibility(View.GONE);
                ansFiveRB.setVisibility(View.GONE);

                ansOneRB.setText(quizList.get(position).getAnswersList().get(0).getAnswer());
                ansTwoRB.setText(quizList.get(position).getAnswersList().get(1).getAnswer());

                break;
            case 3:
                ansOneRB.setVisibility(View.VISIBLE);
                ansTwoRB.setVisibility(View.VISIBLE);
                ansThreeRB.setVisibility(View.VISIBLE);
                ansFourRB.setVisibility(View.GONE);
                ansFiveRB.setVisibility(View.GONE);

                ansOneRB.setText(quizList.get(position).getAnswersList().get(0).getAnswer());
                ansTwoRB.setText(quizList.get(position).getAnswersList().get(1).getAnswer());
                ansThreeRB.setText(quizList.get(position).getAnswersList().get(2).getAnswer());

                break;
            case 4:
                ansOneRB.setVisibility(View.VISIBLE);
                ansTwoRB.setVisibility(View.VISIBLE);
                ansThreeRB.setVisibility(View.VISIBLE);
                ansFourRB.setVisibility(View.VISIBLE);
                ansFiveRB.setVisibility(View.GONE);

                ansOneRB.setText(quizList.get(position).getAnswersList().get(0).getAnswer());
                ansTwoRB.setText(quizList.get(position).getAnswersList().get(1).getAnswer());
                ansThreeRB.setText(quizList.get(position).getAnswersList().get(2).getAnswer());
                ansFourRB.setText(quizList.get(position).getAnswersList().get(3).getAnswer());

                break;
            case 5:
                ansOneRB.setVisibility(View.VISIBLE);
                ansTwoRB.setVisibility(View.VISIBLE);
                ansThreeRB.setVisibility(View.VISIBLE);
                ansFourRB.setVisibility(View.VISIBLE);
                ansFiveRB.setVisibility(View.VISIBLE);

                ansOneRB.setText(quizList.get(position).getAnswersList().get(0).getAnswer());
                ansTwoRB.setText(quizList.get(position).getAnswersList().get(1).getAnswer());
                ansThreeRB.setText(quizList.get(position).getAnswersList().get(2).getAnswer());
                ansFourRB.setText(quizList.get(position).getAnswersList().get(3).getAnswer());
                ansFiveRB.setText(quizList.get(position).getAnswersList().get(4).getAnswer());
                break;
        }

    }


    public static void setCheckBox(int pos) {
        ansOneRB.setChecked(false);
        ansTwoRB.setChecked(false);
        ansThreeRB.setChecked(false);
        ansFourRB.setChecked(false);
        ansFiveRB.setChecked(false);

        for (int i = 0; i < quizList.get(pos).getAnswersList().size(); i++) {
            boolean right = quizList.get(pos).getAnswersList().get(i).isRight();
            if (right) {
                switch (i) {
                    case 0:
                        ansOneRB.setBackgroundResource(R.drawable.prediction_selector_correct);
                        ansTwoRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        ansThreeRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        ansFourRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        ansFiveRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        break;
                    case 1:
                        ansOneRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        ansTwoRB.setBackgroundResource(R.drawable.prediction_selector_correct);
                        ansThreeRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        ansFourRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        ansFiveRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        break;
                    case 2:
                        ansOneRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        ansTwoRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        ansThreeRB.setBackgroundResource(R.drawable.prediction_selector_correct);
                        ansFourRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        ansFiveRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        break;
                    case 3:
                        ansOneRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        ansTwoRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        ansThreeRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        ansFourRB.setBackgroundResource(R.drawable.prediction_selector_correct);
                        ansFiveRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        break;
                    case 4:
                        ansOneRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        ansTwoRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        ansThreeRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        ansFourRB.setBackgroundResource(R.drawable.prediction_selector_wrong);
                        ansFiveRB.setBackgroundResource(R.drawable.prediction_selector_correct);
                        break;
                }
                break;
            }

        }


    }

}
