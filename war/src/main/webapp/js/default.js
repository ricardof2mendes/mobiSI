// booking status for ajax pooling
var WAITING = 'WAIT_OBS_';
var ERROR = 'OBS_ERROR';
var IN_USE = 'IN_USE';
var READY = 'READY';
var BOOKED = 'BOOKED';
// car status
var AVAILABLE = 'AVAILABLE'
// unwanted zone
var UNWANTED_ZONE = 'UNWANTED';
// forbidden zone
var FORBIDDEN = 'FORBIDDEN';

// Define Damage zone default as interior
var CAR_DAMAGE_VISIBLE = false;
var CAR_DAMAGE_ZONE = true;
var INCIDENT_CODE = 'INTERNAL_DAMAGE'; // INTERNAL_DAMAGE (default) or EXTERNAL_DAMAGE
var INCIDENT_TYPE = 'SCRATCHED'; // SCRATCHED(default) or SMASHED
var COORDINATE_ROW = '';
var COORDINATE_COL = '';
var LAST_DAMAGE_CIRCLE = '';

/*
 * Default Script
 */
$(document).ready(function() {
	'use strict';
	
	/**
	 * Captures all image errors and replace image wdth no photo
	 */
	$('.carImage').each(function(){
		$(this).on('error', function() {
			var noPhoto = window.devicePixelRatio > 1 ? 'ios-webapp-no-photo@2x.png' : 'ios-webapp-no-photo.png';
			$(this).attr('src', CONTEXT_PATH + '/img/' + noPhoto);
		});
	});
	
	$('.promotionImage').each(function(){
		$(this).on('error', function() {
			var noPhoto = window.devicePixelRatio > 1 ? 'ios-webapp-no-photo-promo@2x.png' : 'ios-webapp-no-photo-promo.png';
			$(this).attr('src', CONTEXT_PATH + '/img/' + noPhoto);
		});
	});

	/** 
	 * Menu open/close 
	 */
	$('.menuBtn').on('click', function(e) {
		e.preventDefault();
		$('#menu').toggle();
	});

	/*
	$('.menuBack').on('click', function(e) {
		e.preventDefault();
		window.history.back();
	});
	*/
	
	/**
	 * Body with white background
	 */
	if($('.bodyWhite').length > 0) {
		$('body').addClass('noBackground');
	}

	// Add lat long function to the url
	var addLatLon = function(e) {
		e.preventDefault();
		var element = $(this); 
		fillGeoposition(function(position) {
							var url = element.prop('href') +
								'&latitude=' + position.coords.latitude +
								'&longitude=' + position.coords.longitude;
							window.location.href = url;
						}, function(err) {
							if (element.prop('id') == 'carDetails') {
								var url = element.prop('href') +
									'&latitude=0' +
									'&longitude=0';
								window.location.href = url;
							} else {							
								treatGeolocationError(err);
							}
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
 		var initialDate = null;
 		var now = new Date();
	    $('#startDate').mobiscroll().datetime({
	    	setText: OK_LABEL,
	    	minuteText: MINUTES_LABEL,
	    	dateFormat: DATE_PATTERN,
	    	dateOrder: 'ddmmyy',
	    	timeFormat: TIME_PATTERN,
	    	timeWheels: 'HHii',
	        minDate: now,
	        display: 'modal',
	        mode: 'scroller',
	        width: 42,
	        onShow : function(html, valueText, inst) {
	        	initialDate = moment(valueText, DATE_TIME_PATTERN);
	        },
	        onClose : function(valueText, inst) {
	        	if($('#endDate').length > 0) {
	        		var endDate = moment($('#endDate').val(), DATE_TIME_PATTERN),
	        			diference = endDate.diff(initialDate, 'minutes', true),
	        			finalDate = moment(valueText, DATE_TIME_PATTERN).add('minutes', diference);

		        	$('#endDate').mobiscroll('setValue', [finalDate.date(), finalDate.month(), 
		        	                                      finalDate.year(), finalDate.hours(), finalDate.minutes()], true);
		        	
	        	}
	        }
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
	        minDate: new Date(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours(), now.getMinutes() + 5, now.getSeconds(), now.getMilliseconds()),
	        display: 'modal',
	        mode: 'scroller',
	        width: 42,
	        onBeforeShow : function(html, inst) {
	        	var finalDate = moment($('#startDate').val(), DATE_TIME_PATTERN).add('minutes', 5);
	        	inst.init({minDate : finalDate.toDate()});
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
        if(begindate[1] === undefined) {
            begindate = begin[0].split('-');
        }
    	var begintime = begin[1].split(':');
    	var limited = $('#limited').attr('data-limit');

         if(limited.length > 0) {
            var limit = limited.split(' ');
            var limitdate = limit[0].split('/');
            if(limitdate[1] === undefined) {
                limitdate = limit[0].split('-');
            }

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
 		
 		if($('#haveFee').length > 0) {
	 		var url = CONTEXT_PATH+'/recent/RecentActivities.action?hasCancelCost=&activityCode=' + $('#activityCode').text();
	 		
			$.get(url, function(data, textStatus, jqXHR){
				if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
					var returnData = eval(data);
					if(returnData && returnData.hasCancelCosts === true) {
						$('#dontHaveFee').addClass('hidden');
						$('#haveFee').removeClass('hidden');
					} else {
						$('#haveFee').addClass('hidden');
						$('#dontHaveFee').removeClass('hidden');
					}
				} else {
					console.log('An error has occurred or the user\'s session has expired!');
					$('html').html(data);
				}
			}, function(data, textStatus, jqXHR) {
				$('html').html(data.responseText);
			});
 		}
		
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

    if($('#advanceSearch').length > 0) {
        var showNext = location.search.split('searchCarsAdvance=')[1]
        if(showNext !== undefined) {
            $('body').addClass('confirmation');

            $('body').on('touchmove', 'body', function(e){
                e.preventDefault();
            });
        }
    }

 	
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
 		var data = $('#state').text(),
 			url = {state : CONTEXT_PATH, 
 				   end : CONTEXT_PATH};
 		
 		if($('#statePooling').hasClass('currentTrip')) {
 			url.state += '/trip/Trip.action?getState=';
 			url.end += '/trip/Trip.action?showMessage=';
 		} else {
 			url.state += '/recent/RecentActivities.action?getState=&activityCode=' + $('#activityCode').text();
 			url.end += '/recent/RecentActivities.action?showMessage=&activityCode=' + $('#activityCode').text() + '&extended=' + $('#extended').text();
 		}
 		
 		urlBO = url;
 		if(data.substring(0, WAITING.length) === WAITING) {
 			timerVarBO = setInterval(bookingProcess , smallAttemptIntervalBO);
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
        $('div.confirm2 > article section').each(function(){
            $(this).hide();
        });
 		// show message
		$('#unlocking').show();
		var url = {
				timeout : UNLOCK_TIMEOUT_INTERVAL,
 				lockunlock: $(this).prop('href'), 
 				pooling :  CONTEXT_PATH + '/trip/Trip.action?getCurrentTrip=',
 				carState: IN_USE,
 				redirect: CONTEXT_PATH + '/trip/Trip.action?finish=&unlockOp=true&successOp=',
 				redirectToDamageReport: CONTEXT_PATH + '/trip/DamageReport.action?finish=&unlockOp=true&successOp='
 			};
		
		lockUnlockAndWait(url);
 	});
 	
 	// Unlock Last Trip (5 Minutes After Booking Closed)
 	$('#unlockLastTrip').on('click', function(e) {
 		e.preventDefault();
        $('div.confirm2 > article section').each(function(){
            $(this).hide();
        });
 		// show message
		$('#unlocking').show();
		var url = {
				timeout : UNLOCK_TIMEOUT_INTERVAL,
 				lockunlock: $(this).prop('href'), 
 				pooling :  CONTEXT_PATH + '/trip/Trip.action?getLastTrip=',
 				carState: AVAILABLE,
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
 		} else if($("#zone").text() === FORBIDDEN) {
 			$('#forbiddenZoneError').show();
 			$('body').addClass('confirmation');
			$('body').on('touchmove', 'body', function(e){
				e.preventDefault();
			});
 		} else {
             $('#justConfirmLockEndTrip').show();
             $('body').addClass('confirmation');
             $('body').on('touchmove', 'body', function(e){
                 e.preventDefault();
             });
 		} 		
 	});

 	// Lock & End trip
 	$('#lockEndTrip').on('click', function(e) {
 		e.preventDefault();
        $('div.confirm2 > article section').each(function(){
            $(this).hide();
        });
		$('#locking').show();
		var url = {
				timeout : LOCK_TIMEOUT_INTERVAL,
				lockunlock: $(this).prop('href'), 
				pooling :  CONTEXT_PATH + '/trip/Trip.action?getCurrentTrip=', 
				redirect: CONTEXT_PATH + '/trip/Trip.action?finish=&successOp='
			};
		lockUnlockAndWait(url);
 	});
 	
 	$('#lockEndTripUnwanted').on('click', function(e) {
 		e.preventDefault();
        $('div.confirm2 > article section').each(function(){
            $(this).hide();
        });
		$('#locking').show();
		var url = {
				timeout : LOCK_TIMEOUT_INTERVAL,
				lockunlock: $(this).prop('href'), 
				pooling :  CONTEXT_PATH + '/trip/Trip.action?getCurrentTrip=', 
				redirect: CONTEXT_PATH + '/trip/Trip.action?finish=&successOp='
			};
		lockUnlockAndWait(url);
 	});
 	
 	// Lock car
 	$('#lockCar').on('click', function(e) {
 		e.preventDefault();
        $('div.confirm2 > article section').each(function(){
            $(this).hide();
        });
		$('#locking').show();
		var url = {
				timeout : LOCK_TIMEOUT_INTERVAL,
				lockunlock: $(this).prop('href'), 
				pooling :  CONTEXT_PATH + '/trip/Trip.action?getCurrentTrip=', 
				redirect: CONTEXT_PATH + '/trip/Trip.action?finish=&successOp=',
				carState: BOOKED
			};
		lockUnlockAndWait(url);
 	});
 	
 	// Lock Last Trip (5 Minutes After Booking Closed)
 	$('#lockLastTrip').on('click', function(e) {
 		e.preventDefault();
        $('div.confirm2 > article section').each(function(){
            $(this).hide();
        });
		$('#locking').show();
		var url = {
				timeout : LOCK_TIMEOUT_INTERVAL,
				lockunlock: $(this).prop('href'), 
				pooling :  CONTEXT_PATH + '/trip/Trip.action?getLastTrip=', 
				redirect: CONTEXT_PATH + '/trip/Trip.action?finish=&successOp=',
				carState: BOOKED
			};
		lockUnlockAndWait(url);
 	});	
 	 	
 	// click on li to change checkbox
 	$('#check').on('click', function(){
 		$('#check input[type="checkbox"]').on('click', function(e){
 			e.stopPropagation();
 		});
 	 	$('#check input[type="checkbox"]').trigger('click');
 	});
 	
 	/*********************************  Begin - Damages Listeners ************************/
 	
 	//TODO embarros: Get the session data if exists and display at the image. (To do this the user should be able to delete damage unreported)
 	// Clear the session data - Remove possibles data from page reload or error's on submit
 	sessionStorage.clear();
 	
 	
 	//Display the selected car damage area image (Interior or Exterior)
 	$('#damageCarAreaInterior, #damageCarAreaExterior').click(function(e){
 		e.preventDefault();
 		
 		// Check if the click element it's already selected
 		if ( (CAR_DAMAGE_ZONE && (this == $('#damageCarAreaInterior')[0])) || 
 				(!CAR_DAMAGE_ZONE && (this == $('#damageCarAreaExterior')[0]))) {
 			return;
 		}
 		
 		CAR_DAMAGE_ZONE = !CAR_DAMAGE_ZONE;
 		$('#damageCarAreaInterior').removeClass('selected');
 		$('#damageCarAreaExterior').removeClass('selected');
 		
 		// Switch the image to the selected area
 		if (CAR_DAMAGE_ZONE) {
 			$('#imageInternal').css('display', 'block');
 			$('#imageExternal').css('display', 'none');
 			$('#damageCarAreaInterior').addClass('selected');
 			$('input[name=incidentCode]').attr('value', 'INTERNAL_DAMAGE');
 			// 	If exists damage to report is selected
 	 		if ( CAR_DAMAGE_VISIBLE ) {
 	 			$('.interiorRect').show();
 	 		}
 		} else {
 			$('#imageExternal').css('display', 'block');
 			$('#imageInternal').css('display', 'none');
 			$('#damageCarAreaExterior').addClass('selected');
 			$('input[name=incidentCode]').attr('value', 'EXTERNAL_DAMAGE');
 			// 	If exists damage to report is selected
 	 		if ( CAR_DAMAGE_VISIBLE ) {
 	 			$('.exteriorRect').show();
 	 		}
 		}

 	});
 	
 	
 	// Select Car Damage Type - Damage
 	$('#typeScratched').on('click', function(e){
 		e.preventDefault();
 		
 		INCIDENT_TYPE  = 'SCRATCHED';
 		$(this).addClass('selected');
 		$('#typeSmashed').removeClass('selected');
 		$('input[name=incidentType]').attr('value', 'SCRATCHED');
 	});

 	// Select Car Damage Type - Dirtiness
 	$('#typeSmashed').on('click', function(e){
 		e.preventDefault();
 		
 		INCIDENT_TYPE  = 'SMASHED';
 		$(this).addClass('selected');
 		$('#typeScratched').removeClass('selected');
 		$('input[name=incidentType]').attr('value', 'SMASHED');
 	});
 	
 	
 	// Create Damage Report Button Listner
 	$('input[name=createDamageReport]').on('click', function(e){
 		e.preventDefault();
 		CAR_DAMAGE_VISIBLE = true;
 		// Change buttons
 		$('#checkIfExistDamages').hide();
 		if (CAR_DAMAGE_ZONE) {
 			// Show the interior map
 			$('.interiorRect').show();
 			$('.exteriorRect').hide();
 		} else {
 			// Show the exterior map
 			$('.interiorRect').hide();
 			$('.exteriorRect').show();
 		}
 		$('#cancelReportDamage').show();
 	});
 	
 	
 	// Get Image Map Clicked Zone
 	$('rect').on('click', function(e){

 		COORDINATE_COL = e.target.attributes.col.value;
 		COORDINATE_ROW = e.target.attributes.row.value;
 		
 		$('input[name=rowCoord]').attr('value', COORDINATE_ROW);
 		$('input[name=colCoord]').attr('value', COORDINATE_COL);
 	
 		$('body').addClass('confirmation');
 		
 		if (CAR_DAMAGE_ZONE) {
 			var cxValue = ((parseInt(COORDINATE_COL))*33)-16;
 			var cyValue = ((parseInt(COORDINATE_ROW)-10)*25)-12;
 			// Add a new interior damage ball
 			var circle= makeSVG('circle', {cx: cxValue, cy: cyValue, r:10, row:COORDINATE_ROW, col: COORDINATE_COL, class:'toReport'});
 			$("#imageInternal").append(circle);
 			LAST_DAMAGE_CIRCLE = circle;
 		} else {
 			// Add a new exterior damage ball
 			var cxValue = ((parseInt(COORDINATE_COL))*33)-12;
 			var cyValue = ((parseInt(COORDINATE_ROW)+1)*27)-13;
 			// Add a new interior damage ball
 			var circle= makeSVG('circle', {cx: cxValue, cy: cyValue, r:10, row:COORDINATE_ROW, col: COORDINATE_COL, class:'toReport'});
 			$("#imageExternal").append(circle);
 			LAST_DAMAGE_CIRCLE = circle;
 		}
		$('#sendOrReportNew').show();
 	});
 	
 	/**
 	 * Display the dialog to remove the selected damage circle
 	 */
 	var selectedCircle;
 	$('circle.toReport').live('click', function(e){
 		selectedCircle = e.target;
 		$('body').addClass('confirmation');
 		$('#removeCircleOrCancel').show();
 	});
 	
 	$('#removeClickedCirle').on('click',function(e){
 		e.preventDefault();
 		
 		for(var i=0;i<damagesItems.length;i++){
 			if(damagesItems[i].id == selectedCircle.attributes.row.value+"-"+selectedCircle.attributes.col.value){
 				findAndRemove(damagesItems, 'id', damagesItems[i].id);
 				sessionStorage.setItem("damages", JSON.stringify(damagesItems));
 				if (CAR_DAMAGE_ZONE) {
 		 			$(selectedCircle).remove();
 		 		} else {
 		 			$(selectedCircle).remove();
 		 		}
 			}
 		}
 		$('body').removeClass('confirmation');
 		$('#removeCircleOrCancel').hide();
 	});
 	
 	function findAndRemove(array, property, value) {
 	   $.each(array, function(index, result) {
 	      if(result[property] == value) {
 	          //Remove from array
 	          array.splice(index, 1);
 	      }    
 	   });
 	}
 	 	
 	var hasImage = [false,false,false,false,false,false,false,false,false,false];
 	var imgIndex = 1;
 	// Check for changes of upload files
 	$("#upload1, #upload2, #upload3 ,#upload4, #upload5, #upload6, #upload7, #upload8 ,#upload9, #upload10").on("change", function(e){	
 		// Create a image preview
 		if (typeof (FileReader) != "undefined") {
            var regex = /^([a-zA-Z0-9\s_\'\,\(\)\\.\-:])+(.jpg|.jpeg|.gif|.png|.bmp)$/;
            for (var i = 0; i < this.files.length; i++) {
                var file = this.files[i];
                if (regex.test(file.name.toLowerCase())) {
                    var reader = new FileReader();
                    reader.onloadend = function (e1) {
                    	// Create and append the thumbnail
                    	var img = $('<img id="'+imgIndex+'" class="selectedImg" width="100px" height="100px">');
                    	img.attr('src', e1.target.result );
                    	img.appendTo('.picSelectorSnap');
                    	$('.picSelectorSnap').css('width', ''+100*(i+1));
                    }
                    // Mark position as unavailable
            		hasImage[imgIndex-1] = true;
//            		sessionStorage.setItem("image"+imgIndex, e.target.value);
                    reader.readAsDataURL(file);
                } else {
                    alert(file.name + " is not a valid image file.");
                    return false;
                }
            }
        } else {
            alert("This browser does not support HTML5 FileReader.");
        }
 		$('#reportNewBtn').trigger('click');
 	});
 	
 	
 	
 	$('.picSelectorSnap').on('click', function(e){
 		e.preventDefault();
 		console.log(e.target);
 		$('body').addClass('confirmation');
 		$('#removeSelectedImg').show();
 		
 		$('#removeImg').on('click', function(e1){
 			e1.preventDefault();

 			var id = $(e.target).attr('id');
 			$(e.target).remove();
 			
 			// Clear the input of removed image
 	        $('#upload'+id).val('');
 	        // Set position as available
 	        hasImage[id-1] = false;
 	        
 			
 			$('body').removeClass('confirmation');
 	        $('#removeImg').parent().hide();
 	        
 	        
 		});
 		
 	});
 	
 	
	// Display the modal to choose between take picture or select from galery
 	$('.picImage').on('click', function(e){
 		e.preventDefault();
 		$('body').addClass('confirmation');
 		$('#openCameraOrGalery').show();
 	});
 	
 	// Cancel Button
 	$('.cancelBtn').on('click',function(e){
 		e.preventDefault();
 		sessionStorage.clear();
 		location.reload();

 	});
 	
	// Cancel Button
 	$('.cancelRemoveBtn').on('click',function(e){
 		e.preventDefault();
 		
 		$('body').removeClass('confirmation');
 		$('#removeCircleOrCancel').hide();
 	});
 	
 	
 	// Open Galery Button
 	$('.openGaleryBtn').on('click', function(e){
 		e.preventDefault();
 		
 		// Get the first free position
 		for(var k = 0; k < hasImage.length; k++) {
        	if (!hasImage[k]){
        		imgIndex = k+1;
        		break;
        	}
        }

 		$('#noPicturesMessage').hide();
 		$('#upload'+imgIndex).trigger('click');
 		$('body').removeClass('confirmation');
 		$('#openCameraOrGalery').hide();
 	});
    
 	var damagesItems = new Array();
 	$('#reportNewBtn').on('click', function(e){
 		e.preventDefault();

 		damagesItems.push({id:COORDINATE_ROW+'-'+COORDINATE_COL, row:COORDINATE_ROW, col:COORDINATE_COL})
 		sessionStorage.setItem("damages", JSON.stringify(damagesItems));

 		$('body').removeClass('confirmation');
 		$('#sendOrReportNew').hide();
	
 	});
 	
 	$('input[name=continueBtn]').on('click',function(e){
 		e.preventDefault();
 		
 		// Check if exists damages to report
 		if (sessionStorage.getItem("damages")) {
 			// Hide the zone images and display the damage info
 			$(".selectDamage").hide();
 	 		$(".damageDetails").show();
 	 		$('body').removeClass('confirmation');
 	 		if ($('.picSelectorSnap img').size() == 0) {
 	 			$('.picSelectorSnap').unbind();
 	 		}
 	 		$('#sendOrReportNew').hide();
 		} else {
 			$('body').addClass('confirmation');
 	 		$('#noDamageToReport').show();
 		}
 	});
 	
 	// Send Report Button
 	$('input[name=submitDamageReport]').on('click', function(e){
 		
 		var rowC = "";
 		var colC = "";
 		
 		for (var i=0;i<damagesItems.length;i++){
 			rowC = rowC + damagesItems[i].row + ", ";
 			colC = colC + damagesItems[i].col + ", ";
 		}
 		
 		$('input[name=rowCoord]').val(rowC);
 		$('input[name=colCoord]').val(colC);
 		
 		if ($('#damageDescription').val() == "") {
 			$('#errorForm').html("<section class=\"errors\"><div><strong>Unable to submit.</strong></div><ul><li>Description is a required field</li></ul></section>");
 			$('html,body').animate({scrollTop: $("#errorForm").offset().top}, 'slow');
 			return false;
 		}
 		
 		$('body').addClass('confirmation');
 		$('#sendingReport').show();
 		sessionStorage.clear();

 	});
 	
 	$('.removeLastDamage').on('click', function(e){
 		e.preventDefault();
 		$(LAST_DAMAGE_CIRCLE).remove();
 		$('body').removeClass('confirmation');
        $(e.target).parent().hide();
        $('#removeImg').unbind('click');
 	});
 	
// 	$('input[name=continueToTripBtn]').on('click', function(e){
// 		e.preventDefault();
// 		console.log("Continue Clicked");
// 	});
 	
 	//if (!$('#validatingCarState').hasClass('hidden') && $('#validatingCarState').length) {
 	if ($('#carState').length && ($('#carState').attr('value').indexOf("IN_USE") > -1) && ( ($('#validatingCarState').length) && (!$('#validatingCarState').hasClass('hidden')))) {
 		$('div.confirm2 > article section').each(function(){
            $(this).hide();
        });
 		
		var url = {
				timeout : UNLOCK_TIMEOUT_INTERVAL,
				pooling : CONTEXT_PATH + '/trip/Trip.action?getCarState=',
 				carState: READY,
 				redirect: CONTEXT_PATH + '/trip/Trip.action',
 				resultAction: 'TIMEOUT'
 			};
		
		validateCarState(url);
 	}
 	
/*********************************  End - Damages Listeners ************************/
 	
});

//auxiliar variables for lock unlock pooling
var ATTEMPT_FRACTION = 3;
var smallAttemptIntervalLU = Math.floor(POOLING_INTERVAL / ATTEMPT_FRACTION);
var largeAttemptIntervalLU = POOLING_INTERVAL;
var numAttemptsLU = 0;
var retriesLU;
var timerVarLU;
var urlLU;
var dataLU;
var textStatusLU;
var jqXHRLU;

function displayTime(){
	var time = new Date();
	console.log("Current time: " + time.getHours() + ":" + time.getMinutes() + ":" + time.getSeconds());
}

function lockUnlockProcess(){
	if(retriesLU !== 0) {
		$.get(urlLU.pooling, 
				function(dataLU, textStatusLU, jqXHRLU){
					if (jqXHRLU.getResponseHeader('Stripes-Success') === 'OK') {
						var evaluated = eval(dataLU);	
						
						if(evaluated == null || (urlLU.carState && urlLU.carState === evaluated.carState && evaluated.carState !== $('#carState').text())) {
							clearInterval(timerVarLU); 
							if (urlLU.redirectToDamageReport == null) {
								window.location.href = urlLU.redirect + 'true' + '&isClosed=false' + '&keysNotReturned=false' + '&keysAlreadyReturned=false' + '&doorsAlreadyOpen=false' + '&doorsAlreadyClosed=false' + '&unableToCommunicate=false';
							} else {
								window.location.href = urlLU.redirectToDamageReport + 'true' + '&isClosed=false' + '&keysNotReturned=false' + '&keysAlreadyReturned=false' + '&doorsAlreadyOpen=false' + '&doorsAlreadyClosed=false' + '&unableToCommunicate=false';
							}																		 		
				 		}else if(evaluated.errorCode && evaluated.errorCode.value && evaluated.errorCode.value === 'TIMEOUT'){
				 			clearInterval(timerVarLU); 
							window.location.href = urlLU.redirect + 'false'  + '&isClosed=false' + '&keysNotReturned=false' + '&keysAlreadyReturned=false' + '&doorsAlreadyOpen=false' + '&doorsAlreadyClosed=false' + '&unableToCommunicate=true';				
				 		}else if(evaluated.errorCode && evaluated.errorCode.value && evaluated.errorCode.value === 'KEY_NOT_RETURNED'){
				 			clearInterval(timerVarLU); 
							window.location.href = urlLU.redirect + 'false'  + '&isClosed=false' + '&keysNotReturned=true' + '&keysAlreadyReturned=false' + '&doorsAlreadyOpen=false' + '&doorsAlreadyClosed=false' + '&unableToCommunicate=false';				
				 		}else if(evaluated.errorCode && evaluated.errorCode.value && evaluated.errorCode.value === 'KEY_ALREADY_RETURNED'){
				 			clearInterval(timerVarLU); 
							window.location.href = urlLU.redirect + 'false'  + '&isClosed=false' + '&keysAlreadyReturned=true' + '&keysNotReturned=false' + '&doorsAlreadyOpen=false' + '&doorsAlreadyClosed=false' + '&unableToCommunicate=false';		
				 		}else if(evaluated.errorCode && evaluated.errorCode.value && evaluated.errorCode.value === 'DOORS_ALREADY_OPEN'){
				 			clearInterval(timerVarLU); 
							window.location.href = urlLU.redirect + 'false' + '&isClosed=false' + '&keysNotReturned=false' + '&keysAlreadyReturned=false' + '&doorsAlreadyOpen=true' + '&doorsAlreadyClosed=false' + '&unableToCommunicate=false';		
				 		}else if(evaluated.errorCode && evaluated.errorCode.value && evaluated.errorCode.value === 'DOORS_ALREADY_CLOSED'){
				 			clearInterval(timerVarLU); 
							window.location.href = urlLU.redirect + 'false' + '&isClosed=false' + '&keysNotReturned=false' + '&keysAlreadyReturned=false' + '&doorsAlreadyOpen=false' + '&doorsAlreadyClosed=true' + '&unableToCommunicate=false';		
				 		}else if(evaluated.errorCode && evaluated.errorCode.value && evaluated.errorCode.value === 'DOORS_OPENED'){
				 			clearInterval(timerVarLU); 
							window.location.href = urlLU.redirectToDamageReport + 'true' + '&isClosed=true' + '&keysNotReturned=false' + '&keysAlreadyReturned=false' + '&doorsAlreadyOpen=false' + '&doorsAlreadyClosed=false' + '&unableToCommunicate=false';		
				 		}else if(evaluated.errorCode && evaluated.errorCode.value && evaluated.errorCode.value === 'DOORS_CLOSED'){
				 			clearInterval(timerVarLU); 
				 			if (urlLU.redirectToDamageReport == null) {
								window.location.href = urlLU.redirect + 'true' + '&isClosed=false' + '&keysNotReturned=false' + '&keysAlreadyReturned=false' + '&doorsAlreadyOpen=false' + '&doorsAlreadyClosed=false' + '&unableToCommunicate=false';
							} else {
								window.location.href = urlLU.redirectToDamageReport + 'true' + '&isClosed=false' + '&keysNotReturned=false' + '&keysAlreadyReturned=false' + '&doorsAlreadyOpen=false' + '&doorsAlreadyClosed=false' + '&unableToCommunicate=false';
							}		
				 		} else if (urlLU.carState && (urlLU.carState.indexOf(evaluated) > -1)) {
				 			clearInterval(timerVarLU);
				 			//$('body').removeClass('confirmation');
//				 			$('#statePooling').hide();
//				 			$('#carStateToChange').html(READY);
				 			window.location.href = urlLU.redirect;
				 			
				 		}
						
			        } else {
			            console.log('An error has occurred or the user\'s session has expired!');
			            $('html').html(dataLU);
			        }
			    });
		
		// update counters;
		retriesLU--;
		numAttemptsLU++;
		
		if(numAttemptsLU == ATTEMPT_FRACTION){
			clearInterval(timerVarLU);
			timerVarLU = setInterval( lockUnlockProcess , largeAttemptIntervalLU);
		}
	} else {
		clearInterval(timerVarLU);
		window.location.href = urlLU.redirect + '?successOp=false'  + '&isClosed=false' + '&keysNotReturned=false' + '&keysAlreadyReturned=false' + '&doorsAlreadyOpen=false' + '&doorsAlreadyClosed=false' + '&unableToCommunicate=true';					
	}	
}

function lockUnlockAndWait(url) {
	urlLU = url;
	// add modal
	$('body').addClass('confirmation');
	$('body').on('touchmove', 'body', function(e){
		e.preventDefault();
	});
	// do active pooling
	$.get(url.lockunlock, 
		function(data, textStatus, jqXHR){
			dataLU = data;
			textStatusLU = textStatus;
			jqXHRLU = jqXHR;
			if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
				if(eval(dataLU) === true) {
					retriesLU = Math.floor(url.timeout / smallAttemptIntervalLU) + ATTEMPT_FRACTION - 1;
					timerVarLU = setInterval( lockUnlockProcess , smallAttemptIntervalLU);
				}
	        } else {
	            console.log('An error has occurred or the user\'s session has expired!');
	            $('html').html(dataLU);
	        }
	    });
}

function validateCarState(url) {
	urlLU = url;
	// add modal
	//$('body').addClass('confirmation');
	// show message
	$('#statePooling').show();
	// do active pooling
	$.get(url.pooling, 
		function(data, textStatus, jqXHR){
			dataLU = data;
			textStatusLU = textStatus;
			jqXHRLU = jqXHR;
			if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
				
				var state = eval(dataLU);
				if(state != null && ((state.indexOf(IN_USE) >-1 ) || (state.indexOf(READY) >-1 ))) {
					retriesLU = Math.floor(url.timeout / smallAttemptIntervalLU) + ATTEMPT_FRACTION - 1;
					timerVarLU = setInterval( lockUnlockProcess , smallAttemptIntervalLU);
				}
	        } else {
	            console.log('An error has occurred or the user\'s session has expired!');
	            $('html').html(dataLU);
	        }
	    });
}

//auxiliar variables for booking pooling
var smallAttemptIntervalBO = Math.floor(POOLING_INTERVAL / ATTEMPT_FRACTION);
var largeAttemptIntervalBO = POOLING_INTERVAL;
var numAttemptsBO = 0;
var retriesBO;
var timerVarBO;
var urlBO;
var dataBO;
var textStatusBO;
var jqXHRBO;

function bookingProcess(){
	$.get(urlBO.state, 
			function(data, textStatus, jqXHR){
		 		dataBO = data;
		 		textStatusBO = textStatus;
		 		jqXHRBO = jqXHR;
		 		
				if (jqXHRBO.getResponseHeader('Stripes-Success') === 'OK') {
				var evaluated = eval(dataBO);
				if(evaluated === ERROR) {
					clearInterval(timerVarBO); 
					$('#stateError').show();
					$('body').addClass('confirmation');
					$('body').on('touchmove', 'body', function(e){
						e.preventDefault();
					});
		 		} else if(evaluated != null && evaluated.substring(0, WAITING.length) !== WAITING){
		 			window.location.href = urlBO.end;
		 		}
				
				// update counters;
				numAttemptsBO++;
				
				if(numAttemptsBO == ATTEMPT_FRACTION){
					clearInterval(timerVarBO);
					timerVarBO = setInterval( bookingProcess , largeAttemptIntervalBO);
				}
				
	        } else {
	            console.log('An error has occurred or the user\'s session has expired!');
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
                $('#zone').html('');
                if (data == null || data.length != 1) {
                    $('#zone').append('<option value="">' + ZONE_ALL_LABEL + '</option>');
                }
                if (data != null) {
                    $(data).each(function(e) {
                        if (this != null)
                            $('#zone').append('<option value="' + this + '">' + this + '</option>');
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
 * Toogle sort combo
 * @param sort2
 * @param sort1
 * @param sort0
 */
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

/**
 * Create the SVG element with attributes.
 * 
 * @param tag the element type(rect, circle,..)
 * @param attrs the element attributes
 * @returns the created element
 */
function makeSVG(tag, attrs) {
    var el= document.createElementNS('http://www.w3.org/2000/svg', tag);
    for (var k in attrs)
        el.setAttribute(k, attrs[k]);
    return el;
}
