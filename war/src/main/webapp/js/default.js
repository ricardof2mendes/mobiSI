/*
 * Default Script
 */
$(document).ready(function() {
	'use strict';
	
	// preventCacheOnIos();

	/** 
	 * Menu open/close 
	 */
	$('.menuBtn').on('click', function(e) {
		e.preventDefault();
		toggleMenuFx();
	});

	/** 
	 * Nearest car location 
	 */
	$('#nearestCar').on('click', function(e) {
		e.preventDefault();
		var element = $(this); 
		fillGeoposition(function(position) {
							var url = element.prop('href') +
									'&latitude=' + position.coords.latitude +
									'&longitude=' + position.coords.longitude;
							window.location.href = url;
						}, function(err) {
							treatGeolocationError(err);
						}, true);
	});
	
	/** Find me a car for later */
	$('#findForLater').on('click', function(e) {
		e.preventDefault();
		var element = $(this); 
		fillGeoposition(function(position) {
							var url = element.prop('href')
									+ '&latitude=' + position.coords.latitude
									+ '&longitude=' + position.coords.longitude;
							window.location.href = url;
						}, function() {
							window.location.href = element.prop('href') + '&latitude=&longitude=';
						} ,true);
	});
	
    /**
     * Booking Interest
     */
    
	/** Search location link */
	$('#locationLink').on('click', function(e) {
		e.preventDefault();
		var element = $(this);
		var url = element.prop('href');
		url += '&' + $('#latitude').prop('name') + '=' + $('#latitude').val(); 
		url += '&' + $('#longitude').prop('name') + '=' + $('#longitude').val(); 
		url += '&' + $('#startDate').prop('name') + '=' + $('#startDate').val(); 
		url += '&' + $('#distance').prop('name') + '=' + $('#distance').val(); 
		url += '&' + $('#carClazz').prop('name') + '=' + $('#carClazz').val(); 
		url += '&' + $('#fromMyCarClub').prop('name') + '=' + $('#fromMyCarClub').val(); 
		url += '&' + $('#startSending').prop('name') + '=' + $('#startSending').val(); 
		url += '&' + $('#stopSending').prop('name') + '=' + $('#stopSending').val(); 
		url += '&' + $('#maxMessages').prop('name') + '=' + $('#maxMessages').val();
		if($('#edit').length > 0) {
			url += '&edit=true';
			url += '&' + $('#activityCode').prop('name') + '=' + $('#activityCode').val();
		}
		
		window.location.href = url;
	});
	
	/**
	 * Prevent license plate input form to submit
	 */ 
	$('#licensePlateBookForm').on('submit', function(e){
		e.preventDefault();
	});

	/**
	 * License plate autocomplete
	 */
	$('#licensePlate').focus();
	
 	$('#licensePlate').on('keyup', function(e) {
 		var that = this;
 		// put cross to delete input content
 		if($(this).val().length >= 1){
 			$(this).css('text-transform', 'uppercase');
 			$('#licensePlateBookForm > div > div').addClass('delete')
 				.on('click', function() {
 					$(that).val('');
 					$('#articleContainer').html('').css('display', 'none');
 					$('#licensePlateBookForm > div > div').removeClass('delete').off('click');
 					$(that).css('text-transform', 'none');
 			});
 		} else {
 			$('#licensePlateBookForm > div > div').removeClass('delete').off('click');
 			$(this).css('text-transform', 'none');
 		}
 		
 		// add the autocomplete
		if($(this).val().length >= 2) {
			// first clear the delay
			clearTimeout(this.timeout);
			// then create the delay call
			this.timeout = setTimeout(function(){
				$('div.delete').toggle();
				$(that).addClass('autocomplete');
	 			if($('#latitude').val().length == 0 && $('#longitude').val().length == 0){
		 			fillGeoposition(function(position) {
				 				$('#latitude').val(position.coords.latitude);
				 				$('#longitude').val(position.coords.longitude);
				 				autocompleteList();
		 					}, function() {
		 						autocompleteList();
							});
	 			} else {
	 				autocompleteList();
	 			}
			}, 1000);
			
 		} else {
 			$('#articleContainer').html('').css('display', 'none');
 		}
 	});
 	
 	/**
 	 * Search for cars 
 	 */
 	$('#searchCarsForBook').on('click', function(e){
 		e.preventDefault();
 		fillGeoposition(function(position) {
							$('#latitude').val(position.coords.latitude);
							$('#longitude').val(position.coords.longitude);
							$('#searchForm').submit();
						}, function(err) {
							treatGeolocationError(err);
						}, true);
 	});
 	
 	/**
 	 * Select autocomplete
 	 */
 	if($('#location').length > 0){
 		autocompleteZones();
 		
 		$('#location').on('change', function(){
 	 		autocompleteZones();
 	 	});
 	}
 	
 	/**
 	 *  Calendar setup
 	 */
 	if($('#startDate').length > 0){
 		var now = new Date();
	    $('#startDate').mobiscroll().datetime({
	    	setText: 'OK',
	    	dateFormat: DATE_PATTERN,
	    	dateOrder: 'ddmmyy',
	    	timeFormat: TIME_PATTERN,
	    	timeWheels: 'HHii',
	        minDate: new Date(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours(), now.getMinutes(), now.getSeconds(), now.getMilliseconds()),
	        display: 'modal',
	        mode: 'scroller',
	        width: 42
	    });    
 	}
 	
 	if($('#endDate').length > 0){
	 	var now = new Date();
	    $('#endDate').mobiscroll().datetime({
	    	setText: 'OK',
	    	dateFormat: DATE_PATTERN,
	    	dateOrder: 'ddmmyy',
	    	timeFormat: TIME_PATTERN,
	    	timeWheels: 'HHii',
	        minDate: new Date(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours()+1, now.getMinutes(), now.getSeconds(), now.getMilliseconds()),
	        display: 'modal',
	        mode: 'scroller',
	        width: 42
	    });    
 	}
 	
 	if($('#reportDamageDate').length > 0){
	 	var now = new Date();
	    $('#reportDamageDate').mobiscroll().datetime({
	    	setText: 'OK',
	    	dateFormat: DATE_PATTERN,
	    	dateOrder: 'ddmmyy',
	    	timeFormat: TIME_PATTERN,
	    	timeWheels: 'HHii',
	        maxDate: now,
	        display: 'modal',
	        mode: 'scroller',
	        width: 42
	    });    
 	}
 	
 	if($('#limited').length > 0){
 		console.log($('#limited').attr('data-begin'));
 		console.log($('#limited').attr('data-limit'));
 		
	 	var begin = new Date($('#limited').attr('data-begin')),
	 		limit = new Date($('#limited').attr('data-limit'));
	 	console.log(begin);
	 	console.log(limit);
	    var mobiscroll = $('#limited').mobiscroll().datetime({
	    	setText: 'OK',
	    	dateFormat: DATE_PATTERN,
	    	dateOrder: 'ddmmyy',
	    	timeFormat: TIME_PATTERN,
	    	timeWheels: 'HHii',
	    	minDate: begin,
	        display: 'modal',
	        mode: 'scroller',
	        width: 42
	    });    
	    
	    if(limit.length > 0) {
	    	console.log(limit);
	    	mobiscroll.maxDate = limit;
	    }
 	}
 	
 	/* Modal dialog windows */
 	$('#openConfirm').on('click', function(e) {
 		e.preventDefault();
 		$('body').addClass('confirmation');
 		
 		$('body').on('touchmove', 'body', function(e){
 			e.preventDefault();
 		});
 		
// 		var windowHeight = document.documentElement.clientHeight;
// 		var elementHeight = $('.confirm article').height();
// 		$('.confirm article').css({
// 		    'margin-top': (windowHeight-elementHeight)/2 + 'px'
// 		});
 		
 	});
 	
 	$('#closeConfirm').on('click', function(e) {
 		e.preventDefault();
 		$('body').removeClass('confirmation');
 		$('body').off('scroll');
 	});
 	
 	/* report problem license plate search */
 	$('#licenseReport').on('blur', function(){
 		var that = this;
 		
		if(	($('#carLicensePlate').length == 0 ||
			($('#carLicensePlate').length > 0 && $('#carLicensePlate').val() !=  $(this).val()))){
		
			$(this).addClass('autocomplete');
	 		var url = CONTEXT_PATH+'/contacts/ContactsAndDamageReport.action?licensePlateSearch=&licensePlate='+$(this).val();
	
			$.get(url, function(data, textStatus, jqXHR){
				if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
						$(that).removeClass('autocomplete');
						$('#searchResults').html(data);
					    $('#searchResults').css('display', 'block');
				} else {
					console.log('An error has occurred or the user\'s session has expired!');
					$('html').html(data);
				}
			}, function(data, textStatus, jqXHR) {
				$('html').html(data.responseText);
			});
		}
 	});
 	
 	$('#licenseReport').on('keyup', function(){
 		if($(this).val().length >= 1){
 			$(this).css('text-transform', 'uppercase');
 		} else {
 			$(this).css('text-transform', 'none');
 			$('#searchResults').css('display', 'none');
 			$('#searchResults').html('');
 		}
 	});
 	
 	/* Messages */
 	$('.messageBook').on('click', function(e) {
		e.preventDefault();
		var element = $(this); 
		fillGeoposition(function(position) {
							var url = element.prop('href')
									+ '&latitude=' + position.coords.latitude
									+ '&longitude=' + position.coords.longitude;
							window.location.href = url;
						}, function() {
							window.location.href = element.prop('href')+ '&latitude=&longitude=';
						}, true);
	});
 	
 	/* Preferences*/
 	$('#sort0').on('change', function(e) {
 		if($('#sort1').val() == $(this).val()) {
 			toogleSort('#sort1', '#sort2', this);
 		}else {
 			toogleSort('#sort2', '#sort1', this);
 		}
 	});
 	$('#sort1').on('change', function(e) {
 		if($('#sort0').val() == $(this).val()) {
 			toogleSort('#sort0', '#sort2', this);
 		}else {
 			toogleSort('#sort2', '#sort0', this);
 		}
 	});
 	$('#sort2').on('change', function(e) {
 		if($('#sort0').val() == $(this).val()) {
 			toogleSort('#sort0', '#sort1', this);
 		}else {
 			toogleSort('#sort1', '#sort0', this);
 		}		
 	});
 	// click on li to change checkbox
 	$('#appFilter').on('click', function(){
 		$('#app').trigger('click');
 	});
 	// click on li to change checkbox
 	$('#smsFilter').on('click', function(){
 		$('#sms').trigger('click');
 	});
 	// click on checkbox will toogle select option
 	$('#app').on('click', function(e){
 		e.stopPropagation();
 		if(this.appOpt == null) {
 			this.appOpt = $('#appOption'); 
 			this.appOpt.removeAttr('selected');
 		}
 		if($('#app').prop('checked')) {
 			$('#emailOption').after(this.appOpt);
 		} else {
 			this.appOpt.remove();
 		}
 	});
 	// click on checkbox will toogle select option
 	$('#sms').on('click', function(e){
 		e.stopPropagation();
 		if(this.smsOpt == null) {
 			this.smsOpt = $('#smsOption');
 			this.smsOpt.removeAttr('selected');
 		}
 		if($('#sms').prop('checked')) {
 			if($('#appOption').length > 0) {
 				$('#appOption').after(this.smsOpt);
 			} else {
 				$('#emailOption').after(this.smsOpt);
 			}
 		} else {
 			this.smsOpt.remove();
 		}
 	});
 	
 	// Current trip state pooling
 	if($('#statePooling').length > 0) {
 		// check the state
 		var data = $('#state').text();
 		var WAITING = 'WAIT_OBS_';
 		var ERROR = 'IN_ERROR';
 		
 		var url = {state : CONTEXT_PATH, 
 				   end : CONTEXT_PATH};
 		
 		if($('#statePooling').hasClass('currentTrip')) {
 			url.state += '/trip/Trip.action?getState=';
 			url.end += '/trip/Trip.action?showMessage=';
 		} else {
 			url.state += '/recent/RecentActivities.action?getState=&activityCode=' + $('#activityCode').text();
 			url.end += '/recent/RecentActivities.action?showMessage=&activityCode=' + $('#activityCode').text();
 		}
 		
 		if(data.substring(0, WAITING.length) === WAITING) {
 			setInterval(function(){
 				var that = this;
 				$.get(url.state, 
 						function(data, textStatus, jqXHR){
 							if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
 								var evaluated = eval(data);
 								if(evaluated === ERROR) {
 									clearInterval(that); 
 									$('body').addClass('confirmation');
 									$('body').on('touchmove', 'body', function(e){
 										e.preventDefault();
 									});
 						 		} else if(evaluated.substring(0, WAITING.length) !== WAITING){
 						 			window.location.href = url.end;
 						 		}
 					        } else {
 					            console.log('An error has occurred or the user\'s session has expired!');
 					        }
 					    });	
 			}, 3000);
 		} else if(data === ERROR) {
 			$('body').addClass('confirmation');
			$('body').on('touchmove', 'body', function(e){
				e.preventDefault();
			});
 		}
 	}
 	
 	// On click booking error confirm box button
 	$('#closeBookingImmediate').on('click', function(e) {
 		e.preventDefault();
 		window.location.href = CONTEXT_PATH + '/trip/Trip.action?endTrip=';
 	});
 	
 	$('#closeBookingAdvance').on('click', function(e) {
 		e.preventDefault();
 		window.location.href = CONTEXT_PATH + '/recent/RecentActivities.action?cancelAdvanceBooking=&activityCode='+$('#activityCode').text();
 	});
 	
 	$('#endTrip').on('click', function(e) {
 		if($("#state").text() === 'UNWANTED') {
 			e.preventDefault();
 			$('body').addClass('confirmation');
			$('body').on('touchmove', 'body', function(e){
				e.preventDefault();
			});
				
 		} 		
 	});
 	
});


/**
 * Obtain geopostion
 * 
 * @param callback callback function
 * @param isLoading show loading screen
 */
function fillGeoposition(callback, error, isLoading) {
	if (navigator.geolocation) {
		if(isLoading){
			$('body').addClass('loading');
		}
		navigator.geolocation.getCurrentPosition(callback, error, {
			timeout : 10000,
			maximumAge : 60000,
			enableHighAccuracy : true
		});
	} else {
		alert('Geolocation is not supported by this browser.');
	}
}

/**
 * Get car list by ajax
 */
function autocompleteList() {
	
	var url = CONTEXT_PATH+'/booking/ImmediateBooking.action?licensePlateAutocomplete=&licensePlate=' + $('#licensePlate').val()
					+'&latitude='+$('#latitude').val()
					+'&longitude='+$('#longitude').val();
	
	$.get(url, function(data, textStatus, jqXHR){
		if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
        	$('#articleContainer').html(data);
            $('#articleContainer').css('display', 'block');
            $('#licensePlate').removeClass('autocomplete');
            $('div.delete').toggle();
        } else {
            console.log('An error has occurred or the user\'s session has expired!');
            $('html').html(data);
        }
    }, function(data, textStatus, jqXHR) {
        $('html').html(data.responseText);
    });
}

/**
 * Get park list by ajax
 */
function autocompleteZones() {
	var url = CONTEXT_PATH +'/booking/AdvanceBooking.action?getZones=&location=' + $('#location').val();
	$.get(url, function(data, textStatus, jqXHR){
		if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
            	data = eval(data);
            	if (data == null || data.length != 1) {
            		$('#zone').html('');
                    $('#zone').append('<option value="">' + ZONE_ALL + '</option>');
                }
                if (data != null) {
                    $(data).each(function(e) {
                        if (this.zone != null)
                            $('#zone').append('<option value="' + this.zone + '">' + this.zone + '</option>');
                    });
                }
        } else {
            console.log('An error has occurred or the user\'s session has expired!');
            $('html').html(data);
        }
    }, function(data, textStatus, jqXHR) {
    		$('html').html(data.responseText);
	   });
}

/**
 * Treat geolocation error
 * 
 * @param err the error message
 */
function treatGeolocationError(err) {
	var value;
	if (err.code == 1) {
		value=2;
	} else {
		value=1;
	}
	$('body').removeClass('loading');
	$('h3#title'+value).hide();
	$('body').addClass('confirmation');
	$('body').on('touchmove', 'body', function(e){
		e.preventDefault();
	});
}

/**
 * Toogle with fx effects
 * 
 * @param element name
 */
function toggleMenuFx(element) {
	//$('div.bottomShadow').toggle();
	if($('#menu').css('display') == 'none') {
		$('#menu ul').css('margin-top','-10px').css('margin-bottom', '10px');
		$('#menu').gfxFadeIn({
			duration : 500,
			easing : 'ease-in',
		});
	} else {
		$('#menu').gfxFadeOut({
			duration : 400,
			easing : 'ease-out',
		}, function(){
			$('#menu ul').css('margin-top','').css('margin-bottom', '');
		});
	}
}

/**
 * Prevent safari browser cache
 */
function preventCacheOnIos() {
	if ((/iphone|ipod|ipad.*os 5/gi).test(navigator.appVersion)) {
		window.onpageshow = function(evt) {
			// If persisted then it is in the page cache, force a reload of the page.
			if (evt.persisted) {
				document.body.style.display = 'none';
				location.reload();
			}	
		};
	} else {
		$(window).bind('unload', function(){});
	}
}

function toogleSort(sort2, sort1, sort0) {
	var setted = false;
	$(sort2 + ' option').each(function(){
		if($(this).val() != $(sort1).val() && $(this).val() != $(sort0).val()) {
			if(!setted) {
				setted = true;
				$(sort2).val($(this).val());
			}
		}
	});
}

