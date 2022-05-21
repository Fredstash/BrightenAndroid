package com.example.myapplication

class LightLocation(longitude: Double, latitude: Double, messageType: Int) {
    private var longitude: Double = longitude
    private var latitude: Double = latitude
    private var messageType: Int = messageType

//    Getters
    fun getLongitude(): Double {
        return this.longitude
    }
    fun getLatitude(): Double {
        return this.latitude
    }
    fun getMessageType() : Int {
        return  this.messageType
    }






}