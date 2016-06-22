package Util;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

//TextInputLayout监听类
public class MyTextWatch implements TextWatcher {

    private int flag;
    private TextInputLayout textInputLayout;
    private static boolean textValid = false;

    public MyTextWatch(int flag, TextInputLayout textInputLayout){
        this.flag = flag;
        this.textInputLayout = textInputLayout;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        isValid(flag, textInputLayout);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        isValid(flag, textInputLayout);
    }

    //判断输入的学号、密码、验证码是否符合格式要求
    public static boolean isValid(int flag, TextInputLayout textInputLayout){
        switch (flag){
            case 1:
                if (textInputLayout.getEditText().getText().toString().equals("")
                        || textInputLayout.getEditText().getText().toString().length() < 11){
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("请输入正确的学号");
                    textValid = false;
                }else {
                    textInputLayout.setErrorEnabled(false);
                    textValid = true;
                }
                break;

            case 2:
                if (textInputLayout.getEditText().getText().toString().equals("")
                        || textInputLayout.getEditText().getText().toString().length() < 5){
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("请输入正确的密码");
                    textValid = false;
                }else {
                    textInputLayout.setErrorEnabled(false);
                    textValid = true;
                }
                break;

            case 3:
                if (textInputLayout.getEditText().getText().toString().equals("")
                        || textInputLayout.getEditText().getText().toString().length() < 4){
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("请输入正确的验证码");
                    textValid = false;
                }else {
                    textInputLayout.setErrorEnabled(false);
                    textValid = true;
                }
                break;
        }
        return textValid;
    }


    //返回TextInputLayout的EditText中的内容
    public static String getTextInputLayoutString(TextInputLayout textInputLayout){
        return textInputLayout.getEditText().getText().toString();
    }
}
