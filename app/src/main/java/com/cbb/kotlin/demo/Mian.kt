package com.cbb.kotlin.demo

import java.sql.DriverManager.println

/**
 * Created by ZDS-T on 2018/2/26.
 */

fun main(args: Array<String>){
    println("main函数其实就是一个顶层函数")
}

// 操作符重载，即Kotlin中为基本的运算符提供一个特定的方法表达式，同时你还可以使用operator重载该特定的方法，达到改变运算符计算的效果，称为运算符的重载
// 如：运算符"!"在Kotlin中映射为一个方法not(),
// Boolean类中有用operator修饰的not()方法，即表示运算符"!"被重载了
// 此时一个Boolean对象调用了not()方法，根据Kotlin的类型相近原则，执行的会是Boolean中的方法
// 同理，我们自定义一个XX类，也用operator重写了not()方法
// 此时有xx对象调用not()的话，执行的就是XX类中的方法，
// 而"xx.not()"和"!xx"表达的意思是一样的，执行的是我们重写的内容，而不是传统的非运算
// 其实我们可以理解为，Kotlin中的一些运算符现在映射某些特定的方法名
// 但又给我们重写这些方法的机会，相较与toString()等方法多了一个代理的关键字"operator"
// 每次有对象调用映射的方法名，
// 编译器首先会确定对象的类型，假设为T
// 到T类中找到带operator的对应方法，检查类型返回是不是T的子类
// 确定是运算符方法重写了，执行重写的方法



class Mian {
    var arr = arrayOf(1, 2, 3, 4, 5)
    val list = mutableListOf(0, 1, 2, 3, 4, 5)

    fun test(): Unit {
        val newArr1 = arr.drop(3)
        list.drop(1)
        var a = false
        if (a.not()){}
        if (!a){

        }

    }
}