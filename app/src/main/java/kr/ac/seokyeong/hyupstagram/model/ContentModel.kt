package kr.ac.seokyeong.hyupstagram.model

data class ContentModel(
    var explain : String? = null, // 사진 설명
    var imageUrl : String? = null, // 사진 주소
    var uid : String? = null, // 유저 시퀸스 값
    var userId : String? = null, // 유저 아이디
    var timestamp : Long? = null, // 업로드 시간
    var favoriteCount : Int = 0, // 좋아요 카운트
    var favorites : MutableMap<String, Boolean> = HashMap() // 좋아요를 누른 사람
) {
    data class Comment(
        var uid : String? = null,
        var userId: String? = null,
        var comment : String? = null,
        var timestamp: Long? = null
    )
}
