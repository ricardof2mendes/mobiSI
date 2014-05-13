/**
 * Define Map class.
 */
var Map = function(configuration) {
	this.construct(configuration);
};

Map.prototype = {
		/**
		 * Map constructor.
		 */
		construct : function(configuration) {
			
			this.retina = window.devicePixelRatio > 1;
			
			this.type = configuration.type;
			this.licensePlate = configuration.licensePlate;
			this.searchParams = configuration.searchParams;
			
			this.mapProjection = new OpenLayers.Projection('EPSG:900913');
			this.mapDisplayProjection = new OpenLayers.Projection('EPSG:4326');

			this.points = [];
			this.mylatlong = {
					address: null,
					point: null,
					latitude: null,
					longitude: null
			};

			this.setStyles();
			this.buildCommon();
			
			if(this.type === 'mapACar') {
				this.fetchCarData();
			} else if(this.type === 'mapAPark') {
				// MOBICS-1581 removed
				// this.fetchParkData();
                this.fetchCarData();
			} else if(this.type === 'mapAZone') {
				this.fetchZoneData();
			} else if(this.type === 'mapASearch') {
				this.fetchSearchCarsData();
			} else  if(this.type === 'mapAStreet') {
				this.fetchLocationData();
			}
		},
		
		setStyles : function() {
			
			// Styling
			this.greenStyle = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['default']);
			this.greenStyle.fillOpacity = 0.3;
			this.greenStyle.fillColor = '#00FF21';
			this.greenStyle.strokeColor = '#00FF21';
            this.greenStyle.pointRadius = 9;
            this.greenStyle.fillOpacity = 0.4;

			this.redStyle = OpenLayers.Util.extend({}, this.greenStyle);
			this.redStyle.fillColor = '#FF0F0F';
			this.redStyle.strokeColor = '#FF0004';

			this.yellowStyle = OpenLayers.Util.extend({}, this.greenStyle);
			this.yellowStyle.fillColor = '#FFF000';
			this.yellowStyle.strokeColor = '#B7AD00';
			this.yellowStyle.fillOpacity = 0.4;

            this.blueStyle = OpenLayers.Util.extend({}, this.greenStyle);
            this.blueStyle.fillColor = '#4FD3FF';
            this.blueStyle.strokeColor = '#0099FF';
			
			this.ganGanStyle = OpenLayers.Util.extend({}, this.greenStyle);
			this.ganGanStyle.strokeColor = '#008be8';
			this.ganGanStyle.fillOpacity = 0;
			
			this.locationDefautlStyle = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['default']);
            this.locationDefautlStyle.graphicOpacity = 1;
            this.locationDefautlStyle.graphicHeight = 20;
            this.locationDefautlStyle.graphicWidth = 14;
            this.locationDefautlStyle.graphicZIndex = 2;
			
			this.locationSelectStyle = OpenLayers.Util.extend({}, this.locationDefautlStyle);
			this.locationSelectStyle.graphicHeight = 30;
			this.locationSelectStyle.graphicWidth = 22;
            this.locationSelectStyle.graphicZIndex = 1;
			
			this.carDefaultStyle = OpenLayers.Util.extend({}, this.locationDefautlStyle);
			this.carSelectStyle = OpenLayers.Util.extend({}, this.locationSelectStyle);

            this.chargingStationStyle = OpenLayers.Util.extend({}, this.locationDefautlStyle);
            this.chargingStationStyle.graphicHeight = 22;
            this.chargingStationStyle.graphicWidth = 22;
            this.chargingStationStyle.display = 'none';

			if(this.retina) {
				this.locationDefautlStyle.externalGraphic = '../img/map/location-user-unselected@2x.png';
                this.carDefaultStyle.externalGraphic = '../img/map/location-car-unselected@2x.png';
				this.locationSelectStyle.externalGraphic = '../img/map/location-user-selected@2x.png';
				this.carSelectStyle.externalGraphic = '../img/map/location-car-selected@2x.png';
                this.chargingStationStyle.externalGraphic = '../img/map/location-station@2x.png';
			} else {
				this.locationDefautlStyle.externalGraphic = '../img/map/location-user-unselected.png';
				this.carDefaultStyle.externalGraphic = '../img/map/location-car-unselected.png';
				this.locationSelectStyle.externalGraphic = '../img/map/location-user-selected.png';
				this.carSelectStyle.externalGraphic = '../img/map/location-car-selected.png';
                this.chargingStationStyle.externalGraphic = '../img/map/location-station.png';
			}
			
			this.carStyleMap = new OpenLayers.StyleMap({'default':this.carDefaultStyle, 'select': this.carSelectStyle});
			this.locationStyleMap = new OpenLayers.StyleMap({'default': this.locationDefautlStyle, 'select': this.locationSelectStyle});

		},
		
		/** 
		 * Build common stuff 
		 */
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
				center: new OpenLayers.LonLat(-8, 39.7),
				zoom: 7
			});
			
			// Add legend control to the map
			this.map.addControl(new OpenLayers.Control.Legend({
				onLegendClick : function(evt) {
					if(document.getElementById('submit')) {
						document.getElementById('submit').focus(); 
					} 
					if (evt.buttonElement === this.legendLink.legend) {
						if(!$('#menu').hasClass('hidden')) {
							toggleFx('#menu');
						}
						toggleFx('#legend');
					}
				}
			}));
			
			if(this.type === 'mapASearch') {
				// add list link
				this.map.addControl(new OpenLayers.Control.List({
					onListClick : function(evt) {
						if(document.getElementById('submit')) {
							document.getElementById('submit').focus();
						}
						if (evt.buttonElement === this.listLink.list) {
							window.location.href = CONTEXT_PATH +'/booking/ImmediateBooking.action?searchImmediateInList=&price=' + that.searchParams.price +
							'&distance=' + that.searchParams.distance + '&clazz=' + that.searchParams.clazz + '&fuel=' + that.searchParams.fuel +
							'&orderBy=' + that.searchParams.order + '&latitude=' + that.searchParams.latitude + '&longitude=' + that.searchParams.longitude;
						}
					}
				}));
			}

            // add Stations link
            this.map.addControl(new OpenLayers.Control.Stations({
                onStationsClick : function(evt) {
                    if(document.getElementById('submit')) {
                        document.getElementById('submit').focus();
                    }
                    if (evt.buttonElement === this.stationsLink.stations) {
                        if(that.stations) {
                            that.toggleStations();
                        } else {
                            that.fetchStationsData();
                        }
                    }
                }
            }));

			if(this.type === 'mapASearch' || this.type === 'mapAStreet' ) {
				// add my location link
				this.map.addControl(new OpenLayers.Control.MyLocation({
					onMyLocationClick : function(evt) {
						if(document.getElementById('submit')) {
							document.getElementById('submit').focus(); 
						}
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
			$.get(CONTEXT_PATH + '/booking/ImmediateBooking.action?carData=&licensePlate=' + this.licensePlate,
					function(data, textStatus, jqXHR){
						if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
							var evaluated = eval(data);
							that.processZones(evaluated);
							that.processCar(evaluated);
							that.trackMyLocation();
							that.centerAndZoom();
						} else {
				        	console.log('An error has occurred or the user\'s session has expired!');
				        	$('html').html(data);
				        }
				    });	
		},

		/**
		 * Fetch data from server related to given park
		 * @param licensePlate
		 */
		fetchParkData : function() {
			var that = this;
			$.get(CONTEXT_PATH + '/booking/AdvanceBooking.action?carData=&licensePlate=' + this.licensePlate,
					function(data, textStatus, jqXHR){
						if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
							var evaluated = eval(data);
							that.processZones(evaluated);
							that.trackMyLocation();
							that.centerAndZoom();
				        } else {
				            console.log('An error has occurred or the user\'s session has expired!');
				            $('html').html(data);
				        }
				    });	
		},

        /**
         * Fetch data from server related to given zone
         * @param licensePlate
         */
        fetchZoneData : function() {
            var that = this;
            $.get(CONTEXT_PATH + '/addons/AddOns.action?zoneData=&code=' + this.licensePlate,
                function(data, textStatus, jqXHR){
                    if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
                        var evaluated = eval(data);
                        that.processZone(evaluated);
                        that.trackMyLocation();
                        that.centerAndZoom();
                    } else {
                        console.log('An error has occurred or the user\'s session has expired!');
                        $('html').html(data);
                    }
                });
        },

		/**
		 * Fetch data from server related to given license plate
		 * @param licensePlate
		 */
		fetchSearchCarsData : function() {
			var that = this;
			$.get(CONTEXT_PATH + '/booking/ImmediateBooking.action?searchCarsData=&price=' + this.searchParams.price 
					+ '&distance=' + this.searchParams.distance 
					+ '&clazz=' + this.searchParams.clazz 
					+ '&fuel=' + this.searchParams.fuel 
					+ '&orderBy=' + this.searchParams.order 
					+ '&latitude=' + this.searchParams.latitude 
					+ '&longitude=' + this.searchParams.longitude, 
					function(data, textStatus, jqXHR){
						if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
							that.processCars(eval(data));
							that.trackMyLocation();
							that.centerAndZoom();
				        } else {
				            console.log('An error has occurred or the user\'s session has expired!');
				            $('html').html(data);
				        }
				    });	
		},
        /**
		 * Fetch data from server related to stations
		 */
		fetchStationsData : function() {
			var that = this;
			$.get(CONTEXT_PATH + '/booking/ImmediateBooking.action?chargingStationsData=',
					function(data, textStatus, jqXHR){
						if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
                            that.processStations(eval(data));
				        } else {
				            console.log('An error has occurred or the user\'s session has expired!');
				            $('html').html(data);
				        }
				    });
		},
		
		/**
		 * Fetch data from server related to given adress TODO refactor
		 * @param licensePlate
		 */
		fetchLocationData : function(query) {
			var that = this;
			if(query) {
				$.get(CONTEXT_PATH + '/booking/BookingInterest.action?getAddressFromQuery=&query=' + encodeURI(query), 
						function(data, textStatus, jqXHR){
							if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
								that.processLocation({results : eval(data).length > 0, data : eval(data)});
					        } else {
					            console.log('An error has occurred or the user\'s session has expired!');
					            $('html').html(data);
					        }
					    });	
			} else {
				var returnData = {results : true, data :[]};
				
				if(this.searchParams.latitude && this.searchParams.longitude) {
					var isCurrent = this.searchParams.address !== null && this.searchParams.address.length === 0;
					if(isCurrent) {
						this.mylatlong = {
								address: this.searchParams.placeholder, 
								point: new OpenLayers.Geometry.Point(this.searchParams.longitude, this.searchParams.latitude).transform(this.mapDisplayProjection, this.map.getProjectionObject()),
								latitude : this.searchParams.latitude, 
								longitude : this.searchParams.longitude
						};
					} else {
						returnData = {
								results : true, 
								data : [ {
									displayName: this.searchParams.address,
									longitude:this.searchParams.longitude,
									latitude: this.searchParams.latitude
									}]
						};
					}
				}
				that.processLocation(returnData);
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
						 //that.points.push(newPoint);
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
        processZone : function(returnData) {
            var zonesVector = new OpenLayers.Layer.Vector('zones', {style: this.blueStyle});
            var pointList = [];
            var that = this;
            $(returnData.polygon.coordenates).each(function() {
                var newPoint = new OpenLayers.Geometry.Point(this.longitude, this.latitude).transform(that.mapDisplayProjection, that.map.getProjectionObject());
                pointList.push(newPoint);
                //that.points.push(newPoint);
            });
            pointList.push(pointList[0]);
            var linearRing = new OpenLayers.Geometry.LinearRing(pointList);
            var polygonFeature = new OpenLayers.Feature.Vector(
                new OpenLayers.Geometry.Polygon([linearRing]));
            zonesVector.addFeatures(polygonFeature);
            this.map.addLayer(zonesVector);
        },
		
		/**
		 * Process the received server data
		 * @param returnData
		 */
		processCar : function(returnData) {
			// car coordinates
			if(returnData.coordinate) {
				// create the car vector
				
				var objectVector = new OpenLayers.Layer.Vector('objects', { style: this.carSelectStyle, rendererOptions: {zIndexing: true} });
				
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
		 * Display the search for cars given criterias
		 * @param returnData
		 */
		processCars : function(returnData) {
			if(returnData) {
				
				// init vars
                var firstCar = null,
                    that = this,
				    objectVector = new OpenLayers.Layer.Vector('objects', { styleMap: this.carStyleMap, rendererOptions: {zIndexing: true} });
				this.map.addLayer(objectVector);

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
			} else {
				$('#noResults').show();
				$('body').addClass('confirmation');
				$('body').on('touchmove', 'body', function(e){
					e.preventDefault();
				});
			}
		},
		
		/**
		 * Process location search
		 * @param returnData
		 */
		processLocation : function(returnData) {
			
			var selectedFeature = null,
				that = this;
			
			this.points = [];
			
			// remove any prior object layer
			var aux = this.map.getLayersByName('objects');
			if(aux.length > 0) {
				this.map.removeLayer(aux[0]);
			}
			// create the objects layer
			var objectVector = new OpenLayers.Layer.Vector('objects', { styleMap: this.locationStyleMap, rendererOptions: {zIndexing: true}});
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
			if(returnData.results === true) {
				$(returnData.data).each(function(){
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
			} else {
				$('#noResults').show();
				$('body').addClass('confirmation');
				$('body').on('touchmove', 'body', function(e){
					e.preventDefault();
				});
			}
			
			// User selected location...
			if(this.mylatlong.latitude && this.mylatlong.longitude) {

				var feature = new OpenLayers.Feature.Vector(
						this.mylatlong.point, {
									location : true,
									street : {
										displayName : this.mylatlong.address,
										latitude : this.mylatlong.latitude,
										longitude : this.mylatlong.longitude
									}
								}, null);
				objectVector.addFeatures(feature);

				if(selectedFeature === null) {
					selectedFeature = feature;
				}
			}


			// add geolocation updates
			geolocate.events.register('locationupdated', geolocate, function(e) {
				// if we have geolocation chage the placeholder input
				$('#query').attr('placeholder', that.searchParams.placeholder);
				
				var point = new OpenLayers.Geometry.Point(e.position.coords.longitude, e.position.coords.latitude).transform(that.mapDisplayProjection, that.map.getProjectionObject());
				
				if(!that.mylatlong.point || that.mylatlong.point.x !== point.x || that.mylatlong.point.y !== point.y) {
					
					that.mylatlong = {
							address: that.searchParams.placeholder,
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
											displayName : that.mylatlong.address,
											latitude : that.mylatlong.latitude,
											longitude : that.mylatlong.longitude
										}
									}, null);
					objectVector.addFeatures(feature);
					
					if(!selectedFeature || selectedFeature.attributes.location === true) {
						selectCtrl.select(feature);
					}
					that.processList(returnData.data);
				}						
			});	
			geolocate.watch = true;
			geolocate.activate();


			// manually select feature
			if(selectedFeature) {
				selectCtrl.select(selectedFeature);
				this.processList(returnData.data);
				this.centerAndZoom();
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
			
			var icon = this.retina ? 'location-user-selected@2x.png' : 'location-user-selected.png';
			
			if(this.mylatlong.latitude) {
				aux += '&latitude=' + this.mylatlong.latitude + '&longitude=' + this.mylatlong.longitude + '&address='; 
				html += '<li><a href="'+aux+'"><div><img class="userLocation" src="'+ CONTEXT_PATH +'/img/map/'+icon+'"/></div><div class="ellipsis" ><span>'+this.searchParams.placeholder+'</span></div></a></li>';
			}
			if(data) {
				$(data).each(function(){
					aux = url + '&latitude=' + this.latitude + '&longitude=' + this.longitude + '&address=' + this.displayName;
					html += '<li><a href="'+aux+'"><div><img class="userLocation" src="'+ CONTEXT_PATH +'/img/map/'+icon+'"/></div><div class="ellipsis" ><span>'+ this.displayName +'</span></div></a></li>';
				});
			}
			$('#results').html(html);
		},

        /**
         * Process fetched location
         */
        processStations : function(returnData){
            // init vars
            this.stations = new OpenLayers.Layer.Vector('stations', {style: this.chargingStationStyle, rendererOptions: {zIndexing: true} });
            this.map.addLayer(this.stations);

            var that = this;
            $(returnData).each(function(){
                var point =
                    new OpenLayers.Geometry.Point(this.coordinates.longitude, this.coordinates.latitude).transform(that.mapDisplayProjection, that.map.getProjectionObject());
                // add feature
                var feature = new OpenLayers.Feature.Vector(point, {station : this}, null);
                that.stations.addFeatures(feature);

            });

            var selectCtrl = new OpenLayers.Control.SelectFeature(this.stations, {
                clickout : false,
                onSelect : function(e) {
                    window.location.href = CONTEXT_PATH + '/booking/ImmediateBooking.action?chargingStation=&id='+ e.attributes.station.id;
                }
            });

            this.map.addControl(selectCtrl);
            selectCtrl.activate();
            this.toggleStations();
        },

        toggleStations : function() {
            var features = this.stations.features;
            for (var i = 0; i < features.length; i++) {
                var feature = features[i];
                if (feature.style.display === 'none') {
                    feature.style.display = '';
                } else {
                    feature.style.display = 'none';
                }
            }
            this.stations.redraw();
        },
		
		/**
		 * Draw radius
		 * @param geometry
		 */
		drawRadius : function(geometry) {
			var that = this;
			// if radius set circle
			if(this.searchParams && this.searchParams.distance && this.searchParams.distance.length > 0 && this.searchParams.distance < ANY_DISTANCE) {
				// remove any previous radius layer
				this.cleanRadius();
				// add new radius
				var circleLayer = new OpenLayers.Layer.Vector('radiusLocation', {style: this.ganGanStyle});
    			this.map.addLayer(circleLayer);
				// radius calculation with spherical mercator scale h=1/cos(lat)
				var radius = this.searchParams.distance/Math.cos(this.mylatlong.latitude * (Math.PI / 180));
				var polygon = new OpenLayers.Geometry.Polygon.createRegularPolygon(geometry, radius, 100, 0);
				var circle = new OpenLayers.Feature.Vector(polygon);
				circleLayer.addFeatures(circle);
				
				$(polygon.getVertices()).each(function() {
					that.points.push(this);
				});
			} 
			
		},
		
		/** Clean the radius from map */
		cleanRadius : function() {
			var aux = this.map.getLayersByName('radiusLocation');
			if(aux && aux.length > 0) {
				this.map.removeLayer(aux[0]);
			}
		},
		
		/** Clean the map*/
		clearMap : function() {
			// remove any object layer
			var aux = this.map.getLayersByName('objects');
			if(aux.length > 0) {
				this.map.removeLayer(aux[0]);
			}
			// remove any previous radius layer
			this.cleanRadius();
			// process my location if any
			this.processLocation({results:true, data:[]});
		},

		/**
		 * Track my location and refresh it in the map
		 */
		trackMyLocation : function() {
			var vectorLayer = new OpenLayers.Layer.Vector('trackedLocation'),
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
								}, that.locationSelectStyle);
				vectorLayer.addFeatures(feature);
				
				// if radius set circle and zoom contents
				that.drawRadius(feature.geometry);
				that.centerAndZoom();
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
		    this.map.zoomToExtent(bounds, false);
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
				$('#whiteBar img').prop('src', CONTEXT_PATH + '/booking/ImmediateBooking.action?getCarImage=&width=58&height=58&licensePlate=' + options.car.licensePlate);
                $('#whiteBar img').on('error', function() {
                        var noPhoto = window.devicePixelRatio > 1 ? 'ios-webapp-no-photo@2x.png' : 'ios-webapp-no-photo.png';
                        $(this).attr('src', CONTEXT_PATH + '/img/' + noPhoto);
                    });
				// set other content
				$('#licensePlate').html(options.car.licensePlate);
				$('#carBrandName').html(options.car.carBrandName + '&nbsp;' + options.car.carModelName);
				$('#fuelType').html(options.car.formattedFuel);
				$('#priceInUse').html(options.car.formattedPrice);
				$('#distance').html(options.car.formattedDistance);
                if(options.car.promotions) {
                    $('#promo').addClass('showPromotion');
                } else {
                    $('#promo').removeClass('showPromotion');
                }
                if(options.car.addOns) {
                    $('#add').addClass('showAddOn');
                } else {
                    $('#add').removeClass('showAddOn');
                }

			} else if(options.street) {
				$('#whiteBar nav').show();
				$('#choosenAddress').html(options.street.displayName);
				$('#address').val(options.street.displayName);
				$('#latitude').val(options.street.latitude);
				$('#longitude').val(options.street.longitude);
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
	Map.CURRENT.fetchLocationData(query);
};

/**
 * Change document
 * 
 * @param documentName
 */
Map.clear = function(query) {
	Map.CURRENT.clearMap();
};
