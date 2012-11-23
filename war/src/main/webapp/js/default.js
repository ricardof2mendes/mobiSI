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
		toggleFx('#menu');
	});

	/** 
	 * Nearest car location 
	 */
	$('#nearestCar').on('click', function(e) {
		e.preventDefault();
		var element = $(this); 
		fillGeoposition(function(position) {
							var url = element.prop('href')
									+ "&latitude=" + position.coords.latitude
									+ "&longitude=" + position.coords.longitude;
							window.location.href = url;
						}, true);
	});
	
	/** Find me a car for later */
	$('#findForLater').on('click', function(e) {
		e.preventDefault();
		var element = $(this); 
		fillGeoposition(function(position) {
							var url = element.prop('href')
									+ "&latitude=" + position.coords.latitude
									+ "&longitude=" + position.coords.longitude;
							window.location.href = url;
						}, true);
	});
	
	/** Search location link */
	$('#locationLink').on('click', function(e) {
		e.preventDefault();
		var element = $(this);
		var url = element.prop('href');
		url += "&" + $('#latitude').prop('name') + '=' + $('#latitude').val(); 
		url += "&" + $('#longitude').prop('name') + '=' + $('#longitude').val(); 
		url += "&" + $('#startDate').prop('name') + '=' + $('#startDate').val(); 
		url += "&" + $('#distance').prop('name') + '=' + $('#distance').val(); 
		url += "&" + $('#carClazz').prop('name') + '=' + $('#carClazz').val(); 
		url += "&" + $('#fromMyCarClub').prop('name') + '=' + $('#fromMyCarClub').val(); 
		url += "&" + $('#startSending').prop('name') + '=' + $('#startSending').val(); 
		url += "&" + $('#stopSending').prop('name') + '=' + $('#stopSending').val(); 
		url += "&" + $('#maxMessages').prop('name') + '=' + $('#maxMessages').val(); 
		window.location.href = url;
	});
	
	/**
	 * Prevent license plate input form to submit
	 */ 
	$('#licensePlateBookForm').submit(function(e){
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
 					$('#articleContainer').html('').css("display", "none");
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
		 					});
	 			} else {
	 				autocompleteList();
	 			}
			}, 1000);
			
 		} else {
 			$('#articleContainer').html('').css("display", "none");
 		}
 	});
 	
 	/**
 	 * Search for cars 
 	 */
 	$("#searchCarsForBook").on('click', function(e){
 		e.preventDefault();
 		fillGeoposition(function(position) {
							$('#latitude').val(position.coords.latitude);
							$('#longitude').val(position.coords.longitude);
							$('#searchForm').submit();
						}, true);
 	});
 	
 	/**
 	 * Select autocomplete
 	 */
 	if($('#location').length > 0){
 		autocompleteZones();
 		
 		$('#location').on("change", function(){
 	 		autocompleteZones();
 	 	});
 	}
 	
 	/**
 	 *  Calendar setup
 	 */
 	if($('#startDate').length > 0){
 		var now = new Date();
	    $('#startDate').scroller({
	    	preset: 'datetime',
	    	dateFormat: DATE_PATTERN,
	    	dateOrder: 'ddmmyy',
	    	timeFormat: TIME_PATTERN,
	    	timeWheels: 'HHii',
	        minDate: new Date(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours(), now.getMinutes(), now.getSeconds(), now.getMilliseconds()),
	        display: 'modal',
	        theme: 'ios',
	        width: 45,
	        mode: 'scroller',
	        onShow: function(html, inst) {
	        }
	    });    
 	}
 	
 	if($('#endDate').length > 0){
	 	var now = new Date();
	    $('#endDate').scroller({
	    	preset: 'datetime',
	    	dateFormat: DATE_PATTERN,
	    	dateOrder: 'ddmmyy',
	    	timeFormat: TIME_PATTERN,
	    	timeWheels: 'HHii',
	        minDate: new Date(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours()+1, now.getMinutes(), now.getSeconds(), now.getMilliseconds()),
	        display: 'modal',
	        theme: 'ios',
	        width: 45,
	        mode: 'scroller',
	        onShow: function(html, inst) {
	        }
	    });    
 	}
 	
 	/* Modal dialog windows */
 	$('#openConfirm').on('click', function(e) {
 		e.preventDefault();
 		$('body').addClass("confirmation");
 		
 		var windowHeight = document.documentElement.clientHeight;
 		var elementHeight = $(".confirm article").height();
 		$(".confirm article").css({
 		    "margin-top": (windowHeight-elementHeight)/2 + 'px'
 		});
 		
 	});
 	
 	$('#closeConfirm').on('click', function(e) {
 		e.preventDefault();
 		$('body').removeClass("confirmation");
 	});
 	
 	/* report problem license plate search */
 	$('#licenseReport').on('blur', function(){
 		
 		//if($(this).val().length == 8 &&
 			if(	($("#carLicensePlate").length == 0 ||
 				($("#carLicensePlate").length > 0 && $("#carLicensePlate").val() !=  $(this).val()))){
 			
	 		var url = CONTEXT_PATH+'/contacts/ContactsAndDamageReport.action?licensePlateSearch=&licensePlate='+$(this).val();
	
			$.get(url, function(data, textStatus, jqXHR){
				if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
					if (data.indexOf('<html') == -1) {
						$('#searchResults').html(data);
					    $("#searchResults").css("display", "block");
					} else {
					    $('html').html(data);
					}
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
 			$("#searchResults").css("display", "none");
 			$("#searchResults").html('');
 		}
 	});
});


/**
 * Obtain geopostion
 * 
 * @param callback callback function
 * @param isLoading show loading screen
 */
function fillGeoposition(callback, isLoading) {
	if (navigator.geolocation) {
		if(isLoading){
			$('body').addClass("loading");
		}
		navigator.geolocation.getCurrentPosition(callback, function(err) {
			treatGeolocationError(err);
		}, {
			maximumAge : 60000,
			enableHighAccuracy : true
		});
	} else {
		alert("Geolocation is not supported by this browser.");
	}
}

/**
 * Get car list by ajax
 */
function autocompleteList() {
	
	var url = CONTEXT_PATH+'/booking/ImmediateBooking.action?licensePlateAutocomplete=&licensePlate='+$('#licensePlate').val()
					+'&latitude='+$('#latitude').val()
					+'&longitude='+$('#longitude').val();
	
	$.get(url, function(data, textStatus, jqXHR){
		if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
            if (data.indexOf('<html') == -1) {
            	$('#articleContainer').html(data);
                $("#articleContainer").css("display", "block");
                $('#licensePlate').removeClass('autocomplete');
                $('div.delete').toggle();
            } else {
                $('html').html(data);
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
 * Get park list by ajax
 */
function autocompleteZones() {
	var url = CONTEXT_PATH +'/booking/AdvanceBooking.action?getZones=&location='+$('#location').val();
	$.get(url, function(data, textStatus, jqXHR){
		if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
            	data = eval(data);
            	if (data == null || data.length != 1) {
            		$("#zone").html('');
                    $("#zone").append('<option value="">' + ZONE_ALL + '</option>');
                }
                if (data != null) {
                    $(data).each(function(e) {
                        if (this.zone != null)
                            $("#zone").append('<option value="' + this.zone + '">' + this.zone + '</option>');
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
	if (err.code == 1) {
		alert('The user denied the request for location information.');
	} else if (err.code == 2) {
		alert('Your location information is unavailable.');
	} else if (err.code == 3) {
		alert('The request to get your location timed out.');
	} else {
		alert('An unknown error occurred while requesting your location.');
	}
}

/**
 * Toogle with fx effects
 * 
 * @param element name
 */
function toggleFx(element) {
	//$('div.bottomShadow').toggle();
	if($(element).css('display') == 'none') {
		$(element).gfxFadeIn({
			duration : 500,
			easing : 'ease-in',
		});
		
	} else {
		$(element).gfxFadeOut({
			duration : 400,
			easing : 'ease-out',
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
				document.body.style.display = "none";
				location.reload();
			}	
		};
	} else {
		$(window).bind('unload', function(){});
	}
}

