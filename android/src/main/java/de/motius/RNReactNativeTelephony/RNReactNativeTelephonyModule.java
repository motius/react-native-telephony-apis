
package de.motius.RNReactNativeTelephony;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import android.telephony.TelephonyManager;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import android.content.Context;
import android.R;

import java.util.Arrays;

public class RNReactNativeTelephonyModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private final String MODULE_NAME = "RNReactNativeTelephony";
  
  public RNReactNativeTelephonyModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    assert prefixes.length == isoCodes.length;
  }

  //http://stackoverflow.com/a/17266260/2628463
  @ReactMethod
  public void getCountryCode(Promise promise){
    String isoCountryCode="";
    String mobileCountryCode="";
    String mobileNetworkCode="";
    int callPrefix;

    TelephonyManager manager = (TelephonyManager) this.reactContext.getSystemService(Context.TELEPHONY_SERVICE);
    isoCountryCode = manager.getSimCountryIso();
    
    WritableMap map = Arguments.createMap();
    map.putString("isoCountryCode", isoCountryCode);
    
    mobileCountryCode = manager.getNetworkCountryIso();
    map.putString("mobileCountryCode", mobileCountryCode);

    mobileNetworkCode = manager.getNetworkOperator();
    map.putString("mobileNetworkCode", mobileNetworkCode);

    if(isoCountryCode.length() > 0){
      int index = Arrays.asList(isoCodes).indexOf(isoCountryCode.toUpperCase());
      callPrefix = prefixes[index];
      map.putInt("callPrefix", callPrefix);
    }else{
      map.putString("callPrefix", null);
    }
    promise.resolve(map);
  }

  @ReactMethod
  public void getPhoneNumber(Promise promise){
    Boolean callingOrSelfPermissionGranted = getCurrentActivity().checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    Boolean readSMSPermissionGrantedForApi23Up = Build.VERSION.SDK_INT >= 23 && getCurrentActivity().checkCallingOrSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    Boolean readPhoneNumberGrantedForApi26Up = Build.VERSION.SDK_INT >= 26 && getCurrentActivity().checkCallingOrSelfPermission("android.permission.READ_PHONE_NUMBERS") == PackageManager.PERMISSION_GRANTED;
    if( callingOrSelfPermissionGranted || readSMSPermissionGrantedForApi23Up || readPhoneNumberGrantedForApi26Up )
    {
      String phoneNumber = manager.getLine1Number();
      WritableMap map = Arguments.createMap();
      map.putString("phoneNumber", phoneNumber);

      promise.resolve(map);
    }
    else{
      promise.reject("no permission");
    }
  }

  @Override
  public String getName() {
    return MODULE_NAME;
  }

  private final int[] prefixes = {
    93, 355, 213, 376, 244, 672, 54, 374, 297,
    61, 43, 994, 973, 880, 375, 32, 501, 229,
    975, 591, 387, 267, 55, 673, 359, 226, 95, 257,
    855, 237, 1, 238, 236, 235, 56, 86, 61,
    61, 57, 269, 242, 243, 682, 506, 385, 53, 357, 420,
    45, 253, 670, 593, 20, 503, 240, 291, 372, 251,
    500, 298, 679, 358, 33, 689, 241, 220, 995, 49,
    233, 350, 30, 299, 502, 224, 245, 592, 509, 504, 
    852, 36, 91, 62, 98, 964, 353, 44, 972, 39, 225, 
    81, 962, 7, 254, 686, 965, 996, 856, 371, 961, 266, 
    231, 218, 423, 370, 352, 853, 389, 261, 265, 60, 960, 
    223, 356, 692, 222, 230, 262, 52, 691, 373, 377, 976, 
    382, 212, 258, 264, 674, 977, 31, 599, 687, 64, 505,
    227, 234, 683, 850, 47, 968, 92, 680, 507, 675, 595,
    51, 63, 870, 48, 351, 1, 974, 40, 7, 250, 590, 685,
    378, 239, 966, 221, 381, 248, 232, 65, 421, 386, 677,
    252, 27, 82, 34, 94, 290, 508, 249, 597, 268, 46, 41,
    963, 886, 992, 255, 66, 228, 690, 676, 216, 90, 993,
    688, 971, 256, 44, 380, 598, 1, 998, 678, 39, 58, 84,
    681, 967, 260, 263};

  private final String[] isoCodes = {
    "AF", "AL", "DZ", "AD", "AO", "AQ", "AR", "AM", "AW", "AU", "AT", "AZ", "BH", "BD", "BY", "BE", "BZ", "BJ", "BT", "BO", "BA", "BW", "BR", "BN", "BG", "BF", "MM", "BI", "KH", "CM", "CA", "CV", "CF", "TD", "CL", "CN", "CX", "CC", "CO", "KM", "CG", "CD", "CK", "CR", "HR", "CU", "CY", "CZ", "DK", "DJ", "TL", "EC", "EG", "SV", "GQ", "ER", "EE", "ET", "FK", "FO", "FJ", "FI", "FR", "PF", "GA", "GM", "GE", "DE", "GH", "GI", "GR", "GL", "GT", "GN", "GW", "GY", "HT", "HN", "HK", "HU", "IN", "ID", "IR", "IQ", "IE", "IM", "IL", "IT", "CI", "JP", "JO", "KZ", "KE", "KI", "KW", "KG", "LA", "LV", "LB", "LS", "LR", "LY", "LI", "LT", "LU", "MO", "MK", "MG", "MW", "MY", "MV", "ML", "MT", "MH", "MR", "MU", "YT", "MX", "FM", "MD", "MC", "MN", "ME", "MA", "MZ", "NA", "NR", "NP", "NL", "AN", "NC", "NZ", "NI", "NE", "NG", "NU", "KP", "NO", "OM", "PK", "PW", "PA", "PG", "PY", "PE", "PH", "PN", "PL", "PT", "PR", "QA", "RO", "RU", "RW", "BL", "WS", "SM", "ST", "SA", "SN", "RS", "SC", "SL", "SG", "SK", "SI", "SB", "SO", "ZA", "KR", "ES", "LK", "SH", "PM", "SD", "SR", "SZ", "SE", "CH", "SY", "TW", "TJ", "TZ", "TH", "TG", "TK", "TO", "TN", "TR", "TM", "TV", "AE", "UG", "GB", "UA", "UY", "US", "UZ", "VU", "VA", "VE", "VN", "WF", "YE", "ZM", "ZW"
  };
}
