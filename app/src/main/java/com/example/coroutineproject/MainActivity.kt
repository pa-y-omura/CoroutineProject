package com.example.coroutineproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /** ①
            通常のサブルーチンの場合、処理が`val data = fetchData()`で2秒間せき止められる
            従って、2秒間UIがフリーズする
         */
//        println("practice: start")
//        val data = fetchData()
//        save(data)
//        println("practice: end")


        /**
            ②
            ①を回避する解決策として、Threadを使用可能
            問題点として、Threadが増えていった時、複雑になりがちなので、全てのThreadを管理しきれなくなる
            そのため、一般的にはThreadは直接扱われないことが多い
         */
//        println("practice: start")
//        thread {
//            val data = fetchData()
//            save(data)
//        }
//        println("practice: end")


        /**
            ③
            さらに、②の解決策として、Callbackを使用可能
         */
//        println("practice: start")
//        fetchData {
//            save(it)
//        }
//        println("practice: end")


        /**
            ④
            また、③のCallbackはエラー処理も追記できるため便利
            ただ、Callbackは、Callbackが増えるほどネスとしていくので、後々大変になりがち
         */
//        println("practice: start")
//        fetchData(object : OnDataListener<String> {
//            override fun onSuccess(data: String) {
//                save(data)
//            }
//
//            override fun onFailure(e: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })
//        println("practice: end")


        /**
            ⑤ コードなし
            上記の解決策として、PAでは現状Rxを導入している
         */
//        println("practice: start")
//        fetchData()
//            .concatMapCompletable { save(it) }
//        println("practice: end")

        /**
            ⑥
            上記の解決策として、Coroutineを使用して解決する
            Coroutineだと、Threadが軽量であると共に、同期的に記述できるので、管理がしやすい
         */
//        println("practice: start")
//        CoroutineScope(EmptyCoroutineContext).launch {
//            val data = suspendFetchData()
//            save(data)
//        }
//        println("practice: end")


        /**
            ⑦
            - Coroutineが軽量であることを示す例
            通常のThreadを使用すると、OutOfMemoryErrorが発生
         */
//        repeat(100_000) {
//            thread {
//                Thread.sleep(5000L)
//                println("practice: $it")
//            }
//        }


        /**
            ⑧
            - Coroutineが軽量であることを示す例
            Coroutineを活用すると、軽量ゆえに正常に動作する
         */
//        runBlocking {
//            repeat(100_000) {
//                launch {
//                    delay(5000L)
//                    println("practice: $it")
//                }
//            }
//        }
    }

    fun fetchData(): String {
        Thread.sleep(2000L)
        return "practice: data"
    }

    // ③ Callback用関数
    fun fetchData(callback: (String) -> Unit) {
        thread { // IO処理
            Thread.sleep(2000L)
            callback("practice: data")
        }
    }

    // ④ Callback用関数
    fun fetchData(listener: OnDataListener<String>) {
        thread { // IO処理
            Thread.sleep(2000L)
            listener.onSuccess("practice: data")
        }
    }

    // ⑥ Coroutine用関数
    suspend fun suspendFetchData(): String =
        coroutineScope {
            delay(2000L)
            "practice: data"
        }

    fun save(data: String) {
        println(data)
    }

    interface OnDataListener<T> {
        fun onSuccess(data: T)
        fun onFailure(e: Throwable)
    }
}