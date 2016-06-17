# XmlRpcAndroid
XmlRpc is an android extension for using with retrofit and putting serialization or deserialization of xml-rpc service endpoints into object oriented way by using SimpleXml to serialize/deserialize.

Sample Code;

```xml
<?xml version="1.0" encoding="utf-8"?>
<methodCall>
   <methodName>SampleMethodName</methodName>
   <params>
      <param>
         <value>
            <string>stringParam</string>
         </value>
      </param>
   </params>
</methodCall>
```
in java
```java
XMLRpcRequest requestGetSubtitleLanguages = new XMLRpcRequest()
                .withMethodName("SampleMethodName")
                .add(XMLRpcParam.withObject(XMLRpcObject.withValue("stringParam")));
```
another example with different types
```xml
<?xml version="1.0" encoding="utf-8"?>
<methodCall>
   <methodName>SampleMethodName</methodName>
   <params>
      <param>
         <value>
            <string>param1</string>
         </value>
      </param>
      <param>
         <value>
            <double>1.0</double>
         </value>
      </param>
      <param>
         <value>
            <boolean>1</boolean>
         </value>
      </param>
      <param>
         <value>
            <dateTime.iso8601>20160617T22:37:12</dateTime.iso8601>
         </value>
      </param>
      <param>
         <value>
            <int>1</int>
         </value>
      </param>
   </params>
</methodCall>
```
in java
```java
   XMLRpcRequest requestGetSubtitleLanguages = new XMLRpcRequest()
                .withMethodName("SampleMethodName")
                .add(XMLRpcParam.withObject(XMLRpcObject.withValue("param1")))//string
                .add(XMLRpcParam.withObject(XMLRpcObject.withValue(1d)))//double
                .add(XMLRpcParam.withObject(XMLRpcObject.withValue(true)))//boolean
                .add(XMLRpcParam.withObject(XMLRpcObject.withValue(new Date())))//date formated as "yyyyMMdd'T'HH:mm:ss"
                .add(XMLRpcParam.withObject(XMLRpcObject.withValue(1)));//integer
```
