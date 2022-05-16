import com.atguigu.util.SMSUtils;
import com.atguigu.util.ValidateCodeUtils;

public class TestSms {
    public static void main(String[] args) throws Exception {
        SMSUtils.sendShortMessage("15238536252", ValidateCodeUtils.generateValidateCode(6).toString(),"5");
    }
}
