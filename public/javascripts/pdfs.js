;(function($) {
	var search = $('<input>');
	search
		.addClass('filter-list')
		.attr('placeholder', 'Digite para filtrar')
		.prependTo('.courses')
		.filterList(search.parent().find('ul'), 'h2');
})(Zepto);