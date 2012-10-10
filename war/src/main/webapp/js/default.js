$(document).ready(function() {
	'use strict';

	$('.customComboBox').on('click', function(e) {
		$('.comboList').toggle();
	});

	$('.comboList > ul > li').on('click', function(e) {
		$('.customComboBox .comboValue').text($(this).text());
	});

	$('.menuBtn').on('click', function(e) {
		$('#menu').toggle();
	});

	// nearest car location
	$('#nearestCar').on('click', function(e) {
		e.preventDefault();
		var element = $(this); 
		fillGeoposition(function(position) {
							var url = element.attr('href')
									+ "&latitude=" + position.coords.latitude
									+ "&longitude=" + position.coords.longitude;
							window.location.href = url;
						});
	});
	
	// license plate autocomplete
 	$('#licensePlate').on('keyup', function() {
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
						$('#latitude').attr('value', position.coords.latitude);
						$('#longitude').attr('value', position.coords.longitude);
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
 	
 	// Order cars
 	$('.orderCriteria ul li a').each(function(){
 		var element = $(this); 
 		fillGeoposition(function(position) {
							var url = element.attr('href') + "&latitude=" + position.coords.latitude + "&longitude=" + position.coords.longitude;
							window.location.href=url;
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
	
	var url = contextPath+'/book/LicensePlate.action?autocomplete=&q='+$('#licensePlate').val()+'&latitude='+$('#latitude').val()+'&longitude='+$('#longitude').val();
	
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

