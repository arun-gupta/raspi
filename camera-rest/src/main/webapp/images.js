BASE_URL = "http://localhost:8080/camera-rest/webresources/";

var displayedImages = [];

onload = function() {
    loadImages();
    setInterval(loadImages, 3000);
};

function loadImages() {
    var request = new XMLHttpRequest();
    request.open("GET", BASE_URL + "images");
    request.onload = function() {
        if (request.status === 200) {
            var results = JSON.parse(request.responseText);
            results.sort();            
            for (var i = 0; i < results.length; i++) {
                loadImage(results[i]);
            }
        } else {
            console.log("Cannot load images: " + request.status + " - " + request.responseText);
        }
    };
    request.send(null);
}

function loadImage(image) {
    
    if ($.inArray(image, displayedImages) !== -1) {
        return;
    }
    
    var a = $("<a>").attr("href", BASE_URL + "images/" + image);
    var img = $("<img>").attr("src", BASE_URL + "images/thumbs/" + image).attr("alt", image);
    a.append(img);
    $("#images").prepend(a);
    displayedImages.push(image);
}