# ChipletRing-SDK-Android
## 如何使用SDK
### 直接使用
**推荐使用最新SDK**   
SDK是一个aar，放在lib里添加即可，详细参考文档:[《安卓SDK中文文档》](https://github.com/Z-y-hu/ChipletRing-SDK-Android/blob/master/BCL603M1%20ChipletRing%20APP%20SDK%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3-Android%20V1.3.4.pdf )
### 使用简单demo
环境：    
ide：AndroidStudio 2022.3.1   
Gradle JDK:11    
Project Structure→Modules→Source Compatibility and Target Ccompatibility:Java8   
在**TestActivity**里修改mac为正确的戒指**mac** 即可连接使用            
通过参考**简单demo对接口使用的示例**，相信您可以快速上手开发自己的程序
## SDK迭代信息
所有SDK都放在SDK文件夹下
### 1.0.4
增加三个接口回调

- 读取时间有读取方法，没有回调方法  
 LmAPI.READ_TIME();
- 采集周期读取方法  
 LmAPI.GET_COLLECTION();
- 清除步数  
 LmAPI.CLEAR_COUNTING();
### 1.0.5
修复1.0.4反馈的新增接口无法使用问题  
现在1.0.5三个接口皆可使用
### 1.0.6
将PPG原始数据解析集成进SDK  
涉及接口

- LmAPI.GET_HEART_ROTA();
- LmAPI.GET_HEART_Q2();  
例：green:67388;accX:-37;accY:-1084;accZ:-294  
                                                                                                    green:78962;accX:-38;accY:-1086;accZ:-293  
                                                                                                    green:85210;accX:-41;accY:-1087;accZ:-289  
                                                                                                    green:77644;accX:-38;accY:-1091;accZ:-290  
                                                                                                    green:62692;accX:-37;accY:-1089;accZ:-290  
                                                                                                    green:41202;accX:-36;accY:-1090;accZ:-285  
                                                                                                    green:24042;accX:-34;accY:-1089;accZ:-282  
                                                                                                    green:-4842;accX:-20;accY:-1089;accZ:-282  

### 1.0.7
修复lmAPI.READ_HISTORY这个接口无法读取全部历史数据的问题  
*其它：将minSdk修改为21*
### 1.0.9
修改了calculateSleep接口，使其返回指定日期的数据   
增加了血压算法接口LmAPI.GET_BPwaveData()，具体使用请看文档
> 注：需要固件支持血压算法  
### 1.0.10
修改了calculateSleep接口，更新了睡眠算法  
增加了部分开发中的功能，不是特定戒指不需要关注
> 注：需要实现新加的抽象类       

# English
## How to use the SDK
### Use it directly
**We recommend that you use the latest SDK**   
The SDK is an aar, you can add it in the lib, please refer to the documentation for details:[《Android SDK Documentation in English》](https://github.com/Z-y-hu/ChipletRing-SDK-Android/blob/master/BCL603M1%20ChipletRing%20%20APP%20SDK%20Manual-Android%20V1.3.4.pdf)
### Use a simple demo
environment：    
ide：AndroidStudio 2022.3.1   
Gradle JDK:11    
Project Structure→Modules→Source Compatibility and Target Ccompatibility:Java8   
In **TestActivity**, change the mac to the correct ring **mac** to connect and use            
By referring to **the example of using the interface in the simple demo**, I believe you can quickly start developing your own program
## SDK Iteration Information
All SDKs are placed under the SDK folder
### 1.0.4
Added three interface callbacks:

- Read time with read method, no callback method
LmAPI.READ_TIME();
- Collection cycle read method  
LmAPI.GET_COLLECTION();
- Clear step count  
LmAPI.CLEAR_COUNTING();
### 1.0.5
Fixed the issue where the new interfaces added in 1.0.4 could not be used
Now, in version 1.0.5, all three interfaces are available.
### 1.0.6
Integrate PPG raw data parsing into the SDK  
Interfaces are involved 

- LmAPI.GET_HEART_ROTA();
- LmAPI.GET_HEART_Q2();  
example：green:67388;accX:-37;accY:-1084;accZ:-294  
                                                                                                    green:78962;accX:-38;accY:-1086;accZ:-293  
                                                                                                    green:85210;accX:-41;accY:-1087;accZ:-289  
                                                                                                    green:77644;accX:-38;accY:-1091;accZ:-290  
                                                                                                    green:62692;accX:-37;accY:-1089;accZ:-290  
                                                                                                    green:41202;accX:-36;accY:-1090;accZ:-285  
                                                                                                    green:24042;accX:-34;accY:-1089;accZ:-282  
                                                                                                    green:-4842;accX:-20;accY:-1089;accZ:-282  

### 1.0.7
Fixed the issue that lmAPI.READ_HISTORY this interface could not read all historical data  
*Other: Change minSdk to 21*
### 1.0.9
The **calculateSleep interface** has been modified to return data for the specified date  
Added the blood pressure algorithm interface **LmAPI.GET_BPwaveData()**, please refer to the documentation for specific use
> Note: Firmware is required to support the blood pressure algorithm     
### 1.0.10
The calculateSleep API has been modified and the sleep algorithm has been updated  
Added some features in development, not specific rings don't need attention
> Note:A newly added abstract class needs to be implemented 