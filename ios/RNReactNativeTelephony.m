#import "RNReactNativeTelephony.h"

@implementation RNReactNativeTelephony

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(getCountryCode:(RCTResponseSenderBlock) callback){
    NSString * plistPath = [[NSBundle mainBundle] pathForResource:@"DiallingCodes" ofType:@"plist"];
    NSDictionary *diallingCodesDictionary = [NSDictionary dictionaryWithContentsOfFile:plistPath];
    
    CTTelephonyNetworkInfo *network_Info = [CTTelephonyNetworkInfo new];
    CTCarrier *carrier = network_Info.subscriberCellularProvider;
    
    NSMutableDictionary* result = [NSMutableDictionary dictionary];
    
    [result setValue:carrier.mobileCountryCode ? carrier.mobileCountryCode : [NSNull null] forKey:@"mobileCountryCode"];
    
    [result setValue:carrier.mobileNetworkCode ? carrier.mobileNetworkCode : [NSNull null] forKey:@"mobileNetworkCode"];
    

    if(carrier.isoCountryCode){
        [result setValue:carrier.isoCountryCode forKey:@"isoCountryCode"];
        [result setValue:[diallingCodesDictionary objectForKey:(NSString *)carrier.isoCountryCode] forKey:@"callPrefix"];
    }else{
        [result setValue:[NSNull null] forKey:@"isoCountryCode"];
        [result setValue:[NSNull null] forKey:@"callPrefix"];
    }
    callback(@[[NSNull null], result]);
}

@end
  
