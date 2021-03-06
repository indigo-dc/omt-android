# Requirements

- Android Studio 1.5+
- Android API Level >= 17

# Usage

- add the dependency to the app's module build.gradle config
```gradle
compile 'pl.psnc.indigo:indigo-omt-android-library:0.8.7'
```
- create custom Application class which extends Android default Application class 
- remember to update your manifest.xml with the name of your Application class
```xml
    ...
   <application
        android:name=".YourCustomApplicationClass"
        android:allowBackup="true"
        android:icon="@drawable/logo"
    ...
```
- add following initialization line in your YourCustomApplicationClass in *onCreate* method after *super.onCreate();*
```java
Indigo.init(context, user, server);
```
example:
```java
Indigo.init(this, "futuregateway", "http://62.3.168.167");
```
- now you are able to call methods which wrap request to INDIGO DataCloud API. Example:
```java
Indigo.getTasks(TaskStatus.ANY, new TasksApi.TasksCallback() {
    @Override
    public void onSuccess(List<Task> tasks) {
        //do something with tasks!
    }
    @Override
    public void onError(Exception e) {
        //do something on error
    }
});
```
