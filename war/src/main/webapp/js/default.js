/*
 * Default Script
 */
$(document).ready(function() {
	'use strict';
	
	// preventCacheOnIos();

	// Combo box open/close
	$('.customComboBox').on('click', function() {
		$('.comboList').toggle();
	});

	// Put Combo box value on input
	$('.comboList > ul > li').on('click', function(e) {
		$('.customComboBox .comboValue').text($(this).text());
	});

	// Menu open/close
	$('.menuBtn').on('click', function(e) {
		toggleFx('#menu');
	});

	// nearest car location
	$('#nearestCar').on('click', function(e) {
		e.preventDefault();
		var element = $(this); 
		fillGeopositionWithLoading(function(position) {
							var url = element.prop('href')
									+ "&latitude=" + position.coords.latitude
									+ "&longitude=" + position.coords.longitude;
							window.location.href = url;
						});
	});
	
	// Prevent license plate input form to submit
	$('#licensePlateBookForm').submit(function(e){
		e.preventDefault();
	});

	// license plate autocomplete
 	$('#licensePlate').on('keyup', function(e) {
 		$('#licensePlate').css('text-transform', 'uppercase');
 		// put cross to delete input content
 		if($('#licensePlate').val().length >= 1){
 			$('#licensePlateBookForm > div > div').addClass('delete')
 				.on('click', function() {
 					$('#licensePlate').val('');
 					$('#articleContainer').html('').css("display", "none");
 					$('#licensePlateBookForm > div > div').removeClass('delete').off('click');
 					$('#licensePlate').css('text-transform', 'none');
 			});
 		} else {
 			$('#licensePlateBookForm > div > div').removeClass('delete').off('click');
 			$('#licensePlate').css('text-transform', 'none');
 		}
 		
 		// add the autocomplete
		if($('#licensePlate').val().length >= 2) {
			// first clear the delay
			clearTimeout(this.timeout);
			// then create the delay call
			this.timeout = setTimeout(function(){
				$('#licensePlate').addClass('autocomplete');
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
 	
 	// Search for cars
 	$("#searchCarsForBook").on('click', function(e){
 		e.preventDefault();
 		fillGeopositionWithLoading(function(position) {
							$('#latitude').val(position.coords.latitude);
							$('#longitude').val(position.coords.longitude);
							$('#searchForm').submit();
						});
 	});
 	
 	// Search Criteria cars
// 	$('.orderCriteria ul li a').each(function(){
// 		var element = $(this);
// 		$(this).on('click', function(e) {
// 						e.preventDefault();
// 						fillGeopositionWithLoading(function(position) {
//							var url = element.prop('href') + "&latitude=" + position.coords.latitude + "&longitude=" + position.coords.longitude;
//							window.location.href = url;
//						});
// 		});
// 	});
});

/**
 * Obtain geopostion and after execute the callback function
 * @param callback
 */
function fillGeoposition(callback) {
	if (navigator.geolocation) {
		geoposition(callback);
	} else {
		alert("Geolocation is not supported by this browser.");
	}
}

/**
 * Obtain geopostion with loading and waiting screen, after execute the callback function
 * @param callback
 */
function fillGeopositionWithLoading(callback) {
	if (navigator.geolocation) {
		$('body').addClass("loading"); 
		geoposition(callback);
	} else {
		alert("Geolocation is not supported by this browser.");
	}
}

function geoposition(callback) {
	navigator.geolocation.getCurrentPosition(callback, function(err) {
		treatGeolocationError(err);
	}, {
		maximumAge : 60000,
		enableHighAccuracy : true
	});
}


function autocompleteList() {
	
	var url = contextPath+'/booking/ImmediateBooking.action?licensePlateAutocomplete=&q='+$('#licensePlate').val()
					+'&latitude='+$('#latitude').val()
					+'&longitude='+$('#longitude').val();
	
	$.get(url, function(data, textStatus, jqXHR){
		if (jqXHR.getResponseHeader('Stripes-Success') === 'OK') {
            if (data.indexOf('<html') == -1) {
            	$('#articleContainer').html(data);
                $("#articleContainer").css("display", "block");
                $('#licensePlate').removeClass('autocomplete');
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

