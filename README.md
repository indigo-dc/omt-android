# Requirements

Android Studio 1.5+

# Usage

- checkout the sources
- add new application module
- add project-library as dependency to your module
- remove sample-app module if not needed
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
- add following initialization line in *onCreate* method after *super.onCreate();*
```java
Indigo.init(apiServerURlWithPort, username, apiToken);
```
example:
```java
Indigo.init("http://10.0.0.0:8888", "john", "API_TOKEN");
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
# Changelog

- v0.2 - creating tasks
- v0.1 - getting tasks and task details

# Version

0.2
