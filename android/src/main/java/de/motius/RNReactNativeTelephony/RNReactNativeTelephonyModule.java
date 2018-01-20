
package de.motius.RNReactNativeTelephony;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import android.telephony.TelephonyManager;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import android.content.Context;
import android.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.Lo

import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import java.util.Arrays
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;

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
    TelephonyManager manager = (TelephonyManager) this.reactContext.getSystemService(Context.TELEPHONY_SERVICE);
    String phoneNumber = manager.getLine1Number();
    WritableMap map = Arguments.createMap();
    map.putString("phoneNumber", phoneNumber);

    promise.resolve(map);
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  @ReactMethod
  public void getCellInfo(Callback successCallback) {

      WritableArray mapArray = Arguments.createArray();
      TelephonyManager telephonyManager = (TelephonyManager) this.reactContext.getSystemService(Context.TELEPHONY_SERVICE);
      List<CellInfo> cellInfo = telephonyManager.getAllCellInfo();

      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || cellInfo == null) {
          successCallback.invoke(mapArray);
          return;
      }

      int i = 0;

      for (CellInfo info : cellInfo) {
          WritableMap mapCellIdentity = Arguments.createMap();
          WritableMap mapCellSignalStrength = Arguments.createMap();
          WritableMap map = Arguments.createMap();

          map.putInt("key", i);

          if (info instanceof CellInfoGsm) {
              CellIdentityGsm cellIdentity = ((CellInfoGsm) info).getCellIdentity();
              map.putString("connectionType", "GSM");

              mapCellIdentity.putInt("cid", cellIdentity.getCid());
              mapCellIdentity.putInt("lac", cellIdentity.getLac());
              mapCellIdentity.putInt("mcc", cellIdentity.getMcc());
              mapCellIdentity.putInt("mnc", cellIdentity.getMnc());
              mapCellIdentity.putInt("psc", cellIdentity.getPsc());

              CellSignalStrengthGsm cellSignalStrengthGsm = ((CellInfoGsm) info).getCellSignalStrength();
              mapCellSignalStrength.putInt("asuLevel", cellSignalStrengthGsm.getAsuLevel());
              mapCellSignalStrength.putInt("dBm", cellSignalStrengthGsm.getDbm());
              mapCellSignalStrength.putInt("level", cellSignalStrengthGsm.getLevel());
          } else if (info instanceof CellInfoWcdma && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
              CellIdentityWcdma cellIdentity = ((CellInfoWcdma) info).getCellIdentity();
              map.putString("connectionType", "WCDMA");

              mapCellIdentity.putInt("cid", cellIdentity.getCid());
              mapCellIdentity.putInt("lac", cellIdentity.getLac());
              mapCellIdentity.putInt("mcc", cellIdentity.getMcc());
              mapCellIdentity.putInt("mnc", cellIdentity.getMnc());
              mapCellIdentity.putInt("psc", cellIdentity.getPsc());

              CellSignalStrengthWcdma cellSignalStrengthWcdma = ((CellInfoWcdma) info).getCellSignalStrength();

              mapCellSignalStrength.putInt("asuLevel", cellSignalStrengthWcdma.getAsuLevel());
              mapCellSignalStrength.putInt("dBm", cellSignalStrengthWcdma.getDbm());
              mapCellSignalStrength.putInt("level", cellSignalStrengthWcdma.getLevel());
          } else if (info instanceof CellInfoLte) {
              if(info.isRegistered()) {
                  mapCellIdentity.putBoolean("servingCellFlag", info.isRegistered());
              } else {
                  mapCellIdentity.putBoolean("servingCellFlag", info.isRegistered());
              }
              CellIdentityLte cellIdentity = ((CellInfoLte) info).getCellIdentity();
              map.putString("connectionType", "LTE");

              mapCellIdentity.putInt("cid", cellIdentity.getCi());
              mapCellIdentity.putInt("tac", cellIdentity.getTac());
              mapCellIdentity.putInt("mcc", cellIdentity.getMcc());
              mapCellIdentity.putInt("mnc", cellIdentity.getMnc());
              mapCellIdentity.putInt("pci", cellIdentity.getPci());

              String cellIdHex = decToHex(cellIdentity.getCi());
              String eNodeBHex = cellIdHex.substring(0, cellIdHex.length() - 2);
              mapCellIdentity.putInt("eNodeB", hexToDec(eNodeBHex));
              String localCellIdHex = cellIdHex.substring(cellIdHex.length() - 2, cellIdHex.length());
              mapCellIdentity.putInt("localCellId", hexToDec(localCellIdHex));

              CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte) info).getCellSignalStrength();

              mapCellSignalStrength.putInt("asuLevel", cellSignalStrengthLte.getAsuLevel());
              mapCellSignalStrength.putInt("dBm", cellSignalStrengthLte.getDbm());
              mapCellSignalStrength.putInt("level", cellSignalStrengthLte.getLevel());
              mapCellSignalStrength.putInt("timingAdvance", cellSignalStrengthLte.getTimingAdvance());

          } else if (info instanceof CellInfoCdma) {
              CellIdentityCdma cellIdentity = ((CellInfoCdma) info).getCellIdentity();
              map.putString("connectionType", "CDMA");

              mapCellIdentity.putInt("basestationId", cellIdentity.getBasestationId());
              mapCellIdentity.putInt("latitude", cellIdentity.getLatitude());
              mapCellIdentity.putInt("longitude", cellIdentity.getLongitude());
              mapCellIdentity.putInt("networkId", cellIdentity.getNetworkId());
              mapCellIdentity.putInt("systemId", cellIdentity.getSystemId());

              CellSignalStrengthCdma cellSignalStrengthCdma = ((CellInfoCdma) info).getCellSignalStrength();

              mapCellSignalStrength.putInt("asuLevel", cellSignalStrengthCdma.getAsuLevel());
              mapCellSignalStrength.putInt("cmdaDbm", cellSignalStrengthCdma.getCdmaDbm());
              mapCellSignalStrength.putInt("cmdaEcio", cellSignalStrengthCdma.getCdmaEcio());
              mapCellSignalStrength.putInt("cmdaLevl", cellSignalStrengthCdma.getCdmaLevel());
              mapCellSignalStrength.putInt("dBm", cellSignalStrengthCdma.getDbm());
              mapCellSignalStrength.putInt("evdoDbm", cellSignalStrengthCdma.getEvdoDbm());
              mapCellSignalStrength.putInt("evdoEcio", cellSignalStrengthCdma.getEvdoEcio());
              mapCellSignalStrength.putInt("evdoLevel", cellSignalStrengthCdma.getEvdoLevel());
              mapCellSignalStrength.putInt("evdoSnr", cellSignalStrengthCdma.getEvdoSnr());
              mapCellSignalStrength.putInt("level", cellSignalStrengthCdma.getLevel());
          }

          map.putMap("cellIdentity", mapCellIdentity);
          map.putMap("cellSignalStrength", mapCellSignalStrength);

          mapArray.pushMap(map);
          i++;
      }
      successCallback.invoke(mapArray);
  }

  public String decToHex(int dec) {
      return String.format("%x", dec);
  }

  public int hexToDec(String hex) {
      return Integer.parseInt(hex, 16);
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
