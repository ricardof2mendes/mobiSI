'<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>';

var CONTEXT_PATH = '${pageContext.request.contextPath}';
// localizable strings
var ZONE_ALL_LABEL = '<fmt:message key="book.advance.zone.any" />';
var GEOLOCATION_NOT_SUPPORTED_LABEL = '<fmt:message key="geolocation.alert.msg.not.supported"/>';
var OK_LABEL = '<fmt:message key="calendar.ok"/>';
var MINUTES_LABEL = '<fmt:message key="calendar.minutes"/>';
// date patterns for calendar
var DATE_PATTERN = '${applicationScope.configuration.jsDatePattern}';
var TIME_PATTERN = '${applicationScope.configuration.jsTimePattern}';
// booking status for ajax pooling
var WAITING = 'WAIT_OBS_';
var ERROR = 'OBS_ERROR';
var IN_USE = 'IN_USE';
// unwanted zone
var UNWANTED_ZONE = 'UNWANTED';

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
		$('#menu').toggle();
	});
	
	/**
	 * Body with white backgroud
	 */
	if($('.bodyWhite').length > 0) {
		$('body').addClass('noBackground');
	}
	
	var addLatLon = function(e) {
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
		};
	
	

	/** 
	 * Nearest car location 
	 */
	$('#nearestCar').on('click', addLatLon);
	
	/**
	 * Car details (last trip / current trip)
	 */
	$('#carDetails').on('click', addLatLon);
	
	
	/**
	 * css hack on search car list
	 */
	if($('nav.orderCriteria').length > 0) {
		$('div.bottomShadow').addClass('bottomShadowGray');
	}
	
	/** Find me a car for later */
	$('#findForLater').on('click', function(e) {
		e.preventDefault();
		var element = $(this); 
		fillGeoposition(function(position) {
							var url = element.prop('href') + 
								'&latitude=' + position.coords.latitude + 
								'&longitude=' + position.coords.longitude;
							window.location.href = url;
						}, function() {
							window.location.href = element.prop('href') + '&latitude=&longitude=';
						} ,true);
	});
    
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
            $(this).addClass('uppercase');
 			$('#licensePlateBookForm > div > div').addClass('delete')
 			.on('click', function() {
 				$(that).val('');
 				$('#articleContainer').html('').addClass('hidden');
 					$('#licensePlateBookForm > div > div').removeClass('delete').off('click');
 					$(that).removeClass('uppercase');
 			});
 		} else {
 			$('#licensePlateBookForm > div > div').removeClass('delete').off('click');
 			$(this).removeClass('uppercase');
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
 			$('#articleContainer').html('').addClass('hidden');
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
	    	setText: OK_LABEL,
	    	minuteText: MINUTES_LABEL,
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
	    	setText: OK_LABEL,
	    	minuteText: MINUTES_LABEL,
	    	dateFormat: DATE_PATTERN,
	    	dateOrder: 'ddmmyy',
	    	timeFormat: TIME_PATTERN,
	    	timeWheels: 'HHii',
	        minDate: new Date(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours()+1, now.getMinutes(), now.getSeconds(), now.getMilliseconds()),
	        display: 'modal',
	        mode: 'scroller',
	        width: 42,
	        onShow: function() {
	        	var datetime = $('#startDate').val().split(' ');
	        	var date = datetime[0].split('/');
	        	var time = datetime[1].split(':');
	        	var val = new Date(parseInt(date[2]), parseInt(date[1]) === 0 ? 0 : parseInt(date[1]) - 1, parseInt(date[0]), parseInt(time[0]), parseInt(time[1]), 0, 0);
	        	$('#endDate').scroller('setValue', [val.getDate(), val.getMonth(), 
	        			val.getYear(), val.getHours()+1, val.getMinutes()], true);
	        }
	    });    
 	}
 	
 	if($('#reportDamageDate').length > 0){
	 	var now = new Date();
	    $('#reportDamageDate').mobiscroll().datetime({
	    	setText: OK_LABEL,
	    	minuteText: MINUTES_LABEL,
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
 		var begin = $('#limited').attr('data-begin').split(' ');
    	var begindate = begin[0].split('/');
    	var begintime = begin[1].split(':');
    	
    	var limited = $('#limited').attr('data-limit');
 		
    	if(limited.length > 0) {
    		var limit = limited.split(' ');
 			var limitdate = limit[0].split('/');
 			var limittime = limit[1].split(':');
 			
 			$('#limited').mobiscroll().datetime({
 				setText: OK_LABEL,
 				minuteText: MINUTES_LABEL,
 				dateFormat: DATE_PATTERN,
 				dateOrder: 'ddmmyy',
 				timeFormat: TIME_PATTERN,
 				timeWheels: 'HHii',
 				minDate: new Date(parseInt(begindate[2]), parseInt(begindate[1]) === 0 ? 0 : parseInt(begindate[1])-1, parseInt(begindate[0]), parseInt(begintime[0]), parseInt(begintime[1]), 0, 0),
 				maxDate: new Date(parseInt(limitdate[2]), parseInt(limitdate[1]) === 0 ? 0 : parseInt(limitdate[1])-1, parseInt(limitdate[0]), parseInt(limittime[0]), parseInt(limittime[1]), 0, 0),
 				display: 'modal',
 				mode: 'scroller',
 				width: 42
 			});    
 		} else {
 			$('#limited').mobiscroll().datetime({
 				setText: OK_LABEL,
 				minuteText: MINUTES_LABEL,
 				dateFormat: DATE_PATTERN,
 				dateOrder: 'ddmmyy',
 				timeFormat: TIME_PATTERN,
 				timeWheels: 'HHii',
 				minDate: new Date(parseInt(begindate[2]), parseInt(begindate[1]) === 0 ? 0 : parseInt(begindate[1])-1, parseInt(begindate[0]), parseInt(begintime[0]), parseInt(begintime[1]), 0, 0),
 				display: 'modal',
 				mode: 'scroller',
 				width: 42
 			});
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
 	
 	/* Damage Report problem license plate search */
 	$('#licenseReport').on('blur', function(){
 		var that = this;
 		
		if($(this).val().length > 0 && ($('#carLicensePlate').length == 0 ||
			($('#carLicensePlate').length > 0 && $('#carLicensePlate').val() !=  $(this).val()))){
		
			$(this).addClass('autocomplete');
	 		var url = CONTEXT_PATH+'/contacts/ContactsAndDamageReport.action?licensePlateSearch=&licensePlate='+$(this).val();
	
			$.get(url, function(data, textStatus, jqXHR){
				if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
						$(that).removeClass('autocomplete');
						$('#searchResults').html(data);
					    $('#searchResults').removeClass('hidden');
				} else {
					console.log('An error has occurred or the user\'s session has expired!');
					$('html').html(data);
				}
			}, function(data, textStatus, jqXHR) {
				$('html').html(data.responseText);
			});
		}
 	});
 	
 	// Prevents form submition without licenseplate search
 	$('#licenseReport').on('keypress', function(e){
 		if (e.which == 13) {
 			return false;
 		}
 	});
 	
 	// 
 	$('#licenseReport').on('keyup', function(){
 		if($(this).val().length >= 1){
 			$(this).addClass('uppercase');
 		} else {
 			$(this).removeClass('uppercase');
 			$('#searchResults').html('');
 			$('#searchResults').addClass('hidden');
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
 	
 	// on init hide in needed
 	if($('#appOption').length > 0) {
 		if(!$('#app').prop('checked')) {
 			$('#appOption').hide();
 		}
 		if(!$('#sms').prop('checked')) {
 			$('#smsOption').hide();
 		}
 	}	
 	// on click checkbox
	$('#app').on('click', function(e) {
 		e.stopPropagation();
 		if($('#appOption').prop('selected')) {
 			$('#appOption').removeAttr('selected');
 			$('#emailOption').attr('selected', 'selected');
 		}
 	
		if($('#app').prop('checked')) {
			$('#appOption').show();
 		} else {
 			$('#appOption').hide();
 		}
 	});
	// on click checkbox
	$('#sms').on('click', function(e){
 		e.stopPropagation();
 		if($('#smsOption').prop('selected')){
 			$('#smsOption').removeAttr('selected');
 			$('#emailOption').attr('selected', 'selected');
		}
 		if($('#sms').prop('checked')) {
 			$('#smsOption').show();
 		} else {
 			$('#smsOption').hide();
 		}
 	});
 	
 	// Current trip state pooling
 	if($('#statePooling').length > 0) {
 		// check the state
 		var data = $('#state').text();
 		
 		var url = {state : CONTEXT_PATH, 
 				   end : CONTEXT_PATH};
 		
 		if($('#statePooling').hasClass('currentTrip')) {
 			url.state += '/trip/Trip.action?getState=';
 			url.end += '/trip/Trip.action?showMessage=';
 		} else {
 			url.state += '/recent/RecentActivities.action?getState=&activityCode=' + $('#activityCode').text();
 			url.end += '/recent/RecentActivities.action?showMessage=&activityCode=' + $('#activityCode').text() + '&extended=' + $('#extended').text();
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
 									$('#stateError').show();
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
 	
 	// Unlock
 	$('#unlock').on('click', function(e) {
 		e.preventDefault();
 		// show message
		$('#unlocking').show();
		var url ={
 				lockunlock: $(this).prop('href'), 
 				pooling :  CONTEXT_PATH + '/trip/Trip.action?getCurrentTrip=',
 				state: IN_USE,
 				redirect: CONTEXT_PATH + '/trip/Trip.action?finish=&unlockOp=true&successOp='
 				};
		
		lockUnlockAndWait(url);
 	});
 	
 	// End trip
 	$('#endTrip').on('click', function(e) {
 		e.preventDefault();
 		if($("#zone").text() === UNWANTED_ZONE) {
 			$('#unwantedZoneError').show();
 			$('body').addClass('confirmation');
			$('body').on('touchmove', 'body', function(e){
				e.preventDefault();
			});
 		} else {
 			// show message
 			$('#locking').show();
 			var url = {
 	 				lockunlock: $(this).prop('href'), 
 	 				pooling :  CONTEXT_PATH + '/trip/Trip.action?getCurrentTrip=', 
 	 				redirect: CONTEXT_PATH + '/trip/Trip.action?finish=&successOp='
 	 				};
 			lockUnlockAndWait(url);
 		} 		
 	});

 	// End trip
 	$('#lockEndTrip').on('click', function(e) {
 		e.preventDefault();
		$('#locking').show();
		var url = {
				lockunlock: $(this).prop('href'), 
				pooling :  CONTEXT_PATH + '/trip/Trip.action?getCurrentTrip=', 
				redirect: CONTEXT_PATH + '/trip/Trip.action?finish=&successOp='
		};
		lockUnlockAndWait(url);
 	});
 	
});

function lockUnlockAndWait(url) {
	
	// add modal
	$('body').addClass('confirmation');
	$('body').on('touchmove', 'body', function(e){
		e.preventDefault();
	});
	// do active pooling
	$.get(url.lockunlock, 
			function(data, textStatus, jqXHR){
				if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
					if(eval(data) === true) {
						var times = 15;
						setInterval(function(){
							var that = this;
							if(times !== 0) {
								$.get(url.pooling, 
										function(data, textStatus, jqXHR){
											if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
												var evaluated = eval(data);
												
												if(evaluated.licensePlate === null || (url.state && url.state === evaluated.carState)) {
													clearInterval(that); 
													window.location.href = url.redirect + 'true';										 			
										 		} 
									        } else {
									            console.log('An error has occurred or the user\'s session has expired!');
									            $('html').html(data);
									        }
									    });
								times--;
							} else {
								clearInterval(this);
								window.location.href = url.redirect + 'false';	
							}	
						}, 3000);
					}
		        } else {
		            console.log('An error has occurred or the user\'s session has expired!');
		            $('html').html(data);
		        }
		    });
	
}



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
		alert(GEOLOCATION_NOT_SUPPORTED_LABEL);
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
                    $('#zone').append('<option value="">' + ZONE_ALL_LABEL + '</option>');
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

