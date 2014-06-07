BASE_URL = document.location.href + "webresources/";

onload = function() {
    loadImages();
    setInterval(loadImages, 3000);
};

function loadImages() {
    $("#images").empty();
    var request = new XMLHttpRequest();
    request.open("GET", BASE_URL + "images");
    request.onload = function() {
        if (request.status === 200) {
            var results = JSON.parse(request.responseText);
            for (var i = 0; i < results.length; i++) {
                loadImage(results[i]);
            }
        } else {
            console.log("Cannot load images: " + request.status + " - " + request.responseText);
        }
    };
    request.send(null);
}

function loadImage(imageName) {
    var p = $("<p>");
    var img = $("<img>").attr("src", BASE_URL + "images/" + imageName).attr("alt", imageName);
    p.append(img);
    $("#images").append(p);
}