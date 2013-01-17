$(document).ready(function() {
	
	//Menu button
	$('.menuBtn').on('click', function(e) {
		e.preventDefault();
		toggleFx('#menu', true);
	});
	
	// Close legend button
	$('#closeLegend').on('click', function(e) {
		e.preventDefault();
		toggleFx('#legend', true);
	});
	
	$('#resultlist').on('click', function(e) {
		e.preventDefault();
		toggleSearchList();
	});
	$('#resultmap').on('click', function(e) {
		e.preventDefault();
		toggleSearchList();
	});
	
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

function toggleSearchList() {
	$('#resultlist').toggle();
	if($('#resultmap').css('display') === 'none') {
		$('#resultmap').show();
	} else {
		$('#resultmap').hide();
	}
	$('#resultmap').toggle();
	$('#container').toggle();
	$('#whiteBar').toggle();
	if($('#addressList').css('display') === 'none') {
		$('#addressList').show();
	} else {
		$('#addressList').hide();
	}
}


/**
 * Toggle element
 * @param element
 */
function toggleFx(element, animate) {
	if($(element).css('display') == 'none') {
		if(element.indexOf('menu') > 0) {
			if($('#streetsearch').length > 0) {
				$('#container.street').css('top', '413px !important');
				$('#menu ul').css('margin-top','-10px').css('margin-bottom', '10px');
			} else {
				$('#container').css('top', '366px');
				$('div.bottomShadow').hide();
			}
		}else {
			$('#container').hide();
			$('#whiteBar').hide();
			$('#streetsearch').hide();
		}
		if(animate){
			$(element).gfxFadeIn({
				duration : 500,
				easing : 'ease-in',
			});
		} else {
			$(element).css('display', 'block');
		}		
	} else {
		if(animate) {
			$(element).gfxFadeOut({
				duration : 100,
				easing : 'ease-out',
			});
		} else {
			$(element).css('display', 'none');
		}
		if(element.indexOf('menu') > 0) {
			
			if($('#streetsearch').length > 0) {
				$('#container.street').css('top', '92px !important');
				$('#menu ul').css('margin-top','').css('margin-bottom', '');
			} else {
				$('#container').css('top', '45px');
				$('div.bottomShadow').show();
			}
		} else {
			$('#container').show();
			$('#whiteBar').show();
			$('#streetsearch').show();
		}
	}
}