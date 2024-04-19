# ChipletRing-SDK-Android
## SDK迭代信息
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

# English
## SDK Iteration Information
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