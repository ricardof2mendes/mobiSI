/**
 * Define Map class.
 */
var Map = function(configuration) {
	this.construct(configuration);
};

Map.prototype = {
		/**
		 * Image editor constructor.
		 */
		construct : function(configuration) {
			
			this.type = configuration.type;
			this.licensePlate = configuration.licensePlate;
			this.searchParams = configuration.searchParams;
			
			this.mapProjection = new OpenLayers.Projection('EPSG:900913');
			this.mapDisplayProjection = new OpenLayers.Projection('EPSG:4326');

			this.points = [];
			this.mylatlong = {
					point: null,
					latitude: null,
					longitude: null
			};

			this.setStyles();
			
			this.buildCommon();
			
			if(this.type === 'mapACar') {
				this.fetchCarData();
			} else if(this.type === 'mapAZone') {
				this.fetchZonesData();
			} else if(this.type === 'mapASearch') {
				this.fetchSearchCarsData();
			} else  if(this.type === 'mapAStreet') {
				this.fetchStreetData();
			}
		},
		
		setStyles : function() {
			
			// Styling
			this.defaultStyle = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['default']);
			this.defaultStyle.pointRadius = 10;
			this.defaultStyle.graphicOpacity = 1; 
			this.defaultStyle.fillOpacity = 1;
			this.defaultStyle.fillColor = '#ee9900';
			this.defaultStyle.strokeColor = '#000';

			this.selectStyle = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['select']);
			this.selectStyle.pointRadius = 15;
			this.selectStyle.fillOpacity = 1;
			this.selectStyle.fillColor = '#ee9900';
			this.selectStyle.strokeColor = '#000';

			this.greenStyle = OpenLayers.Util.extend({}, this.defaultStyle);
			this.greenStyle.fillOpacity = 0.3;
			this.greenStyle.fillColor = '#ACE228';
			this.greenStyle.strokeColor = '#94C222';

			this.redStyle = OpenLayers.Util.extend({}, this.greenStyle);
			this.redStyle.fillColor = '#FF0F0F';
			this.redStyle.strokeColor = '#FF0004';

			this.yellowStyle = OpenLayers.Util.extend({}, this.sgreenStyle);
			this.yellowStyle.fillColor = '#FFF000';
			this.yellowStyle.strokeColor = '#B7AD00';
			
			this.locationDefautlStyle = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['default']);
			this.locationDefautlStyle.graphicOpacity = 1; 
			this.locationDefautlStyle.externalGraphic = '../img/map/mylocation.png';
			this.locationDefautlStyle.graphicHeight = 25;
			this.locationDefautlStyle.graphicWidth = 21;
			
			this.locationSelectStyle = OpenLayers.Util.extend({}, this.locationDefautlStyle);
			this.locationSelectStyle.graphicHeight = 35;
			this.locationSelectStyle.graphicWidth = 31;
		},
		
		buildCommon : function() {
			var that = this;
			// crete the map, add navigation and zoom controls, add basic layer open street map
			this.map = new OpenLayers.Map({
				div : 'map',
				theme : null,
				controls : [ new OpenLayers.Control.Attribution(),
						new OpenLayers.Control.TouchNavigation({
							dragPanOptions : {
								enableKinetic : true
							}
						}), new OpenLayers.Control.Zoom()],
				layers : [ new OpenLayers.Layer.OSM('OpenStreetMap', null, {
					transitionEffect : 'resize'
				}) ],
		        numZoomLevels: 18,
		        center: new OpenLayers.LonLat(742000, 5861000),
		        zoom: 1
			});
			
			// Add legend control to the map
			this.map.addControl(new OpenLayers.Control.Legend({
				onLegendClick : function(evt) {
			        if (evt.buttonElement === this.legendLink.legend) {
						if($('#menu').css('display') != 'none') {
							toggleFx('#menu', true);
						}
						toggleFx('#legend', true);
			        }
				}
			}));
			
			if(this.type === 'mapASearch') {
				// add list link
				this.map.addControl(new OpenLayers.Control.List({
					onListClick : function(evt) {
				        if (evt.buttonElement === this.listLink.list) {
							window.location.href = contextPath+'/booking/ImmediateBooking.action?searchImmediateInList=&price=' + that.searchParams.price 
								+ '&distance=' + that.searchParams.distance 
								+ '&clazz=' + that.searchParams.clazz
								+ '&fuel=' + that.searchParams.fuel 
								+ '&orderBy=' + that.searchParams.order 
								+ '&latitude=' + that.searchParams.latitude 
								+ '&longitude=' + that.searchParams.longitude;
				        }
					}
				}));
			}
			if(this.type === 'mapASearch' || this.type === 'mapAStreet' ) {
				// add my location link
				this.map.addControl(new OpenLayers.Control.MyLocation({
					onMyLocationClick : function(evt) {
						if (evt.buttonElement === this.myLocationLink.myLocation) {
							if(that.mylatlong.latitude && that.mylatlong.longitude) {
								var mapCenter = new OpenLayers.LonLat(that.mylatlong.longitude, that.mylatlong.latitude );
								that.map.panTo(mapCenter.transform(that.mapDisplayProjection, that.map.getProjectionObject()));
							}
						}
					}
				}));
			}
		},
		
		/**
		 * Fetch data from server related to given license plate
		 * @param licensePlate
		 */
		fetchCarData : function() {
			var that = this;
			$.get(contextPath + '/booking/ImmediateBooking.action?carData=&licensePlate=' + this.licensePlate, 
					function(data, textStatus, jqXHR){
						if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
							var evaluated = eval(data);
							that.processZones(evaluated);
							that.processCar(evaluated);
							that.trackMyLocation();
				        } else {
				            console.log('An error has occurred or the user\'s session has expired!');
				        }
				    });	
		},

		/**
		 * Fetch data from server related to given park
		 * @param licensePlate
		 */
		fetchZonesData : function() {
			var that = this;
			$.get(contextPath + '/booking/AdvanceBooking.action?parkData=&licensePlate=' + this.licensePlate, 
					function(data, textStatus, jqXHR){
						if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
							that.processZones(eval(data));
							that.trackMyLocation();
				        } else {
				            console.log('An error has occurred or the user\'s session has expired!');
				        }
				    });	
		},

		/**
		 * Fetch data from server related to given license plate
		 * @param licensePlate
		 */
		fetchSearchCarsData : function() {
			var that = this;
			$.get(contextPath + '/booking/ImmediateBooking.action?searchCarsData=&price=' + this.searchParams.price 
					+ '&distance=' + this.searchParams.distance 
					+ '&clazz=' + this.searchParams.clazz 
					+ '&fuel=' + this.searchParams.fuel 
					+ '&orderBy=' + this.searchParams.order 
					+ '&latitude=' + this.searchParams.latitude 
					+ '&longitude=' + this.searchParams.longitude, 
					function(data, textStatus, jqXHR){
						if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
							that.processSearch(eval(data));
							that.trackMyLocation();
				        } else {
				            console.log('An error has occurred or the user\'s session has expired!');
				        }
				    });	
		},
		
		/**
		 * Fetch data from server related to given adress TODO refactor
		 * @param licensePlate
		 */
		fetchStreetData : function() {
			if(!this.searchParams.address && this.searchParams.latitude && this.searchParams.longitude) {
				this.mylatlong = {
						point: new OpenLayers.Geometry.Point(this.searchParams.longitude, this.searchParams.latitude).transform(this.mapDisplayProjection, this.map.getProjectionObject()),
						latitude : this.searchParams.latitude, 
						longitude : this.searchParams.longitude
				};
			}
			this.queryData(this.searchParams.address);
		},
		
		queryData : function(query) {
			var that = this;
			if(query.length > 0) {
				$.get(contextPath + '/booking/BookingInterest.action?getAddressFromQuery=&query=' + encodeURI(query), 
						function(data, textStatus, jqXHR){
							if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
								that.processStreet(eval(data));
					        } else {
					            console.log('An error has occurred or the user\'s session has expired!');
					        }
					    });	
			} else {
				that.processStreet();
			}
		},

		/**
		 * Process the received server data
		 * @param returnData
		 */
		processZones : function(returnData) {
			
			// zones delimiters
			if(returnData.zones && returnData.zones.length > 0) {
				var that = this;
				$(returnData.zones).each(function() {
					var zonesVector;
					if(this.category.value == 'NORMAL') {
						zonesVector = new OpenLayers.Layer.Vector('zones', {style: that.greenStyle});
					} else if(this.category.value == 'SPECIAL') {
						zonesVector = new OpenLayers.Layer.Vector('zones', {style: that.greenStyle});
					} else if(this.category.value == 'UNWANTED') {
						zonesVector = new OpenLayers.Layer.Vector('zones', {style: that.yellowStyle});
					} else if(this.category.value == 'FORBIDDEN') {
						zonesVector = new OpenLayers.Layer.Vector('zones', {style: that.grayStyle});
					} else {
						// PARKING
						zonesVector = new OpenLayers.Layer.Vector('zones', {style: that.greenStyle});
					}
					
					 var pointList = [];
					 $(this.polygon.coordenates).each(function() {
						 var newPoint = new OpenLayers.Geometry.Point(this.longitude, this.latitude).transform(that.mapDisplayProjection, that.map.getProjectionObject());
						 pointList.push(newPoint);
						 that.points.push(newPoint);
					 });
					 pointList.push(pointList[0]);
					 var linearRing = new OpenLayers.Geometry.LinearRing(pointList);
					 var polygonFeature = new OpenLayers.Feature.Vector(
							 new OpenLayers.Geometry.Polygon([linearRing])); 
					 zonesVector.addFeatures(polygonFeature);
					 that.map.addLayer(zonesVector);			
				});		
			}
		},
		
		/**
		 * Process the received server data
		 * @param returnData
		 */
		processCar : function(returnData) {
			// car coordinates
			if(returnData.coordinate) {
				// create the car vector
				var objectVector = new OpenLayers.Layer.Vector('objects', {style: this.defaultStyle});
				
				// create point
				var point = 
					new OpenLayers.Geometry.Point(returnData.coordinate.longitude, returnData.coordinate.latitude).transform(this.mapDisplayProjection, this.map.getProjectionObject());
				this.points.push(point);
				
				var feature = new OpenLayers.Feature.Vector(point);
				objectVector.addFeatures(feature);
				this.map.addLayer(objectVector);
			}
		},

		/**
		 * Process search
		 * @param returnData
		 */
		processSearch : function(returnData) {
			if(returnData) {
				// set style
				var style = new OpenLayers.StyleMap({
						'default': this.defaultStyle,
			            'select' : this.selectStyle
		        	});
				
				// create the car vector
				var objectVector = new OpenLayers.Layer.Vector('objects', { styleMap: style });
				this.map.addLayer(objectVector);

				var firstCar = null,
					that = this;
				$(returnData).each(function(){
					var point = 
						new OpenLayers.Geometry.Point(this.longitude, this.latitude).transform(that.mapDisplayProjection, that.map.getProjectionObject());
					that.points.push(point);
					// add feature
					var feature = new OpenLayers.Feature.Vector(point, {car : this}, null);
					objectVector.addFeatures(feature);
					
					if(firstCar === null) {
						firstCar = feature;
					}
				});

		        var selectCtrl = new OpenLayers.Control.SelectFeature(objectVector, {
		        	clickout : false,
		        	onSelect : function(e) {
		        		that.updateWhiteBar({car : e.attributes.car});
		        	}
		        });

		        this.map.addControl(selectCtrl);
		        selectCtrl.activate();
		        
        		selectCtrl.select(firstCar);
			}
		},
		
		/**
		 * Process street search
		 * @param returnData
		 */
		processStreet : function(returnData) {
			
			var selectedFeature = null,
				that = this;
			
			this.points = [];
			
			// set style
			var style = new OpenLayers.StyleMap({
					'default': this.locationDefautlStyle,
		            'select' : this.locationSelectStyle
	        	});
			
			// remove any prior object layer
			var aux = this.map.getLayersByName('objects');
			if(aux.length > 0) {
				this.map.removeLayer(aux[0]);
			}
			// create the objects layer
			var objectVector = new OpenLayers.Layer.Vector('objects', { styleMap: style });
			// add the new layer
			this.map.addLayer(objectVector);
			
			// create the select control
			var selectCtrl = new OpenLayers.Control.SelectFeature(objectVector, {
				clickout : false,
				onSelect : function(e) {
					selectedFeature = e;
					that.drawRadius(e.geometry);
					that.updateWhiteBar({
						street : e.attributes.street
					});
				}
			});

	        this.map.addControl(selectCtrl);
	        selectCtrl.activate();
	        
	    	// create the control
			var geolocate = new OpenLayers.Control.Geolocate({
			    bind: false,
			    geolocationOptions: {
			    	timeout : 10000,
					maximumAge : 60000,
					enableHighAccuracy : true
			    }
			});
			this.map.addControl(geolocate);
			
			// process the return data with streets
			if(returnData) {
				$(returnData).each(function(){
					var point = 
						new OpenLayers.Geometry.Point(this.longitude, this.latitude).transform(that.mapDisplayProjection, that.map.getProjectionObject());
					that.points.push(point);
					// add feature
					var feature = new OpenLayers.Feature.Vector(
									point, {
										location : false,
										street : this
									},
									null);
					objectVector.addFeatures(feature);
					
					if(selectedFeature === null) {
						selectedFeature = feature;
					}
				});
			}
			
			// Let's show user position!
			if(this.mylatlong.point) {
				var feature = new OpenLayers.Feature.Vector(
						this.mylatlong.point, {
									location : true,
									street : {
										displayName : this.searchParams.placeholder,
										latitude : this.mylatlong.latitude,
										longitude : this.mylatlong.longitude
									}
								}, null);
			    objectVector.addFeatures(feature);
			    
			    if(selectedFeature === null) {
			    	selectedFeature = feature;
				}
			}

			// timeout to avoid geolocation override allready selected
			window.setTimeout(function(){
				// add geolocation updates
				geolocate.events.register('locationupdated', geolocate, function(e) {
					// if we have geolocation chage the placeholder input
					$('#query').attr('placeholder', that.searchParams.placeholder);
					
					var point = new OpenLayers.Geometry.Point(e.position.coords.longitude, e.position.coords.latitude).transform(that.mapDisplayProjection, that.map.getProjectionObject());
					
					if(!that.mylatlong.point || that.mylatlong.point.x !== point.x || that.mylatlong.point.y !== point.y) {
						
						that.mylatlong = {
								point: point,
								latitude : e.position.coords.latitude, 
								longitude : e.position.coords.longitude
						};
						
						// remove any geolocation features before creating the new one
						if(objectVector.getFeaturesByAttribute('location', true).length > 0) {
							objectVector.removeFeatures([objectVector.getFeaturesByAttribute('location', true)[0]]);
						}
						
					    var feature = new OpenLayers.Feature.Vector(
										point, {
											location : true,
											street : {
												displayName : that.searchParams.placeholder,
												latitude : that.mylatlong.latitude,
												longitude : that.mylatlong.longitude
											}
										}, null);
					    objectVector.addFeatures(feature);
					    
					    if(!selectedFeature || selectedFeature.attributes.location === true) {
					    	selectCtrl.select(feature);
						}
					    that.processList(returnData);
					}
						
				});
						
	
				geolocate.watch = true;
			    geolocate.activate();
			    
			}, 1000);
			

			// manually select feature
			if(selectedFeature) {
				selectCtrl.select(selectedFeature);
				this.processList(returnData);
			} else { // or clear if none selected
				this.cleanRadius();
				this.processList();
				this.updateWhiteBar({car : null});
				this.map.zoomTo(1);
			}
		},
		
		/**
		 * Process the data by putting it on the list
		 * @param data
		 */
		processList : function(data) {
			var html = '';
			$('#results').html(html);
			var url = $('#linkToBeUsedInList').attr('href'),
				aux = url;
			
			if(this.mylatlong.point) {
				aux += '&latitude=' + this.mylatlong.latitude + '&longitude=' + this.mylatlong.longitude + '&address='; 
				html += '<li><a href="'+aux+'"><div><img src="'+contextPath+'/img/map/mylocation.png"/></div><div class="ellipsis" ><span>'+this.searchParams.placeholder+'</span></div></a></li>';
			}
			if(data) {
				$(data).each(function(){
					aux = url + '&latitude=' + this.latitude + '&longitude=' + this.longitude + '&address=' + this.displayName;
					html += '<li><a href="'+aux+'"><div><img src="'+contextPath+'/img/map/mylocation.png"/></div><div class="ellipsis" ><span>'+ this.displayName +'</span></div></a></li>';
				});
			}
			$('#results').html(html);
		},
		
		/**
		 * Draw radius
		 * @param geometry
		 */
		drawRadius : function(geometry) {
			var bounds = new OpenLayers.Bounds();
			bounds.extend(geometry.getBounds());
			// if radius set circle
			if(this.searchParams && this.searchParams.distance && this.searchParams.distance.length > 0) {
				// remove any previous radius layer
				this.cleanRadius();
				// add new radius
				var circleLayer = new OpenLayers.Layer.Vector('radiusLocation', {style: this.redStyle});
    			this.map.addLayer(circleLayer);
				// radius calculation with spherical mercator scale h=1/cos(lat)
				var radius = this.searchParams.distance/Math.cos(this.mylatlong.latitude * (Math.PI / 180));
				var polygon = new OpenLayers.Geometry.Polygon.createRegularPolygon(geometry, radius, 100, 0);
				var circle = new OpenLayers.Feature.Vector(polygon);
				circleLayer.addFeatures(circle);

				bounds.extend(polygon.getBounds());
			} 
			
			if(this.points.length > 1) {
				$(this.points).each(function(){
					bounds.extend(this.getBounds());
				});
			}
			
			// zoom contents
			bounds.toBBOX();
			this.map.zoomToExtent(bounds, false);
			if(this.points.length > 1) {
				this.map.zoomTo(this.map.zoom-1);
			}
		},
		
		/** Clean the radius from map */
		cleanRadius : function() {
			var aux = this.map.getLayersByName('radiusLocation');
			if(aux && aux.length > 0) {
				this.map.removeLayer(aux[0]);
			}
		},

		/**
		 * Track my location and refresh it in the map
		 */
		trackMyLocation : function() {
			var vectorLayer = new OpenLayers.Layer.Vector('trackedLocation'),
				firstGeolocation = true,
				that = this;
			this.map.addLayer(vectorLayer);
			
			var geolocate = new OpenLayers.Control.Geolocate({
			    bind: false,
			    geolocationOptions: {
			    	timeout : 10000,
					maximumAge : 60000,
					enableHighAccuracy : true
			    }
			});
			this.map.addControl(geolocate);
			
			geolocate.events.register('locationupdated', geolocate, function(e) {
				vectorLayer.removeAllFeatures();
				var point = new OpenLayers.Geometry.Point(e.position.coords.longitude, e.position.coords.latitude).transform(that.mapDisplayProjection, that.map.getProjectionObject());
				
				that.mylatlong = {
						point: point,
						latitude : e.position.coords.latitude, 
						longitude : e.position.coords.longitude
				};
				
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
				if(that.searchParams && that.searchParams.distance && that.searchParams.distance.length > 0) {
					// remove any previous radius layer
					that.cleanRadius();
					var circleLayer = new OpenLayers.Layer.Vector('radiusLocation', {style: that.redStyle});
					that.map.addLayer(circleLayer);
					var radius = that.searchParams.distance/Math.cos(that.mylatlong.latitude * (Math.PI / 180));
					var polygon = new OpenLayers.Geometry.Polygon.createRegularPolygon(point, radius, 100, 0);
					var circle = new OpenLayers.Feature.Vector(polygon);
					circleLayer.addFeatures(circle);
					
					if (firstGeolocation) {
				        firstGeolocation = false;
						
				        var bounds = new OpenLayers.Bounds();
						bounds.extend(polygon.getBounds());
						bounds.toBBOX();
						that.map.zoomToExtent(bounds, false);
					}
					
				} else {
					if (firstGeolocation) {
				        firstGeolocation = false;
				        that.centerAndZoom();
				    }
				}
			});
			geolocate.events.register('locationfailed',this,function() {
			    OpenLayers.Console.log('Location detection failed');
			});

			geolocate.watch = true;
		    geolocate.activate();
		},


		/**
		 * Center in map according to points and zoom to corresponding level
		 */
		centerAndZoom : function(){
		    var bounds = new OpenLayers.Bounds();
		    $(this.points).each(function(){
		    	bounds.extend(this);
		    });
		    if(this.mylatlong.point) {
		    	bounds.extend(this.mylatlong.point);
		    }
		    bounds.toBBOX();
		    this.map.zoomToExtent(bounds, true);
		    this.map.zoomTo(this.map.zoom-1);
		},

		/**
		 * Update white bar with car information
		 */
		updateWhiteBar : function(options) {
			var that = this;
			if(options.car) {
				$('#whiteBar nav').show();
				// Set link
				$('#whiteBar a').on('click', function(e){
						e.preventDefault();
						var href = $(this).attr('href') + '&licensePlate=' + options.car.licensePlate;
						if(that.mylatlong.length > 0) {
							href += '&latitude=' + that.mylatlong.latitude + '&longitude=' + that.mylatlong.longitude;
						} else {
							href += '&latitude=&longitude=';
						}
						window.location.href = href;
					}
				);
				
				// Set image
				$('#whiteBar img').prop('src', contextPath + '/booking/ImmediateBooking.action?getCarImage=&licensePlate=' + options.car.licensePlate);
				// set other content
				$('#licensePlate').html(options.car.licensePlate);
				$('#carBrandName').html(options.car.carBrandName + '&nbsp;' + options.car.carModelName);
				$('#fuelType').html(options.car.formattedFuel);
				$('#priceInUse').html(options.car.formattedPrice);
				$('#distance').html(options.car.formattedDistance);
			} else if(options.street) {
				$('#whiteBar nav').show();
				$('#address').val(options.street.displayName);
				$('#choosenAddress').html(options.street.displayName);
				$('#latitude').html(options.street.latitude);
				$('#longitude').html(options.street.longitude);
			} else {
				$('#whiteBar nav').hide();
			}
		}
};

/**
 * Instance
 */
Map.CURRENT = null;

/**
 * Create instance
 * 
 * @param configuration
 */
Map.create = function(configuration) {
	Map.CURRENT = new Map(configuration);
};

/**
 * Change document
 * 
 * @param documentName
 */
Map.change = function(query) {
	Map.CURRENT.queryData(query);
};
