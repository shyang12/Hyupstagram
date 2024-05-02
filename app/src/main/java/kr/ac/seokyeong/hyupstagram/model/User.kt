package kr.ac.seokyeong.hyupstagram.model

data class User(
    var email: String,
    var uId: String,
){
    constructor(): this("", "")
}
