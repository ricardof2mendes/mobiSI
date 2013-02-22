$(document).ready(function() {
	'use strict';
	
	//Menu button
	$('.menuBtn').on('click', function(e) {
		e.preventDefault();
		toggleFx('#menu');
	});
	
	// Close legend button
	$('#closeLegend').on('click', function(e) {
		e.preventDefault();
		toggleFx('#legend');
	});
	
	/**
	 * Body with white backgroud
	 */
	if($('.bodyWhite').length > 0) {
		$('body').addClass('noBackground');
	}
	
	// toogle function
	var toggle = function() {
		$('#resultlist').toggle();
		$('#resultmap').toggleClass('hidden');
		$('#container').toggle();
		$('#whiteBar').toggle();
		if($('#addressList').hasClass('hidden')) {
			$('#addressList').removeClass('hidden');
		} else {
			$('#addressList').addClass('hidden');
		}
	};
	// show the results on a list
	$('#resultlist').on('click', function(e) {
		e.preventDefault();
		toggle();
	});
	// show the results on a map
	$('#resultmap').on('click', function(e) {
		e.preventDefault();
		toggle();
	});
	
 	$('#closeConfirm').on('click', function(e) {
 		e.preventDefault();
 		$('body').removeClass('confirmation');
 		$('body').off('scroll');
 	});
	
	/** delele function*/
	$('#query').on('keyup', function() {
		var that = this;
 		// put cross to delete input content
 		if($(this).val().length > 0){
 			$('#streetSearchForm > div > div > div').addClass('delete')
 				.on('click', function() {
 					$(that).val('');
 					Map.clear();
 					$('#streetSearchForm > div > div > div').removeClass('delete').off('click');
 			});
 		} else {
 			$('#streetSearchForm > div > div > div').removeClass('delete').off('click');
 		}
	});
	
	if($('#query').length > 0 && $('#query').val().length > 0) {
		$('#query').trigger('keyup');
	}
	
	// Draw the map
	var configuration = {};

	if($('#paramOrder').length > 0) {
		configuration.type = 'mapASearch';
		configuration.searchParams = {
				price : $('#paramPrice').html(),
				distance : $('#paramDist').html(),
				clazz : $('#paramClazz').html(),
				fuel : $('#paramFuel').html(),
				order : $('#paramOrder').html(),
				latitude : $('#paramLat').html(),
				longitude : $('#paramLong').html()
			};
	} else if($('#paramLicensePlate').length > 0) {
		configuration.type = 'mapACar';
		configuration.licensePlate = $('#paramLicensePlate').html();
	} else if($('#paramZone').length > 0) {
		configuration.type = 'mapAZone';
		configuration.licensePlate = $('#paramZone').html();
	} else if($('#paramAddr').length > 0) {
		configuration.type = 'mapAStreet';
		configuration.searchParams = {
				distance : $('#paramDist').html(),
				address : $('#paramAddr').html(),
				latitude : $('#paramLat').html(),
				longitude : $('#paramLon').html(),
				placeholder : $('#paramCLM').html()
			};
		
		// add event listener to search
		$('#streetSearchForm').on('submit', function(e){
			e.preventDefault();
			Map.change($('#query').val());
			document.getElementById("submit").focus(); 
		});
	}
	
	Map.create(configuration);
});

/**
 * Toggle Menu or Legend
 * @param element
 */
var toggleFx = function (element) {
	if($(element).hasClass('hidden')) {
		$('#container').hide();
		$('#whiteBar').hide();
		$('#streetsearch').hide();
		$('div.streetBottomShadow').hide();
		$(element).removeClass('hidden');
	} else {
		$(element).addClass('hidden');
		$('#container').show();
		$('#whiteBar').show();
		$('#streetsearch').show();
		$('div.streetBottomShadow').show();
	}
};