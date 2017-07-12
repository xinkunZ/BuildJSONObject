# BuildJSONObject
* Generate `toJson` Method For Java Beans
* How To Use:
  * Alt+Insert - toJSON
* Json tool supported by `org.apache.tapestry.json.JSONObject`
  
# Version Notes
### version 1.2.1
* If not java file,remove toJson from generate items

### version 1.2
* Fix configuration persist bug

### version 1.1
* Support One-To-Many Java Beans 
* Only use private fields to build json
* Support ignore fields(like serialVersionUID, logger,etc.) By IDEA Setttings
* Support un-String type fields generate toString() automatically
  * If Date type field, use `Converter.toString()`;
  * If BigDecimal type field
    * If field name contain "qty"， use `Converter.toQtyString()`;
    * Else use `Converter.toMoneyString()`;
* Support import `com.hd123.common.Converter` and `org.apache.tapestry.json.JSONObject packages` automatically

### version 0.1
  * First Upload
      
## Licence
* **[Apache](http://www.apache.org/licenses/LICENSE-2.0)**  
