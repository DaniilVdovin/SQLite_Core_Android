# SQLite_Core_Android
Core for work with SQLlite on Android (Android Studio)

EXAMPLE:
```java
core = new Core(this);

core.setEventListener(()->{
            UpdateAdapter(core.Read());
        });
core.EventListener.Udpate();

Table table = core.Read();
Table tableSearch = core.Search("SOME");
```

