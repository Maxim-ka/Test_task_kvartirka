package com.reschikov.kvartirka.testtask.domain.enteries

data class Ad(val id : Int,
              val title :String,
              val address : String,
              val type : String,
              val rooms : Int,
              val price : String,
              val mainPhotoUrl: String,
              val urls : List<String>)