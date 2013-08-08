;(function($) {
	var customPDFFormSource = $('#custom-pdf-form-template').html();
	var customPDFForm = $(customPDFFormSource);
	customPDFForm.prependTo('.courses');
	var search = customPDFForm.find('input[name="name"]');
	search
		.focus()
		.filterList($('.course-list'), 'h2')
		.on('filtered', function(e, matches) {
			console.log(matches);
			if (matches === 0) {
				customPDFForm.addClass('no-matches');
			} else {
				customPDFForm.removeClass('no-matches');
			}
		});
})(Zepto);