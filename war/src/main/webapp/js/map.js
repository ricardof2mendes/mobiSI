var map;
var mapProjection = new OpenLayers.Projection("EPSG:900913");
var mapDisplayProjection = new OpenLayers.Projection("EPSG:4326");

var layerStyle = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['default']);
layerStyle.fillOpacity = 0.2;
layerStyle.graphicOpacity = 1; 

var greenStyle = OpenLayers.Util.extend({}, layerStyle);
greenStyle.fillColor = "#ACE228";
greenStyle.strokeColor = '#94C222';

var yellowStyle = OpenLayers.Util.extend({}, layerStyle);
yellowStyle.fillColor = "#FFF000";
yellowStyle.strokeColor = '#B7AD00';

var carMarker = OpenLayers.Util.extend({}, layerStyle);
carMarker.fillOpacity = 1;

$(document).ready(function() {
	//Menu button
	$('.menuBtn').on('click', function(e) {
		if($('#legend').css('display') != 'none') {
			toggleFx('#legend');
		} else {
			toggleFx('div.bottomShadow');
		}
		toggleFx('#menu');
	});
	
	// Legend button
	$('.legendBtn').on('click', function(e) {
		if($('#menu').css('display') != 'none') {
			toggleFx('#menu');
		} else {
			toggleFx('div.bottomShadow');
		}
		toggleFx('#legend');
	});
});


var MapACar = function(licensePlate) {
	//Build parts
	buildCommon();
	// process data attached to license plate
	fetchCarData(licensePlate);
};

var MapAPark = function(licensePlate) {
	//Build parts
	buildCommon();
	// process data attached to park
	fetchParkData(licensePlate);
};

/**
 * Build common map and controls
 */
function buildCommon() {
	// remove spacing
	$('div.bottomShadow').css('display', 'none');
	
	// crete the map, add navigation and zoom controls, add basic layer open street map
	map = new OpenLayers.Map({
		div : "map",
		theme : null,
		controls : [ new OpenLayers.Control.Attribution(),
				new OpenLayers.Control.TouchNavigation({
					dragPanOptions : {
						enableKinetic : true
					}
				}), new OpenLayers.Control.Zoom() ],
		layers : [ new OpenLayers.Layer.OSM("OpenStreetMap", null, {
			transitionEffect : 'resize'
		}) ],
        numZoomLevels: 18
	});
	
	// change button color according to header
	var background = $('body > header').css('background-color');
	var normal = 'none repeat scroll 0 0 rgba('+background.substring(background.indexOf('(')+1, background.indexOf(')')) + ', 0.5)';
	var hover = 'none repeat scroll 0 0 rgba('+background.substring(background.indexOf('(')+1, background.indexOf(')')) + ', 0.7)';
	
	$('div.olControlZoom a').css('background', normal);
	$('div.olControlZoom a').on('mouseover', function(){
		$(this).css('background', hover);
		$(this).on('mouseout', function(){
			$(this).css('background', normal);
		});
	});
}

/**
 * Determine My location and add it to the map
 * 
 * @param map
 */
function addMyLocation(vectorLayer) {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(
			function(position) {
				var feature = new OpenLayers.Feature.Vector(
						new OpenLayers.Geometry.Point(position.coords.longitude, position.coords.latitude).transform(mapDisplayProjection, map.getProjectionObject()), {
									some : 'data'
								}, {
									externalGraphic : '../img/map/mylocation.png',
									graphicHeight : 25,
									graphicWidth : 21
								});
				vectorLayer.addFeatures(feature);
				map.addLayer(vectorLayer);
				map.zoomToExtent(vectorLayer.getDataExtent());
				map.zoomTo(map.getZoom()-1);
			}, 
			function (err) {
				if (err.code == 1) {
					alert('The user denied the request for location information.');
				} else if (err.code == 2) {
					alert('Your location information is unavailable.');
				} else if (err.code == 3) {
					alert('The request to get your location timed out.');
				} else {
					alert('An unknown error occurred while requesting your location.');
				}
			}, {
				maximumAge: 60000,
				enableHighAccuracy: true 
			});
	} else {
		alert("Geolocation is not supported by this browser.");
	}
}

/**
 * Fetch data from server related to given license plate
 * @param licensePlate
 */
function fetchCarData(licensePlate) {
	$.get(contextPath + '/booking/ImmediateBooking.action?carData=&licensePlate=' + licensePlate, 
			function(data, textStatus, jqXHR){
				if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
					processData(eval(data));
		        } else {
		            console.log('An error has occurred or the user\'s session has expired!');
		        }
		    });	
}

/**
 * Fetch data from server related to given park
 * @param licensePlate
 */
function fetchParkData(licensePlate) {
	$.get(contextPath + '/booking/AdvanceBooking.action?parkData=&licensePlate=' + licensePlate, 
			function(data, textStatus, jqXHR){
				if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
					processData(eval(data));
		        } else {
		            console.log('An error has occurred or the user\'s session has expired!');
		        }
		    });	
}

/**
 * Process the received server data
 * @param returnData
 */
function processData(returnData) {
	
	var addedMyLocation = false;
	
	// car coordinates
	if(returnData.coordinate) {
		
		// create the car vector
		var objectVector = new OpenLayers.Layer.Vector("objects", {style: carMarker});
		var feature = new OpenLayers.Feature.Vector(
				new OpenLayers.Geometry.Point(returnData.coordinate.longitude, returnData.coordinate.latitude).transform(mapDisplayProjection, map.getProjectionObject()));
		objectVector.addFeatures(feature);
		map.addLayer(objectVector);
		// center the map on the car
		var mapCenter = new OpenLayers.LonLat(returnData.coordinate.longitude, returnData.coordinate.latitude);
		map.setCenter (mapCenter.transform(mapDisplayProjection, map.getProjectionObject()), 14);	
		
		// add my location to the map
		addMyLocation(objectVector);
		addedMyLocation = true;
	}
	
	// zones delimiters
	if(returnData.zones && returnData.zones.length > 0) {
		
		$(returnData.zones).each(function() {
			var zonesVector;
			if(this.category.value == 'NORMAL') {
				zonesVector = new OpenLayers.Layer.Vector("zones", {style: greenStyle});
			} else if(this.category.value == 'SPECIAL') {
				zonesVector = new OpenLayers.Layer.Vector("zones", {style: greenStyle});
			} else if(this.category.value == 'UNWANTED') {
				zonesVector = new OpenLayers.Layer.Vector("zones", {style: yellowStyle});
			} else if(this.category.value == 'FORBIDDEN') {
				zonesVector = new OpenLayers.Layer.Vector("zones", {style: grayStyle});
			} else {
				// PARKING
				zonesVector = new OpenLayers.Layer.Vector("zones", {style: greenStyle});
			}
			
			 var pointList = [];
			 $(this.polygon.coordenates).each(function() {
				 var newPoint = new OpenLayers.Geometry.Point(this.longitude, this.latitude).transform(mapDisplayProjection, map.getProjectionObject());
				 pointList.push(newPoint);
			 });
			 pointList.push(pointList[0]);
			 var linearRing = new OpenLayers.Geometry.LinearRing(pointList);
			 var polygonFeature = new OpenLayers.Feature.Vector(
					 new OpenLayers.Geometry.Polygon([linearRing])); 
			 zonesVector.addFeatures(polygonFeature);
			 map.addLayer(zonesVector);
			 
			 if(!addedMyLocation) {
				// add my location to the map
				addMyLocation(zonesVector);
				addedMyLocation = true;
			 }
			 
		});
		
	}
}

function toggleFx(element) {
	if($(element).css('display') == 'none') {
		if(element.indexOf('menu') > 0) {
			$('#map').css('top', '376px');
		}else {
			$('#map').css('top', height);
		}
		$(element).gfxFadeIn({
			duration : 500,
			easing : 'ease-in',
		});
		
	} else {
		$('#map').css('top', '45px');
		$(element).gfxFadeOut({
			duration : 100,
			easing : 'ease-out',
		});
	}
}