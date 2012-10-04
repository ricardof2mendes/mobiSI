var map;
var mapProjection = new OpenLayers.Projection("EPSG:900913");
var mapDisplayProjection = new OpenLayers.Projection("EPSG:4326");

var Map = function(licensePlate) {
	
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
		
	// add my location to the map
	addMyLocation();
	
	// process data attached to license plate
	fetchData(licensePlate);
};

/**
 * Determine My location and add it to the map
 * 
 * @param map
 */
function addMyLocation() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(
			function(position) {
				var vectorLayer = new OpenLayers.Layer.Vector("mylocation");
				var feature = new OpenLayers.Feature.Vector(
						new OpenLayers.Geometry.Point(position.coords.longitude, position.coords.latitude).transform(mapDisplayProjection, map.getProjectionObject()), {
									some : 'data'
								}, {
									externalGraphic : '../img/mylocation.png',
									graphicHeight : 25,
									graphicWidth : 21
								});
				vectorLayer.addFeatures(feature);
				map.addLayer(vectorLayer);
				// center the map on the car
				var mapCenter = new OpenLayers.LonLat(position.coords.longitude, position.coords.latitude);
				map.setCenter (mapCenter.transform(mapDisplayProjection, map.getProjectionObject()), 14);	
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
			});
	} else {
		alert("Geolocation is not supported by this browser.");
	}
}

/**
 * Fetch data from server related to given license plate
 * @param licensePlate
 */
function fetchData(licensePlate) {
	var returnData = null;
	$.ajax({
		type : 'GET',
		url : contextPath + '/book/CarDetails.action?ajax=&q=' + licensePlate,
		dataType : 'text',
		cache : false,
		success : function(data) {
			returnData = data;
		},
		error : function(jqXHR, textStatus, errorThrown) {
			// TODO
			alert(xhr.status + ' ' + textStatus + ' ' + errorThrown);
		},
		complete : function(jqXHR, textStatus) {
			if (textStatus == 'success' && returnData != null) {
				processData(eval(returnData));
			}
		}
	});
}

/**
 * Process the received server data
 * @param returnData
 */
function processData(returnData) {
	// car coordinates
	var layer_style = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['default']);
	
	if(returnData.coordinate) {
		// we want opaque and outer line
		layer_style.fillOpacity = 1;
		layer_style.strokeColor = '#000000';
		// create the car vector
		var carlocationVector = new OpenLayers.Layer.Vector("carlocation", {style: layer_style});
		var feature = new OpenLayers.Feature.Vector(
				new OpenLayers.Geometry.Point(returnData.coordinate.longitude, returnData.coordinate.latitude).transform(mapDisplayProjection, map.getProjectionObject()), {
							some : 'data'
								
						});
		carlocationVector.addFeatures(feature);
		map.addLayer(carlocationVector);
		// center the map on the car
		var mapCenter = new OpenLayers.LonLat(returnData.coordinate.longitude, returnData.coordinate.latitude);
		map.setCenter (mapCenter.transform(mapDisplayProjection, map.getProjectionObject()), 14);		
	}
	
	// zones delimiters
	if(returnData.zones && returnData.zones.length > 0) {
		var style_green = OpenLayers.Util.extend({}, layer_style);
		style_green.fillColor = "green";
		var style_yellow = OpenLayers.Util.extend({}, layer_style);
		style_yellow.fillColor = "yellow";
		var style_grey = OpenLayers.Util.extend({}, layer_style);
		style_grey.fillColor = 'light-grey';
		
		var zonesVector = new OpenLayers.Layer.Vector("zones");
		
		$(returnData.zones).each(function() {
			
		});
	}
	
}

/**
 * Add cars to the map
 * 
 * @param map
 *            the car array
 */
function addCars(map, options) {
	
}

function addPolygons(map) {
	
}

$.fn.map.defaults = {


};