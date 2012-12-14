// Global variables
var map;
var mapProjection = new OpenLayers.Projection('EPSG:900913');
var mapDisplayProjection = new OpenLayers.Projection('EPSG:4326');

var points = [];

var mylatlong = [];


// Styling
var layerStyle = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['default']);
layerStyle.fillOpacity = 0.2;
layerStyle.graphicOpacity = 1; 

var greenStyle = OpenLayers.Util.extend({}, layerStyle);
greenStyle.fillColor = '#ACE228';
greenStyle.strokeColor = '#94C222';

var redStyle = OpenLayers.Util.extend({}, layerStyle);
redStyle.fillColor = 'transparent';
redStyle.strokeColor = 'red';

var yellowStyle = OpenLayers.Util.extend({}, layerStyle);
yellowStyle.fillColor = '#FFF000';
yellowStyle.strokeColor = '#B7AD00';

var carMarker = OpenLayers.Util.extend({}, layerStyle);
carMarker.fillOpacity = 1;
carMarker.strokeColor = '#000';
carMarker.pointRadius = 10;

var selectStyle = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['select']);
selectStyle.pointRadius = 15;
selectStyle.fillOpacity = 1;
selectStyle.fillColor = '#ee9900';
selectStyle.strokeColor = '#000';

$(document).ready(function() {
	//Menu button
	$('.menuBtn').on('click', function(e) {
		e.preventDefault();
		
		if($('#legend').css('display') != 'none') {
			toggleFx('#legend', true);
		} else {
			toggleFx('div.bottomShadow');
		}
		toggleFx('#menu', true);
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

var MapASearch = function(price, distance, clazz, fuel, orderBy, latitude, logitude) {
	//Build parts
	var attributes = [price, distance, clazz, fuel, orderBy, latitude, logitude];
	buildCommon('list', attributes);
	fetchSearchCarsData(attributes);
};

var button1Clicked = function(){
	alert('a');
};

/**
 * Build common map and controls
 */
function buildCommon(type, attributes) {
	// remove spacing
	//$('div.bottomShadow').css('display', 'none');
	
	// crete the map, add navigation and zoom controls, add basic layer open street map
	map = new OpenLayers.Map({
		div : 'map',
		theme : null,
		controls : [ new OpenLayers.Control.Attribution(),
				new OpenLayers.Control.TouchNavigation({
					dragPanOptions : {
						enableKinetic : true
					}
				}), new OpenLayers.Control.Zoom() ],
		layers : [ new OpenLayers.Layer.OSM('OpenStreetMap', null, {
			transitionEffect : 'resize'
		}) ],
        numZoomLevels: 18
	});
	
	// Create other components hover the map
	if(type == 'list') {
		$('#map > div').append('<div class="myControl top right"><a class="wide" href="'
				+contextPath+'/booking/ImmediateBooking.action?searchImmediateInList=&price=' + attributes[0] + '&distance=' + attributes[1] + 
				'&clazz=' + attributes[2] + '&fuel=' + attributes[3] + '&orderBy=' + attributes[4] + '&latitude=' + attributes[5] + '&longitude=' + attributes[6]+'">List</a></div>');
		$('#map > div').append('<div class="myControl bottom left"><a id="wheremi" href="#"></a></div>');
		$('#wheremi').on('click', function(e){
			e.preventDefault();
			if(mylatlong.length == 2) {
				var mapCenter = new OpenLayers.LonLat(mylatlong[1], mylatlong[0]);
				map.panTo(mapCenter.transform(mapDisplayProjection, map.getProjectionObject()));
			}
		});
	} 
	
	// Legend button
	$('#map > div').append('<div id="lll" class="myControl bottom right"><a id="legBtn" class="italic" href="#">i</a></div>');
	$('#legBtn').on('click', function(e) {
		e.preventDefault();
		
		if($('#menu').css('display') != 'none') {
			toggleFx('#menu', true);
		} else {
			toggleFx('div.bottomShadow');
		}
		toggleFx('#legend', true);
	});
	
//	var button1 = new OpenLayers.Control.Button ({displayClass: 'olControlButton1', trigger: button1Clicked, title: 'Button is to be clicked'});
//	var button2 = new OpenLayers.Control.Button ({div: document.getElementById('lll'), trigger: button1Clicked, title: 'Button is to be clicked'});
//
//	panel = new OpenLayers.Control.Panel({defaultControl: button1});
//	panel.addControls([button1,button2]);
//	map.addControl (panel);
	
	$('#search').on('click', function(e) {
		fetchSearchData($('#query').val());
	});
}

/**
 * Fetch data from server related to given license plate
 * @param licensePlate
 */
function fetchCarData(licensePlate) {
	$.get(contextPath + '/booking/ImmediateBooking.action?carData=&licensePlate=' + licensePlate, 
			function(data, textStatus, jqXHR){
				if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
					var evaluated = eval(data);
					processZones(evaluated);
					processCar(evaluated);
					trackMyLocation();
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
					processZones(eval(data));
					trackMyLocation();
		        } else {
		            console.log('An error has occurred or the user\'s session has expired!');
		        }
		    });	
}

/**
 * Fetch data from server related to given license plate
 * @param licensePlate
 */
function fetchSearchCarsData(attributes) {
	$.get(contextPath + '/booking/ImmediateBooking.action?searchCarsData=&price=' + attributes[0] + '&distance=' + attributes[1] + 
			'&clazz=' + attributes[2] + '&fuel=' + attributes[3] + '&orderBy=' + attributes[4] + '&latitude=' + attributes[5] + '&longitude=' + attributes[6], 
			function(data, textStatus, jqXHR){
				if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
					processSearch(eval(data));
					addMyLocation(distance);
		        } else {
		            console.log('An error has occurred or the user\'s session has expired!');
		        }
		    });	
}

/**
 * Process the received server data
 * @param returnData
 */
function processZones(returnData) {
	
	// zones delimiters
	if(returnData.zones && returnData.zones.length > 0) {
		
		$(returnData.zones).each(function() {
			var zonesVector;
			if(this.category.value == 'NORMAL') {
				zonesVector = new OpenLayers.Layer.Vector('zones', {style: greenStyle});
			} else if(this.category.value == 'SPECIAL') {
				zonesVector = new OpenLayers.Layer.Vector('zones', {style: greenStyle});
			} else if(this.category.value == 'UNWANTED') {
				zonesVector = new OpenLayers.Layer.Vector('zones', {style: yellowStyle});
			} else if(this.category.value == 'FORBIDDEN') {
				zonesVector = new OpenLayers.Layer.Vector('zones', {style: grayStyle});
			} else {
				// PARKING
				zonesVector = new OpenLayers.Layer.Vector('zones', {style: greenStyle});
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
		});		
	}
}
/**
 * Process the received server data
 * @param returnData
 */
function processCar(returnData) {
	// car coordinates
	if(returnData.coordinate) {
		// create the car vector
		var objectVector = new OpenLayers.Layer.Vector('objects', {style: carMarker});
		
		// create point
		var point = 
			new OpenLayers.Geometry.Point(returnData.coordinate.longitude, returnData.coordinate.latitude).transform(mapDisplayProjection, map.getProjectionObject());
		points.push(point);
		
		var feature = new OpenLayers.Feature.Vector(point);
		objectVector.addFeatures(feature);
		map.addLayer(objectVector);
		// center the map on the car
//		var mapCenter = new OpenLayers.LonLat(returnData.coordinate.longitude, returnData.coordinate.latitude);
//		map.setCenter (mapCenter.transform(mapDisplayProjection, map.getProjectionObject()), 14);	
	}
}

/**
 * Process search
 * @param returnData
 */
function processSearch(returnData) {
	if(returnData) {
		// set style
		var style = new OpenLayers.StyleMap({'default': carMarker,
            'select': selectStyle, 'temporary' : selectStyle});
		// create the car vector
		var objectVector = new OpenLayers.Layer.Vector('objects', {styleMap: style});
		map.addLayer(objectVector);

		$(returnData).each(function(){
			var point = 
				new OpenLayers.Geometry.Point(this.longitude, this.latitude).transform(mapDisplayProjection, map.getProjectionObject());
			points.push(point);
			// add feature
			objectVector.addFeatures(new OpenLayers.Feature.Vector(point, {car : this}, null));
		});
		
		
		var report = function(e) {
            OpenLayers.Console.log(e.type, e.feature.id);
        };
		
		
		var highlightCtrl = new OpenLayers.Control.SelectFeature(objectVector, {
            hover: true,
            highlightOnly: true,
            renderIntent: "temporary",
            eventListeners: {
                beforefeaturehighlighted: report,
                featurehighlighted: report,
                featureunhighlighted: report
            }
        });

        var selectCtrl = new OpenLayers.Control.SelectFeature(objectVector,{
        	clickout : true,
        	onSelect : function(e) {
        		updateWhiteBar(e.attributes.car);
        	},
        	onUnselect : function(e) {
        		updateWhiteBar();
        	}
        });

        map.addControl(highlightCtrl);
        map.addControl(selectCtrl);

        highlightCtrl.activate();
        selectCtrl.activate();
	}
}

/**
 * Track my location and refresh it in the map
 */
function trackMyLocation() {
	var vectorLayer = new OpenLayers.Layer.Vector('trackedLocation');
	map.addLayer(vectorLayer);
	
	var geolocate = new OpenLayers.Control.Geolocate({
	    bind: false,
	    geolocationOptions: {
	    	timeout : 10000,
			maximumAge : 60000,
			enableHighAccuracy : true
	    }
	});
	map.addControl(geolocate);
	var firstGeolocation = true;
	geolocate.events.register('locationupdated', geolocate, function(e) {
		vectorLayer.removeAllFeatures();
		var point = new OpenLayers.Geometry.Point(e.position.coords.longitude, e.position.coords.latitude).transform(mapDisplayProjection, map.getProjectionObject());
		points.push(point);
		mylatlong.push(e.position.coords.latitude);
		mylatlong.push(e.position.coords.longitude);
		
	    var feature = new OpenLayers.Feature.Vector(
						point, {
							some : 'data'
						}, {
							externalGraphic : '../img/map/mylocation.png',
							graphicHeight : 25,
							graphicWidth : 21
						});
		vectorLayer.addFeatures(feature);
		
		if (firstGeolocation) {
	        firstGeolocation = false;
	        centerAndZoom();
	    }
	});
	geolocate.events.register('locationfailed',this,function() {
	    OpenLayers.Console.log('Location detection failed');
	});

	geolocate.watch = true;
    geolocate.activate();
}

/**
 * Track my location and refresh it in the map
 */
function addMyLocation(radius) {
	var vectorLayer = new OpenLayers.Layer.Vector('myLocation');
	map.addLayer(vectorLayer);
	
	var geolocate = new OpenLayers.Control.Geolocate({
	    bind: false,
	    geolocationOptions: {
	    	timeout : 10000,
			maximumAge : 60000,
			enableHighAccuracy : true
	    }
	});
	
	map.addControl(geolocate);

	geolocate.events.register('locationupdated', geolocate, function(e) {
		vectorLayer.removeAllFeatures();
		var point = new OpenLayers.Geometry.Point(e.position.coords.longitude, e.position.coords.latitude).transform(mapDisplayProjection, map.getProjectionObject());
		
		points.push(point);
		mylatlong.push(e.position.coords.latitude);
		mylatlong.push(e.position.coords.longitude);
		
	    var feature = new OpenLayers.Feature.Vector(
						point, {
							some : 'data'
						}, {
							externalGraphic : '../img/map/mylocation.png',
							graphicHeight : 25,
							graphicWidth : 21
						});
		vectorLayer.addFeatures(feature);
		
		// if radius set circle and zoom contents
		if(radius && radius.length > 0) {
			var circleLayer = new OpenLayers.Layer.Vector('radiusLocation', {style: redStyle});
			map.addLayer(circleLayer);
			var polygon = new OpenLayers.Geometry.Polygon.createRegularPolygon(point, radius, 50, 0);
			var circle = new OpenLayers.Feature.Vector(polygon);
			circleLayer.addFeatures(circle);
			// create bounds
			var bounds = new OpenLayers.Bounds();
			bounds.extend(polygon.getBounds());
			bounds.toBBOX();
		    map.zoomToExtent(bounds, false);
		    map.zoomTo(map.zoom-1);
			
		} else {
			centerAndZoom();
		}
	});
	geolocate.events.register('locationfailed',this,function() {
	    OpenLayers.Console.log('Location detection failed');
	});

    geolocate.activate();
}


/**
 * Center in map according to points and zoom to corresponding level
 */
function centerAndZoom(){
    var bounds = new OpenLayers.Bounds();
    $(points).each(function(){
    	bounds.extend(this);
    });
    bounds.toBBOX();
    map.zoomToExtent(bounds, false);
    map.zoomTo(map.zoom-1);
}

/**
 * Update white bar with car information
 */
function updateWhiteBar(car) {
	if(car != null) {
		// toggle viewer
		$('#whiteBar ul.hidden').toggle();
		// Set link
		var href = $('#whiteBar a').prop('href') + '&licensePlate=' + car.licensePlate + '&latitude='+mylatlong[0]+'&longitude='+mylatlong[1];
		$('#whiteBar a').prop('href', href);
		// Set image
		var src = contextPath + '/booking/ImmediateBooking.action?getCarImage=&licensePlate=' + car.licensePlate;
		$('#whiteBar img').prop('src', src);
		// set other content
		$('#whiteBar a > div:nth-child(2) > div:first-child > span:first-child').html(car.licensePlate);
		$('#whiteBar a > div:nth-child(2) > div:first-child > span:nth-child(2)').html(car.carBrandName + '&nbsp;' + car.carModelName);
		$('#whiteBar a > div:nth-child(2) > div:first-child > span:nth-child(3)').html(car.formattedFuel);
		
		$('#whiteBar a > div:nth-child(2) > div:nth-child(2) > span:first-child').html(car.formattedPrice);
		$('#whiteBar a > div:nth-child(2) > div:nth-child(2) > span:nth-child(2)').html(car.formattedDistance);
		
	} else {
		$('#whiteBar ul.hidden').toggle();
	}
}

/**
 * Toggle element
 * @param element
 */
function toggleFx(element, animate) {
	if($(element).css('display') == 'none') {
		if(element.indexOf('menu') > 0) {
			$('#container').css('top', '376px');
		}else {
			$('#container').css('top', height);
		}
		if(animate){
			$(element).gfxFadeIn({
				duration : 500,
				easing : 'ease-in',
			});
		} else {
			$(element).css('display', 'block');
		}		
	} else {
		$('#container').css('top', '45px');
		if(animate) {
			$(element).gfxFadeOut({
				duration : 100,
				easing : 'ease-out',
			});
		} else {
			$(element).css('display', 'none');
		}
	}
}