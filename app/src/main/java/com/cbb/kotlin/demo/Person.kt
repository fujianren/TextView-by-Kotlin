package com.cbb.kotlin.demo

import java.sql.DriverManager.println
import java.util.*
import kotlin.reflect.KProperty

/**
 * Created by ZDS-T on 2018/2/26.
 */
class Person {
    var name = "芳儿"

    var age = 28
    /**/
        get() = 18  // 获取数据，访问到的永远都是18，而不是默认的28
        set(value) {
            field = value
            if (value > 18){
                println("${name}年龄${age},是成年人")
            } else {
                println("${name}年龄${age},是未成年人")
            }
        }

    fun printDemo(){
        val obj = Student("小明",18)
        val (a, b) = obj
        val obj2 = obj.copy(age = 0);

//        val ss = Man()
    }

    var money: Int = 0
    operator fun getValue(man: Man, property: KProperty<*>): Int {
        return 100
    }

}

class Man(var name: String, var age: Int){}
data class Student(var name: String, var age: Int)