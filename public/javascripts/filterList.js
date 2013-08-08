;(function($) {
	$.extend($.fn, {
		filterList: function(list, titleSelector) {
			return this.on('keyup', function() {
				var $input = $(this);
				var filterVal = $input.val();
				var $listItems = $(list).find('li')
				$listItems.removeClass('filtered');
				if (filterVal != "") {
					$listItems.each(function() {
						var $this = $(this);
						var title = $this.find(titleSelector);
						if (!title.text().contains(filterVal)) {
							$this.addClass('filtered');
						}
					});
				}
				$input.trigger('filtered', $listItems.filter(':not(.filtered)').length);
			});
		}
	});
})(Zepto);