

###preview
![gif](https://github.com/anderson9/LovelyProgressBar/blob/master/gif/preview.gif)


###Dependency(引入)

```java
dependencies {
	compile 'com.ljs:LovelyProgressBar:1.0.0'
}
```


###Usage （用法）
```java
<com.ljs.lovelyprogressbar.LovelyProgressBar
        android:id="@+id/loadbar"
        android:layout_width="100dp"
        android:layout_height="100dp"
        custom:lineColor="@android:color/white"
        custom:lineWidth="8"
        custom:textSize="16sp"
        />
```
若不设置
线宽默认：8
颜色默认：Green
textsize默认：16sp

``` java
LovelyProgressBar mbar=(LovelyProgressBar) findViewById(R.id.loadbar);;

mbar.startload();//设置progress前先startload（）
mbar.setProgress(int progress);//设置进度

//加载成功调用即可成功动画
mbar.errorLoad();
//加载失败调用即可执行失败动画
mbar.succesLoad();


```
如果progress=100。自动启动succesload动画

####动画回调(Listener)
```java
mbar.setOnLoadListener(new LovelyProgressBar.OnLoadListener() {
           @Override
           public void onAnimSuccess() {
               Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
           }
           @Override
           public void onAnimError() {
               Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
           }
       });
```



###Licence
This Licence,
don't need a Public Licence!i don't care!
If u like the LovelyToast,
give me a star,
don't be such a dick,
u just do what u want to do.
（不需要licence，大家想怎么用怎么用，给个star即可，thanks！！ ）
