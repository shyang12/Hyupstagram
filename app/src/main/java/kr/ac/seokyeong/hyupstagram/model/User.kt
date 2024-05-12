package kr.ac.seokyeong.hyupstagram.model


data class User(
    var email: String? = null,
    var uId: String? = null
){
    constructor(): this("", "")
}
