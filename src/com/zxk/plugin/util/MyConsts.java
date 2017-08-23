package com.zxk.plugin.util;

/**
 * @author zhangxinkun
 */
public interface MyConsts {

  String TO_JSON = "toJson";
  String TO_STRING = "toString";

  String CONVERTER_CLASSNAME = "Converter";
  String CONVERTER_QUALIFIEDNAME = "com.hd123.common.Converter";

  String JSONOBJECT_CLASSNAME = "JSONObject";
  String JSONOBJECT_QUALIFIEDNAME = "org.apache.tapestry.json.JSONObject";

  public enum NeedImport {
    converter(CONVERTER_CLASSNAME, CONVERTER_QUALIFIEDNAME), jsonObject(JSONOBJECT_CLASSNAME, JSONOBJECT_QUALIFIEDNAME);

    public String name;
    public String qualifiedName;

    private NeedImport(String name, String qualifiedName) {
      this.name = name;
      this.qualifiedName = qualifiedName;
    }
  }
}
