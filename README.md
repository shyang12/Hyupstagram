# Hyupstagram
## ▶ MVVM화면 패턴과 Firebase 활용해 안드로이드 SNS 앱 만들기
 
 - 안드로이드 스튜디오에서 Kotlin을 기반으로 이미지 기반 소셜 미디어 서비스를 구현하는 프로젝트

`MVVM` `ViewBinding` `dataviewbinding` `Glide` `Firebase`

## 1. Co-Development Environment   
### 1. 1 Environments
- Windows 10
- Android Studio / Kotlin 
- Firebase

## 2. Project Architecture   
```bash
├── fragment
│   ├── AlarmFragment.kt
│   ├── DetailViewFragment.kt
│   ├── GridFragment.kt
│   └── UserFragment.kt
├── login
│   ├── FindIdActivity.kt
│   ├── FindIdViewModel.kt
│   ├── InputNumberActivity.kt
│   ├── InputNumberViewModel.kt
│   ├── LoginActivity.kt
│   └── LoginViewModel.kt
├── model
│   ├── AlarmDTO.kt
│   ├── ContentModel.kt
│   ├── FollowDTO.kt
│   └── PushDTO.kt
├── navigation
│   └── CommentActivity.kt
├── util
│   └── FcmPush.kt
├── main
│   ├── MainActivity.kt
│   └── AddPhotoActivity.kt
├── ui
│   ├── layout
│   │   ├── activity_add_photo.xml
│   │   ├── activity_comment.xml
│   │   ├── activity_find_id.xml
│   │   ├── activity_input_number.xml
│   │   ├── activity_login.xml
│   │   ├── activity_main.xml
│   │   ├── fragment_alarm.xml
│   │   ├── fragment_detail_view.xml
│   │   ├── fragment_grid.xml
│   │   ├── fragment_user.xml
│   │   ├── item_comment.xml
│   │   └── item_detail.xml
│   ├── menu
        └── bottom_navigation.kt
```

## 3. Firebase   
### 3.1 Authentication

### 3.2 Realtime Database
```bash
├── alarms
│   └── alarm
│        ├── destinationUid
│        ├── kind
│        ├── message
│        ├── timestamp
│        ├── uid
│        └── userId
├── findIds
│   └── findId
│        ├── id
│        └── phoneNumber
├── images
│   └── image
│        └── comments
│              ├── explain
│              ├── favoriteCount
│              ├── favorties
│              ├── imageUrl
│              ├── timestamp
│              ├── uid
│              └── userId
├── profileImages
│   └── porgileImage
│        └── iamge
├── pushtokens
│   └── PushToken
│       
└── users
      └── userId
            ├── followerCount
            ├── followers
            ├── followingCount
            └── followings
```

### 3.3 Storage
```bash
├── images
│   └── IMAGE_FILE.png
└── userProfileimages
    └── PROFILEIMAGE_FILE.png
```

### 3.4 Cloud Messaging

## 4. Result   
- 로그인 및 프로필 편집

- 게시글 CRUD (갱신 제외)

- MyPage Gridview 편집
