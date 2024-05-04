if ($request.url.indexOf('.starrails.com') != -1||$request.url.indexOf('.hoyoverse.com') != -1||$request.url.indexOf('.mihoyo.com') != -1||$request.url.indexOf('.bhsr.com') != -1) {
    if($response.status!=200){
        $done({status: 200, headers: $response.headers, body: '{"cacheExpirationDays":999,"resultCode":"GOOD","message":"Device Valid"}' })
    }else{
        $done({})
    }
}else{
    $done({})
}
