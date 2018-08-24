# AndroidJoyStickView
![libimage](https://user-images.githubusercontent.com/22986571/44574794-07ae2e00-a7a8-11e8-9c60-c73c50294e80.jpg)

[![Platform](https://img.shields.io/badge/platform-android-blue.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-14%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.html)

This library lets you create joystick with some customization for android

Gradle
------

#### Add the dependency

```
dependencies {
    ...
    implementation 'com.jackandphantom.android:joystickview:1.0.2'
}
```
# Basic Usage

#### AndroidJoystickView XML

In order to use AndroidLikeButton copy following xml code in your xml file.

```xml

 <com.jackandphantom.joystickview.JoyStickView
     android:layout_width="150dp"
     android:layout_height="150dp"
     app:outerCircleBorderColor="#fff"
     app:innerCircleColor="#151414"
     app:outerCircleBorderWidth="5"
     app:shadowRadius="7"
     app:shadowColor="#000"
     app:outerCircleColor="#a6a9551e"
     app:innerCircleImage="@drawable/ic_directions_run_black_24dp"
     android:layout_alignParentBottom="true">

```

#### AndroidJoystickView Interface

```java
JoyStickView joyStickView = findViewById(R.id.joy);
joyStickView.setOnMoveListener(new JoyStickView.OnMoveListener() {
            @Override
            public void onMove(double angle, float strength) {
                
            }
        });

```

# Concept

There are two views in JoyStick 

1. InnerCircleView : This is small circle in joystick as you can see in image so attributes related to this view will using innerCircle
in prefix in xml

2. OuterCircleView : This is big circle in joystick so all attributes related to this will using outerCircle in prefix in xml

# Attributes

There are several attribute to configure AndroidJoystick view, they are following

#### InnerCircleView Attributes

 #### innerCircleColor , innerCircleImage
 
 In order to change the inner Circle color and add the image into the small circle use the following code :- 
 
 ```xml
 <com.jackandphantom.joystickview.JoyStickView
      app:innerCircleColor="#151414"
      app:innerCircleImage="@drawable/ic_directions_run_black_24dp"
  />

```
In Java 

```java
  joyStickView.setInnerCircleImageResId(R.drawable.ic_directions_run_black_24dp);
  joyStickView.setInnerCircleColor(Color.BLACK);

```

#### innerCircleRadius

Before changing the size of small circle you need to understand small concep which is both inner and outer circle has ratio 0.1f and 0.4f respectively so you have to maintain 5.0f in total. so when you change innerCircleRadius value it substracted outercircle value from 5.0f.

```xml
 <com.jackandphantom.joystickview.JoyStickView
    app:innerCircleRadius="0.15"
  />

```
In Java 

```java
joyStickView.setInnerCircleRadius(0.1f);

```
#### OuterCircleView Attributes

#### outerCircleColor , outerCircleBorderWidth , outerCircleBorderColor

In order to change outerCircle attributes like it's color , bordercolor and borderWidth you can use following code :

```xml
 <com.jackandphantom.joystickview.JoyStickView
    app:outerCircleBorderColor="#fff"
     app:outerCircleBorderWidth="5"
     app:outerCircleColor="#a6a9551e"
  />

```
In Java 

```java
 joyStickView.setOuterCircleBorderColor(Color.RED);
 joyStickView.setOuterCircleBorderStrokeWidth(15);
 joyStickView.setOuterCircleColor(Color.WHITE);

```
### lockCenter

The default property of joystickview is that when move it and you release it then it automatically came to the center and lockCenter is the property in which it lock the small circle when it stength is equal to 100 so if you want to lock it then 

```xml
 <com.jackandphantom.joystickview.JoyStickView
    app:lockCenter="true"
  />

```
In Java 

```java
 joyStickView.setLockCenter(false);

```
#### Shadow

If you want to addt the shadow in joystick first you need to add outerCircleBorderWidth value and then you can change these properties 

#### ShadowRadius , shadowColor , Dx, Dy

```xml
 <com.jackandphantom.joystickview.JoyStickView
     app:shadowColor="#000"
     app:shadowRadius="7"
     app:shadowDy="5"
     app:shadowDx="5"
  />

```
In Java 

```java
joyStickView.setShadowColor(Color.BLACK);
joyStickView.setShadowRadius(7f);
joyStickView.setShadowDxAndDy(5f, 5f);

```

 LICENCE
-----

 Copyright 2018 Ankit kumar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 Special Thanks to Manish and zerokol

