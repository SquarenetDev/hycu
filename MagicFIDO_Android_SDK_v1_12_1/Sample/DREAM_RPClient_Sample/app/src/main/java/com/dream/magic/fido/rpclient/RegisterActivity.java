package com.dream.magic.fido.rpclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dream.magic.fido.rpclient.cp.FIDORegistration;
import com.dream.magic.fido.rpsdk.callback.FIDOCallbackResult;
import com.dream.magic.fido.rpsdk.client.FIDORequestCode;
import com.dream.magic.fido.rpsdk.client.FidoResult;

/**
 * Registration Activity
 * - FIDO에 등록하기 위한 화면
 **/

public class RegisterActivity extends Activity {

    private Button btn_fido = null;    // 일반 FIDO 등록 버튼

    private EditText edit_userID = null; // 일반 FIDO에서 UserID 받기
    private EditText edit_authCode = null;

    // RP SDK 를 사용하기 위한 객체
    private FIDORegistration reg = null;

    private final int ALERT_REQUEST = 0xA3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rp_activity_register);
        this.setTitle("Registration");

        getActionBar().setDisplayShowHomeEnabled(false);

        Drawable background = getResources().getDrawable(R.drawable.bg_top);
        getActionBar().setBackgroundDrawable(background);

        // RP SDK 생성
        edit_userID = (EditText) findViewById(R.id.edit_userID);
        edit_userID.setPrivateImeOptions("defaultInputmode=english;");
        btn_fido = (Button) findViewById(R.id.btn_fido);


        // FIDO 클릭 Event
        btn_fido.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                reg = new FIDORegistration(RegisterActivity.this,  fidoResult);

                /**
                 * 일반 FIDO이기 때문에 사용자한테서 UserID를 입력 받음.
                 */
                String userID = edit_userID.getText().toString();

                if (userID.length() < 1) {
                    Toast.makeText(RegisterActivity.this, "Please, Fill out the UserID.", Toast.LENGTH_LONG).show();
                    return;
                }

                // FIDO
                reg.registFIDO(userID);
            }
        });
    }



    private FIDOCallbackResult fidoResult = new FIDOCallbackResult() {

        @Override
        public void onFIDOResult(int requestCode, boolean result, FidoResult fidoResult) {

            if (requestCode == FIDORequestCode.REQUEST_CODE_REGIST) {

                if (fidoResult.getErrorCode() == FidoResult.RESULT_SUCCESS) {
                    // 에러코드 0: 등록 성공
                    String token = reg.getToken();
                    System.out.println("Token Value : " + token);

                    isSuccess = true;
                    MainActivity.setAuthID(edit_userID.getText().toString());
                    showDialog("SUCCESS", "REG Success");


                } else {

                    // 실패 사유
                    switch (fidoResult.getErrorCode()) {
                        case FidoResult.RESULT_USER_CANCEL:
                            // 에러코드 999: 사용자 취소
                        case FidoResult.ERROR_JSON_PARSER:
                            // 에러코드 1005: 서버로부터 받은 메세지 파싱하다 에러 발생
                        case FidoResult.ERROR_CANNOT_USE_FIDO:
                            // 에러코드 1007: 지원하지 않는 단말기
                        case FidoResult.ERROR_UNTRUSTED_FACET_ID:
                            // 에러코드 2001: -등록되지 않은 3rd App으로 진행
                            //			   -FIDO Server에 FacetID 를 등록해야 함.
                        case FidoResult.ERROR_NO_SUITABLE_AUTHENTICATOR:
                            // 에러코드 2002: 적합한 인증장치가 없음
                        case FidoResult.ERROR_NETWORK_STATE:
                            // 에러코드 4001: -네트워크 오류
                            //			   -fidoResult.getDescription() 을 통하여 자세한 오류 내용을 알 수 있음
                        case FidoResult.PW_NOT_MATCH_BIO_FINGER:
                            // 에러코드 5000: 지문 인증 실패
                        case FidoResult.ERROR_UNKNOWN:
                            // 에러코드 9999: FIDO 로직 수행 중, 알수 없는 에러 발생
                            showDialog("FAIL", fidoResult.getDescription() + "(" + fidoResult.getErrorCode() + ")");
                            break;
                        case FidoResult.RESULT_USER_SELECT_REREG:
                            // 에러코드 980: 사용자가 재등록 버튼 클릭
                            Toast.makeText(RegisterActivity.this, "사용자가 재등록 버튼 클릭" , Toast.LENGTH_SHORT).show();
                            break;
                        case FidoResult.ERROR_ALREADY_REGI_USER:
                            // 에러코드 1008: -이미 등록 된 사용자
                            //             -해지 후 재등록 요청을 해야 함.
                            showDialog("FAIL", "이미 등록 된 사용자입니다. 해지 후 재등록 해주세요.(" + fidoResult.getErrorCode() + ")");
                            break;
                        default:
                            showDialog("FAIL", fidoResult.getDescription() + "(" + fidoResult.getErrorCode() + ")");
                            break;
                    }

                }
            }

        }
    };


    //=======================Activity에서 쓰이는 Alert창==============================

    private void showDialog(String title, String body) {

        Intent alertIntent = new Intent(this, AlertDlg.class);
        alertIntent.putExtra("msg", body);
        startActivityForResult(alertIntent, ALERT_REQUEST);

    }

    private boolean isSuccess = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ALERT_REQUEST && isSuccess == true) {
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0 );
    }

}