# Embedded InfluxDB

Start influx servers at runtime.

Usage:

```java
class Main {
    public static void main(String[] args){
      InfluxServer server = new InfluxServer.Builder().build();
      
      server.start();
      // [...]
      server.cleanup();
    }
}
```