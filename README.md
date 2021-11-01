# Изучение работы с сетью Retrofit + RxJava3 (Version 0.1)


Описание:
1. Приложение отправляет запрос в сеть 
2. Приложение отображает список фильмов по поиску (планируется)
3. Приложение может сохранять список фильмов (планируется)
4. Можно удалить список добавленных фильмов (планируется)
5. Можно редактировать добавленные фильмы (планируется)

[Быстрый переход к коду проекта](url)

## Использованные источники:
1. [Android играюче Retrofit](http://developer.alexanderklimov.ru/android/library/retrofit.php)
2. [Видео YouTybe Retrofit. Часть 1. Основы [RU] / Мобильный разработчик](https://youtu.be/mVx3_vSxbJU)
3. [Как в Android получить данные с сервера с помощью Retrofit](https://startandroid.ru/ru/blog/506-retrofit.html)
4. [Документация Retrofit](https://square.github.io/retrofit/)
5. [Пример моего проекта по поиску фильмов](https://github.com/Sguruu/Movie-Search/blob/master/app/src/main/java/com/example/moviesearch/network/Network.kt)
6. [OMDb API](http://www.omdbapi.com/)
7. [Статья Изучаем Retrofit 2](https://habr.com/ru/post/314028/)
8. [Json Parser](http://json.parser.online.fr/)
9. [Retrofit 2 аннотации не найдено](https://coderoad.ru/35345304/Retrofit-2-%D0%B0%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D0%B8-%D0%BD%D0%B5-%D0%BD%D0%B0%D0%B9%D0%B4%D0%B5%D0%BD%D0%BE)
10. [No Retrofit annotation found. parameter #1](https://stackoverflow.com/questions/28371305/no-retrofit-annotation-found-parameter-1)
11. [Документация Picasso](https://square.github.io/picasso/)
##

# Важные моменты 
Подключение к проекту 
```.gradle
plugins {
    id 'com.android.application'
    id 'kotlin-android'
}

android {
    compileSdk 31

    defaultConfig {
        applicationId "com.example.cleanretrofit"
        minSdk 21
        targetSdk 31
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
}

dependencies {

    implementation 'androidx.core:core-ktx:1.7.0'
    implementation 'androidx.appcompat:appcompat:1.3.1'
    implementation 'com.google.android.material:material:1.4.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.1'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'

    // Retrofit 2 API21+
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.retrofit2:adapter-rxjava3:2.9.0'
    implementation 'com.squareup.okhttp3:okhttp:4.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.8.0'

    // RxJava
    implementation 'io.reactivex.rxjava3:rxandroid:3.0.0'
    implementation 'io.reactivex.rxjava3:rxandroid:3.0.0'

}

```
# Манифест 

Разрешение доступа к интернету 

```  <uses-permission android:name="android.permission.INTERNET" /> ```
``` <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> ```

Добавления конфигурации безопасности (чтобы можно было общаться по протоколу http)

``` android:networkSecurityConfig="@xml/network_security_config" ```
```.xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.cleanretrofit">
    <!-- Разрашение для доступа к интернету  -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:name=".MyApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:networkSecurityConfig="@xml/network_security_config"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.CleanRetrofit">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

Описание конфигурации бехопасности ```"@xml/network_security_config"```
```.xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">www.omdbapi.com</domain>
    </domain-config>
</network-security-config>
```

# Стадия готовности проекта : В ПРОЦЕССЕ 

