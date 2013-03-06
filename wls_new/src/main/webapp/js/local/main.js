var map;
var vehicles;


$(document).ready(function () {

    map = new GMaps({
        div: '#map',
        lat: -12.043333,
        lng: -77.028333
    });


    $("#browser").treeview({
        collapsed: true,
        animated: "medium"
    });
    $("#groupSelectDialog").dialog({
        autoOpen: false,
        height: 400,
        width: 450,
        modal: true,
        resizable: false,

        open: function (event, ui) {
            $('body').css('overflow', 'hidden');
            $('.ui-widget-overlay').css('width', '100%');
        },
        close: function (event, ui) {
            $('body').css('overflow', 'auto');
        }
    });
    updateDimensions();
    var groupNum = $.cookies.get('groupNum');
    if (groupNum != null) {
        selectGroup(groupNum);
    }
    $('#leftMenuSP').mCustomScrollbar({
        scrollButtons:{
            enable:true
        },
        theme:"dark-thick",
        advanced:{
            updateOnContentResize: Boolean
        }

    });
})

$(window).resize(function () {
    updateDimensions();
});


function selectGroup(num) {
    $.cookies.set('groupNum', num);

    $.post("getVehicles", {"groupId": num},
        function (data) {
            var leftMenu = '';
            vehicles = new Object();
            map.removeMarkers(map.markers);
            for (var i = 0; i < data.length; i++) {
                var vehicle = data[i];
                vehicles['vehicle' + vehicle.id] = vehicle;


                leftMenu += generateDiv(vehicle);
                map.addMarker({
                    lat: vehicle.lat,
                    lng: vehicle.lon,
                    icon: "../img/vehicleIcons/" + vehicle.imageFileName + "_N_100.png"
                });
            }
            map.fitZoom();
            $('#leftMenu').html(leftMenu);
            $('#groupSelectDialog').dialog('close');
        });


}

function generateDiv(vehicle) {

    //vehReg
    //imageName
    //heading
    var result = ' <div class="vehicleDiv" id="' + vehicle.id + '"  >' +
        '<div class="vehicleHeader" >' +
        '<div class="regNumDiv" onclick="slide(this)">' + vehicle.registrationNumber + '</div>' +
        '<div class="ActionDiv">Action</div>' +
        '<div class="zoomDiv" onclick="zoomIn(this,' + vehicle.id + ')">Zoom</div>' +
        '</div>' +
        '<div class="vehicleContent">' +
        '<div class="vcLeft">' +
        // '<img src="/img/vehicleIcons/glow/grey/'+vehicle.imageFileName+'_E_100.png">'+
        '</div>' +
        '</div>' +
        '</div> ';

    return result;
}

function slide(headerDiv) {
    $(headerDiv).parent().parent().find(".vehicleContent").slideToggle("slow");
}

function zoomIn(div, num) {
    var vehicle = vehicles['vehicle' + num];
    var latLng = new google.maps.LatLng(vehicle.lat, vehicle.lon);
    map.ZoomAt(latLng);

    $('#leftMenu').find(".zoomDiv").each(function (index) {
        var id = $(this).parent().parent().attr('id');
        $(this).attr("onClick", "zoomIn(this," + id + ")");
        $(this).html("Zoom");
    });


    $(div).attr("onClick", "zoomOut(this," + num + ")");
    $(div).html("Zoom out");
}

function zoomOut(div, num) {
    map.fitZoom();
    $(div).attr("onClick", "zoomIn(this," + num + ")");
    $(div).html("Zoom");
}

function updateDimensions() {
    var windowWidth = $(window).width(); //retrieve current window width
    var windowHeight = window.innerHeight;//$(window).height(); //retrieve current window height
    var windowWidthIn = $(window).innerWidth(); //retrieve current window width
    var windowHeightIn = $(window).innerHeight(); //retrieve current window height


    var documentWidth = $(document).width(); //retrieve current document width
    var documentHeight = $(document).height(); //retrieve current document height
    //alert(windowWidth+" "+windowHeight  + " /"+documentWidth+" " +documentHeight+" /" +windowWidthIn+" "+ windowHeightIn);
    // alert(windowHeight+" "+window.innerHeight +"  "+document.body.clientHeight);


    var d = 150;
    $("#menu").height(windowHeight - d);
    $("#leftMenuSP").height(windowHeight - d - 40);

    $("#map").height(windowHeight - d);
    $("#map").width(windowWidth - 380);


}

function expand() {
    $('#leftMenu').find(".vehicleContent").slideDown("slow");
}

