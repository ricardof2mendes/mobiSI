$(document).ready(function() {
	
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
	
	$('#resultlist').on('click', function(e) {
		e.preventDefault();
		toggle();
	});
	$('#resultmap').on('click', function(e) {
		e.preventDefault();
		toggle();
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
		if(element.indexOf('menu') > 0) {
			if($('#streetsearch').length > 0) {
				$('#container').addClass('menuDownBig');
				$('div.bottomShadow').addClass('bottomShadowBig');
				$('div.streetBottomShadow').toggle();
			} else {
				$('#container').addClass('menuDownSmall');
				$('div.bottomShadow').addClass('bottomShadowSmall');
			}
		}else {
			$('#container').hide();
			$('#whiteBar').hide();
			$('#streetsearch').hide();
		}
		$(element).removeClass('hidden');
	} else {
		$(element).addClass('hidden');
		if(element.indexOf('menu') > 0) {
			if($('#streetsearch').length > 0) {
				$('#container').removeClass('menuDownBig');
				$('div.bottomShadow').removeClass('bottomShadowBig');
				$('div.streetBottomShadow').toggle();
			} else {
				$('#container').removeClass('menuDownSmall');
				$('div.bottomShadow').removeClass('bottomShadowSmall');
			}
		} else {
			$('#container').show();
			$('#whiteBar').show();
			$('#streetsearch').show();
		}
	}
};