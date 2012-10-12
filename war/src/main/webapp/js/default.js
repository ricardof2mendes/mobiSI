$(document).ready(function() {
	'use strict';

	$('.customComboBox').on('click', function() {
		
		$('.comboList').toggle();
	});

	$('.comboList > ul > li').on('click', function(e) {
		$('.customComboBox .comboValue').text($(this).text());
	});

	$('.menuBtn').on('click', function(e) {
		if($('#menu').css('display') == 'none') {
			$('#menu').gfxFadeIn({
				duration : 500,
				easing : 'ease-in',
			});
			
		} else {
			$('#menu').gfxFadeOut({
				duration : 400,
				easing : 'ease-out',
			});
		}
		// $('#menu').toggle();
	});

	// nearest car location
	$('#nearestCar').on('click', function(e) {
		e.preventDefault();
		var element = $(this); 
		fillGeoposition(function(position) {
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
 		
 			if($('#licensePlate').val().length >= 2) {
	 			if($('#latitude').val().length == 0 && $('#longitude').val().length == 0){
		 			fillGeoposition(function(position) {
				 				$('#latitude').val(position.coords.latitude);
				 				$('#longitude').val(position.coords.longitude);
				 				autocompleteList();
		 					});
	 			} else {
	 				autocompleteList();
	 			}
	 		} else {
	 			$('#articleContainer').html('');
	 			$('#articleContainer').css("display", "none");
	 		}
 	});
 	
 	$("#licensePlateBookButton").on('click', function(e){
 		e.preventDefault();
 		fillGeoposition(function(position) {
						$('#latitude').prop('value', position.coords.latitude);
						$('#longitude').prop('value', position.coords.longitude);
						$('#licensePlateBookForm').submit();
					});
 	});
 	
 	// Search for cars
 	$("#searchCarsForBook").on('click', function(e){
 		e.preventDefault();
 		fillGeoposition(function(position) {
							$('#latitude').val(position.coords.latitude);
							$('#longitude').val(position.coords.longitude);
							$('#searchForm').submit();
						});
 	});
 	
 	// Search Criteria cars
 	$('.orderCriteria ul li a').each(function(){
 		var element = $(this);
 		$(this).on('click', function(e) {
 						e.preventDefault();
 						fillGeoposition(function(position) {
							var url = element.prop('href') + "&latitude=" + position.coords.latitude + "&longitude=" + position.coords.longitude;
							window.location.href = url;
						});
 		});
 	});
});

function fillGeoposition(callback) {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(
				callback, 
				function (err) {
					treatGeolocationError(err);
				}, {
					maximumAge: 60000,
					enableHighAccuracy: true 
				});
	} else {
		alert("Geolocation is not supported by this browser.");
	}
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

