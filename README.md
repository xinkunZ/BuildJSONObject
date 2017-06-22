# BuildJSONObject
* 快速为 java bean 生成 toJson()和 toJson.toString()方法
* 使用方法：alt+insert - toJson
  * Json 对象使用 org.apache.tapestry.json.JSONObject
  
# Version Notes
### version 1.1
* 支持line行添加
* 插件只查找private类型的字段，并且支持设置排除的field名称，如序列化id，logger等
* 如果是Date类型的field，则使用jpos.Converter.toString();
* 如果是Bigdecimal类型的field
  * 如果field名称包含qty，则使用toQtyString();
  * 否则使用toMoneyString();
* 支持自动导入Converter和JSONObject包

### version 0.1
  * first upload
      
## Licence
* **[Apache](http://www.apache.org/licenses/LICENSE-2.0)**  
