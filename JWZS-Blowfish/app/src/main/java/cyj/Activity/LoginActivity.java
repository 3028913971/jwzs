package cyj.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import Util.HttpUtil;
import Util.LogUtil;
import Util.MyTextWatch;
import Util.Utility;

//登陆页面
public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener {

    private Button login_btn;
    private TextInputLayout usernameLayout, passwordLayout, checkcodeLayout;
    private ImageButton img, seePassword;
    private Dialog dialog;
    private String TAG = "HttpUtil";
    private boolean hasError = false, isSee = false;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Utility.CONNECTION_ERROR:
                    Toast.makeText(LoginActivity.this, "当前网络状况不好", Toast.LENGTH_SHORT).show();
                    break;

                case Utility.LOAD_SUCCESS:
                    //获取储存在SD卡中的checkcode.png文件
                    File file = new File(LoginActivity.this.getFilesDir(),"checkcode.png");
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(file);
                        Bitmap bitmap = BitmapFactory.decodeStream(fis);
                        img.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;

                case Utility.LOAD_ERROR:
                    Toast.makeText(LoginActivity.this, "验证码下载出错", Toast.LENGTH_SHORT).show();
                    if (!hasError){
                        HttpUtil.getCheckcode(new Utility.GetCheckcodeCallBack() {
                            @Override
                            public void doSuccess() {
                                handler.sendEmptyMessage(Utility.LOAD_SUCCESS);
                            }

                            @Override
                            public void doError() {
                                handler.sendEmptyMessage(Utility.LOAD_ERROR);
                            }
                        });
                        hasError = true;
                    }else {
                        Toast.makeText(LoginActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case Utility.LOGIN_SUCCESS:
                    dialog.dismiss();
                    IndexActivity.getInstance(LoginActivity.this);
                    LoginActivity.this.finish();
                    break;

                case Utility.LOGIN_ERROR:
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "登录出错,请重新登录", Toast.LENGTH_SHORT).show();
                    passwordLayout.getEditText().setText("");
                    checkcodeLayout.getEditText().setText("");
                    HttpUtil.getCheckcode(new Utility.GetCheckcodeCallBack() {
                        @Override
                        public void doSuccess() {
                            handler.sendEmptyMessage(Utility.LOAD_SUCCESS);
                        }

                        @Override
                        public void doError() {
                            handler.sendEmptyMessage(Utility.LOAD_ERROR);
                        }
                    });
                    break;
            }
        }
    };

    //获取当前实例
    public static void getInstance(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findControl();
        HttpUtil.setContext(this);
        HttpUtil.getConnection(new Utility.GetConnectionCallBack() {

            @Override
            public void connectionError() {
                handler.sendEmptyMessage(Utility.CONNECTION_ERROR);
            }
        });

        HttpUtil.getCheckcode(new Utility.GetCheckcodeCallBack() {
            @Override
            public void doSuccess() {
                handler.sendEmptyMessage(Utility.LOAD_SUCCESS);
            }

            @Override
            public void doError() {
                handler.sendEmptyMessage(Utility.LOAD_ERROR);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                if (MyTextWatch.isValid(1, usernameLayout) && MyTextWatch.isValid(2, passwordLayout)
                        && MyTextWatch.isValid(3, checkcodeLayout)) {
                    final Map<String, String> data = new ArrayMap<>();
                    data.put("txtUserName", MyTextWatch.getTextInputLayoutString(usernameLayout));
                    data.put("TextBox2", MyTextWatch.getTextInputLayoutString(passwordLayout));
                    data.put("txtSecretCode", MyTextWatch.getTextInputLayoutString(checkcodeLayout));
                    dialog = Utility.showDialog(LoginActivity.this, "登录中...");
                    HttpUtil.login(data, new Utility.LoginCallBack() {
                        @Override
                        public void loginSuccess() {
                            handler.sendEmptyMessage(Utility.LOGIN_SUCCESS);
                        }

                        @Override
                        public void loginError() {
                            handler.sendEmptyMessage(Utility.LOGIN_ERROR);
                        }
                    });
                }
                break;

            case R.id.checkcode_image:
                HttpUtil.getCheckcode(new Utility.GetCheckcodeCallBack() {
                    @Override
                    public void doSuccess() {
                        handler.sendEmptyMessage(Utility.LOAD_SUCCESS);
                    }

                    @Override
                    public void doError() {
                        handler.sendEmptyMessage(Utility.LOAD_ERROR);
                    }
                });
                LogUtil.e(TAG, "点击验证码");
                break;

            case R.id.see_password:
                if (isSee == false){
                    LogUtil.e(TAG, "isSee= " + isSee);
                    isSee = true;
                    passwordLayout.getEditText()
                            .setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    LogUtil.e(TAG, "isSee= " + isSee);
                    isSee = false;
                    passwordLayout.getEditText()
                            .setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
        }
    }

    //find控件
    private void findControl() {
        img = (ImageButton) findViewById(R.id.checkcode_image);
        img.setOnClickListener(this);
        seePassword = (ImageButton)findViewById(R.id.see_password);
        seePassword.setOnClickListener(this);
        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
        usernameLayout = (TextInputLayout) findViewById(R.id.username_input_layout);
        passwordLayout = (TextInputLayout) findViewById(R.id.password_input_layout);
        checkcodeLayout = (TextInputLayout) findViewById(R.id.checkcode_input_layout);
        usernameLayout.getEditText().addTextChangedListener(new MyTextWatch(1, usernameLayout));
        passwordLayout.getEditText().addTextChangedListener(new MyTextWatch(2, passwordLayout));
        checkcodeLayout.getEditText().addTextChangedListener(new MyTextWatch(3, checkcodeLayout));
    }

}

