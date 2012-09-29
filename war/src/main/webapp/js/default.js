/*!
 * $Id: default.js,v 1.5 2012/09/17 14:21:01 ssantos Exp $
 *
 * Copyright (c) Present Technologies Lda., All Rights Reserved.
 * (www.present-technologies.com)
 *
 * This software is the proprietary information of Present Technologies Lda.
 * Use is subject to license terms.
 *
 * Last changed on $Date: 2012/09/17 14:21:01 $
 * Last changed by $Author: ssantos $
 */
 
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
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(
					function(position) {
						$('#latitude').attr('value', position.coords.latitude);
						$('#longitude').attr('value', position.coords.longitude);
						$('#geolocation').submit();
					}, 
					function (err) {
						if (err.code == 1) {
							alert('The user denied the request for location information.');
						} else if (err.code == 2) {
							alert('Your location information is unavailable.');
						} else if (err.code == 3) {
							alert('The request to get your location timed out.');
						} else {
							alert('An unknown error occurred while requesting your location.');
						}
					});
		} else {
			alert("Geolocation is not supported by this browser.");
		}
	});
	
	// license plate autocomplete
	 	$('#licensePlate').on('keyup', function() {
	 		if($('#licensePlate').val().length >= 2) {
	 			var returnData = null;
	 			$
	 			.ajax({
			 		type : 'GET',
	 				url : contextPath+'/book/LicensePlate.action?autocomplete=&q='+$('#licensePlate').val(),
		 			dataType : 'text',
	 				cache : false,
	 				success : function(data) {
	 					returnData = data;
	 				},
	 				error : function(jqXHR, textStatus, errorThrown) {
	 					alert(xhr.status + ' ' + textStatus + ' ' + errorThrown);
	 				},
	 				complete : function(jqXHR, textStatus) {
	 					if (textStatus == 'success') {
	 						if (returnData != null) {
	 							alert(returnData);
	 	 						var divHtml = "";
	 	 			            var onClk = "itemClick(this,'autocompleteContainer')";
	 	 			            $.each(returnData, function() {
	 	 			                divHtml += "<div onclick=" + onClk + " class='completionListItem'>" + this + "</div>";
	 	 			            });
	 	 			            $("#autocompleteContainer").html(divHtml);
	 	 			            $("#autocompleteContainer").css("display", "block");
	 	 						
	 						} else {
	 							
	 						}
	 					}
	 				}
	 			});
			
	 		}
	 	});
	
//	$('#licensePlate').autocomplete(
//			contextPath+'/book/LicensePlate.action?autocomplete='
//	);
});
